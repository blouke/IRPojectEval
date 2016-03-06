package ir.indexer;

import java.io.Serializable;
import java.util.HashMap;

public class TokenInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private double idf;
	private HashMap<Integer,TokenOccurrence> occMap;
	
	public TokenInfo() {
		this.idf = 0.0;
		this.occMap = new HashMap<Integer,TokenOccurrence>();
	}
	
	public void calculateIdf(int totalDoc){
		idf = Math.log(totalDoc/occMap.size())/Math.log(totalDoc);
//		System.out.println("Document frequency: "+occMap.size());		//remove this line
	}
	
	public double getIdf(){
		return idf;
	}
	
	public void addTokenOccurrence(int docId, int count){
		if (occMap.containsKey(docId)){
			TokenOccurrence tokenOccc = occMap.get(docId);
			tokenOccc.addTokenOcc(count);
		} else {
			occMap.put(docId, new TokenOccurrence(docId,count));
		}
	}
	
	public HashMap<Integer,TokenOccurrence> getOccMap(){
		return occMap;
	}
}
