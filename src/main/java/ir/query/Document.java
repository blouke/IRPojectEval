package ir.query;

public class Document implements Comparable<Document>{
	private int docId;
	private String url;
	private String snippet;
	private double score;
	
	public Document(int docId, double score, String url, String snippet){
		this.docId = docId;
		this.score = score;
		this.url = url;
		this.snippet = snippet;
	}

	public int getDocId(){
		return docId;
	}
	
	public String getUrl() {
		return url;
	}

	public String getSnippet() {
		return snippet;
	}
	
	public double getScore(){
		return score;
	}

	@Override
	public int compareTo(Document d) {
		// TODO Auto-generated method stub
		return (this.score>d.score)?-1:(this.score<d.score)?1:0;
	}
	
	
}
