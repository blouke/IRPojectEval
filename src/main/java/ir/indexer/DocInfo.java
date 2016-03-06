package ir.indexer;

import java.io.Serializable;

public class DocInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String url;
	private double length;
	private String snippet;
	
	public DocInfo(int id, String url, String snippet) {
		this.id = id;
		this.url = url;
		this.snippet = snippet;
		length = 0.0;
	}
	
	public int getId(){
		return id;
	}
	
	public void setLength(double length){
		this.length = length;
	}
	
	public double getLength(){
		return length;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getSnippet(){
		return snippet;
	}
}
