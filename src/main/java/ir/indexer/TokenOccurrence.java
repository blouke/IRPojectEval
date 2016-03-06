package ir.indexer;

import java.io.Serializable;

public class TokenOccurrence implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int docId;
	private int count;
	
	public TokenOccurrence(int docId,int count){
		this.docId = docId;
		this.count=count;
	}
	
	public int getDocId(){
		return docId;
	}
	
	public int getCount(){
		return count;
	}
	
	public void addTokenOcc(int count){
		this.count+=count;
	}
}
