package ir.indexer;

import java.io.Serializable;
import java.util.HashMap;

public class TermOccurrence implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String,Double> termOccMap;
	
	public TermOccurrence(String term){
		this.termOccMap = new HashMap<String,Double>();
		termOccMap.put(term, 1d);
	}
	
	public HashMap<String,Double> getTermOccMap(){
		return termOccMap;
	}
	public void addTermOcc(String term){
		if (termOccMap.containsKey(term)){
			Double count = termOccMap.get(term);
			termOccMap.put(term, count+1);
		} else {
			termOccMap.put(term, 1d);
		}
	}
}
