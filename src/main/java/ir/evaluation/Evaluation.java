/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aldawsari
 */
public class Evaluation {

	HashMap<Integer,ArrayList<Integer>> actualResults;
	HashMap<Integer,ArrayList<Integer>> testResults;

	public Evaluation (HashMap<Integer,ArrayList<Integer>> testResult, HashMap<Integer,ArrayList<Integer>> actualResult){
		actualResults = actualResult;
		testResults= testResult;
	}


	public ArrayList<Scores>  calculateScores(){
		ArrayList<Scores> scores = new ArrayList<Scores>();
		for( Map.Entry<Integer,ArrayList<Integer>> entry: actualResults.entrySet()){
			ArrayList<Integer> relevantDocs = entry.getValue(); // relevant documents from test data 
			Integer queryId= entry.getKey();
			ArrayList<Integer> retrievedDocs = testResults.get(queryId);  // retrieved documents from out system
			int numDocRetrieved = retrievedDocs.size();
			int numDocActual = relevantDocs.size();
			int precisionCount=0;
			int recallCount=0;
			for(Integer docId : relevantDocs){
				for (Integer i: retrievedDocs){
					if(i.equals(docId)) {	// relevant document
						precisionCount++;
						recallCount++;
					}
				}
			}
			double recallScore = 0;
			double precisionScore = 0;
			double fmeasure = 0;
			if (retrievedDocs.size()!=0){            
				precisionScore= (double)precisionCount/(double)retrievedDocs.size(); // compute precision
				recallScore = (double)recallCount/(double)relevantDocs.size();  // compute recall
				if (precisionScore>0 && recallScore>0){
					fmeasure = 2*((precisionScore*recallScore)/(precisionScore+recallScore)); // compute f measure
				} else {
					fmeasure=0;
				}
			}
			Scores score = new Scores(queryId, numDocRetrieved, numDocActual);
			score.setPrecision(precisionScore);
			score.setRecall(recallScore);
			score.setFmeasure(fmeasure);
			scores.add(score);        
		}
		return scores;
	}    

}
