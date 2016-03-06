/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.evaluation;

/**
 *
 * @author aldawsari
 */
public class Scores {
	int queryNum;
	double precision=0;
	double recall=0;
	double fmeasure=0;
	int numDocRetrieved;
	int numDocActual;

	public Scores(int queryNum, int numDocRetrieved, int numDocActual){
		this.queryNum = queryNum;
		this.numDocRetrieved = numDocRetrieved;
		this.numDocActual = numDocActual;
	}
	
	public int getNumDocActual(){
		return numDocActual;
	}
	
	public int getNumDocRetrieved(){
		return numDocRetrieved;
	}
	
	public int getQueryNum(){
		return queryNum;
	}
	
	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getFmeasure() {
		return fmeasure;
	}

	public void setFmeasure(double fmeasure) {
		this.fmeasure = fmeasure;
	}
}
