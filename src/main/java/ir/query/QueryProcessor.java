package ir.query;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Iterator;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import ir.evaluation.Evaluation;
import ir.evaluation.Scores;
import ir.indexer.DocInfo;
import ir.indexer.TermIndexer;
import ir.indexer.TermOccurrence;
import ir.indexer.TokenInfo;
import ir.indexer.TokenOccurrence;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;

public class QueryProcessor {
	private TermIndexer index;
	private Dictionary wordnet;

	public QueryProcessor(TermIndexer index){
		this.index = index;
		try {
			JWNL.initialize(new FileInputStream(System.getProperty("jwnlProp")));
			wordnet = Dictionary.getInstance();
		} catch (FileNotFoundException | JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public HashMap<String,Double> processQuery(String query){
		HashMap<String,Double> queryIndex = new HashMap<String,Double>();
		try {

			StandardTokenizer stream = new StandardTokenizer();
			stream.setReader(new StringReader(query));
			TokenStream tokenStream = new LowerCaseFilter(stream); 
			tokenStream = new StopFilter(tokenStream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
			tokenStream.reset();
			CharTermAttribute token = tokenStream.addAttribute(CharTermAttribute.class);
			StringBuilder str = new StringBuilder();

			while (tokenStream.incrementToken()){
				String term = token.toString();
				str.append(term+"\t");
				//				IndexWord indexWord = wordnet.lookupIndexWord(POS.NOUN, term);
				//				if (indexWord != null){
				//					Synset[] senses = indexWord.getSenses();
				//					for (Synset synset: senses){
				//						Word[] words = synset.getWords(); 
				//						for (Word word: words){
				//							str.append(word.getLemma()+"\t");
				//						}
				//
				//						//					PointerTargetNodeList relatedList = PointerUtils.getInstance().getSynonyms(synset);					
				//						//					Iterator i = relatedList.iterator();
				//						//					while (i.hasNext()){
				//						//						PointerTargetNode synonymNode = (PointerTargetNode)i.next();
				//						//						Synset synonym = synonymNode.getSynset();
				//						//						Word[] words = synonym.getWords();
				//						//						for (Word w: words){
				//						//							str.append(w.getLemma());
				//						//						}
				//						//					}
				//					}
				//				}
			}

			tokenStream.end();
			tokenStream.close();

			stream.setReader(new StringReader(str.toString()));
			TokenStream newTokenStream = new PorterStemFilter(tokenStream);
			newTokenStream.reset();
			CharTermAttribute newToken = newTokenStream.addAttribute(CharTermAttribute.class);
			while (newTokenStream.incrementToken()){
				String term = newToken.toString();
				queryIndex.put(term,1d);
			}
			newTokenStream.end();
			newTokenStream.close();
		} catch (IOException e){//| JWNLException e) {
			e.printStackTrace();
		}
		return queryIndex;
	}


	public ArrayList<Document> generateResults(HashMap<String,Double> queryIndex, String similarityMeasure){

		HashMap<DocInfo, Double> searchResult = new HashMap<DocInfo,Double>();
		double queryVectorLength = 0d;

		//	System.out.println(queryIndex.keySet());

		for (Map.Entry<String, Double> dictionaryEntry: queryIndex.entrySet()){
			String token = dictionaryEntry.getKey();

			if (index.dictionary.containsKey(token)){
				TokenInfo tokenInfo = index.dictionary.get(token);
				queryIndex.put(token, 1*tokenInfo.getIdf());

				for (Map.Entry<Integer, TokenOccurrence> tokenOccEntry: tokenInfo.getOccMap().entrySet()){
					int docId = tokenOccEntry.getKey();
					int tokenCount = tokenOccEntry.getValue().getCount();
					double idf = tokenInfo.getIdf();
					DocInfo docInfo = index.docInfoList.get(docId);

					if (searchResult.containsKey(docInfo)){
						double score = searchResult.get(docInfo);
						double newScore = score + (queryIndex.get(token)*(tokenCount*idf));
						searchResult.put(docInfo, newScore);
					} else {
						searchResult.put(docInfo, queryIndex.get(token)*tokenCount*idf);
					}
				}
			}
		}

		// calculate the lenght of query vector
		//		for (Double l: queryIndex.values()){
		//			queryVectorLength += Math.pow(l, 2);
		//		}
		//		queryVectorLength = Math.sqrt(queryVectorLength);
		//
		//		// calculate similarity score
		//		ArrayList<Document> result = new ArrayList<Document>();
		//		for (Map.Entry<DocInfo, Double> entry: searchResult.entrySet()){
		//			DocInfo docInfo = entry.getKey();
		//			int docId = docInfo.getId();
		//			double dotProduct = entry.getValue();
		//			double denominator = docInfo.getLength())*queryVectorLength;
		//			double score = dotProduct/denominator;
		//			String snippet = docInfo.getSnippet();
		//                        if(score>.15){
		//                           result.add(new Document(docId,score,docInfo.getUrl(),snippet)); 
		//                        }
		//			
		//		}


		ArrayList<Document> result = new ArrayList<Document>();    

		if (similarityMeasure.equals("cosine")){
			result = runCosineSimilarity(searchResult, queryIndex);
		} else if (similarityMeasure.equals("jaccard")){
			result = runJaccardSimilarity(searchResult, queryIndex);
		} else if (similarityMeasure.equals("dice")){
			result = runDiceSimilarity(searchResult, queryIndex);
		}

		Collections.sort(result);
		return result;
	}

	private ArrayList<Document> runCosineSimilarity(HashMap<DocInfo, Double> searchResult,HashMap<String,Double> queryIndex){
		double queryVectorLength = 0d;
		for (Double l: queryIndex.values()){
			queryVectorLength += Math.pow(l, 2);
		}
		queryVectorLength = Math.sqrt(queryVectorLength);

		// calculate similarity score
		ArrayList<Document> result = new ArrayList<Document>();
		for (Map.Entry<DocInfo, Double> entry: searchResult.entrySet()){
			DocInfo docInfo = entry.getKey();
			int docId = docInfo.getId();
			double dotProduct = entry.getValue();
			double denominator = Math.sqrt(docInfo.getLength())*queryVectorLength;
			double score = dotProduct/denominator;
			String snippet = docInfo.getSnippet();
			if(score>.50){
				result.add(new Document(docId,score,docInfo.getUrl(),snippet)); 
			}

		}
		return result;
	}

	private ArrayList<Document> runDiceSimilarity(HashMap<DocInfo, Double> searchResult,HashMap<String,Double> queryIndex){
		double queryVectorLength = 0d;
		for (Double l: queryIndex.values()){
			queryVectorLength += Math.pow(l, 2);

		}
		ArrayList<Document> result = new ArrayList<Document>();
		for (Map.Entry<DocInfo, Double> entry: searchResult.entrySet()){
			DocInfo docInfo = entry.getKey();
			int docId = docInfo.getId();
			double dotProduct = 2*(entry.getValue());
			double denominator = docInfo.getLength()*queryVectorLength;
			double score = dotProduct/denominator;
			String snippet = docInfo.getSnippet();
			if(score>.15){
				result.add(new Document(docId,score,docInfo.getUrl(),snippet)); 
			}


		}
		return result;
	}

	private ArrayList<Document> runJaccardSimilarity(HashMap<DocInfo, Double> searchResult,HashMap<String,Double> queryIndex){
		double queryVectorLength = 0d;
		for (Double l: queryIndex.values()){
			queryVectorLength += Math.pow(l, 2);

		}
		ArrayList<Document> result = new ArrayList<Document>();
		for (Map.Entry<DocInfo, Double> entry: searchResult.entrySet()){
			DocInfo docInfo = entry.getKey();
			int docId = docInfo.getId();
			double dotProduct = 2*(entry.getValue());
			double denominator = docInfo.getLength()+queryVectorLength-dotProduct;
			double score = dotProduct/denominator;
			String snippet = docInfo.getSnippet();
			if(score>.15){
				result.add(new Document(docId,score,docInfo.getUrl(),snippet)); 
			}


		}
		return result;
	}


	public ArrayList<Scores> evaluateTestData(InputStream inputStream,InputStream secondinputStream, String similarityMeasure ){
		PatternTokenizer tokenStream = new PatternTokenizer(Pattern.compile("(?s)(?<=\\*FIND\\s).*?(?=\\*FIND|\\*STOP)"),0);
		tokenStream.setReader(new InputStreamReader(inputStream));
		HashMap<Integer,String> queries = new HashMap<Integer,String>();
		try {
			tokenStream.reset();
			CharTermAttribute token = tokenStream.addAttribute(CharTermAttribute.class);
			String[] tokenString;
			String query;
			int queryNum=1;
			while (tokenStream.incrementToken()){
				tokenString = token.toString().split("\\R",2);
				query = tokenString[1];
				query = query.trim();
				if (query.length()>0){
					queries.put(queryNum, query);
					queryNum+=1;
				}
			}
			tokenStream.end();
			tokenStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HashMap<Integer,ArrayList<Integer>> retrievedResults = new HashMap<Integer,ArrayList<Integer>>();
		for (Map.Entry<Integer, String> entry: queries.entrySet()){

			ArrayList<Document> results = generateResults(processQuery(entry.getValue()), similarityMeasure);
			ArrayList<Integer> added= new ArrayList<Integer>();
			for (Document doc: results){
				added.add(doc.getDocId());
			}
			retrievedResults.put(entry.getKey(),added);
		}

		HashMap<Integer,ArrayList<Integer>> relevantResults = getRelevantResults(secondinputStream);
		Evaluation eval = new Evaluation(retrievedResults,relevantResults );
		return eval.calculateScores();
	}



	private HashMap<Integer,ArrayList<Integer>> getRelevantResults(InputStream secondinputStream){

		HashMap<Integer,ArrayList<Integer>> relevantResults=new HashMap<Integer,ArrayList<Integer>>(); 
		BufferedReader br=null;
		try{		 
			br= new BufferedReader(new InputStreamReader(secondinputStream));
			String line=null;
			int number=0;
			while((line=br.readLine())!=null){
				String[] tokens;
				ArrayList <Integer> docs = new ArrayList <Integer>();  
				if (line.trim().length()!=0){
					number+=1;
					tokens=line.split("\\s+");
					for (int i=1; i<tokens.length;i++){
						docs.add(Integer.parseInt(tokens[i]));
					}
					relevantResults.put(number, docs);
				}
			}

		}catch(IOException e){
			e.printStackTrace();
		}
		return relevantResults;
	}


}