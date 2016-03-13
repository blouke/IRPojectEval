package ir.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ir.evaluation.Scores;
import ir.indexer.TermIndexer;
import ir.query.Document;
import ir.query.QueryProcessor;

/**
 * Servlet implementation class SearchController
 */
//@WebServlet("/search")
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static TermIndexer index;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchController() {
		super();

		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		ServletContext context = config.getServletContext();

		index = new TermIndexer(context.getResourceAsStream("/WEB-INF/classes/testData"));
		index.initialize();

		String jwnlPropPath = context.getInitParameter("jwnl.properties");
		try {
			URL jwnlPropURL = context.getResource(jwnlPropPath);
			String jwnlProp = Paths.get(jwnlPropURL.toURI()).toString();
			System.setProperty("jwnlProp", jwnlProp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		QueryProcessor queryProcessor = new QueryProcessor(index);
		
		ArrayList<Scores> evalResultsCosine = queryProcessor.evaluateTestData(context.getResourceAsStream("/WEB-INF/classes/testQueries"),context.getResourceAsStream("/WEB-INF/classes/actualJudgements"), "cosine");
		Double mae_cosine = 0d;
		for (Scores score: evalResultsCosine){
			mae_cosine += Math.abs(score.getNumDocActual() - score.getNumDocRetrieved());
		}
		mae_cosine /= evalResultsCosine.size();
		request.setAttribute("mae_cosine", mae_cosine);
		
		
		ArrayList<Scores> evalResultsJaccard = queryProcessor.evaluateTestData(context.getResourceAsStream("/WEB-INF/classes/testQueries"),context.getResourceAsStream("/WEB-INF/classes/actualJudgements"), "jaccard");
		Double mae_jaccard = 0d;
		for (Scores score: evalResultsJaccard){
			mae_jaccard += Math.abs(score.getNumDocActual() - score.getNumDocRetrieved());
		}
		mae_jaccard /= evalResultsJaccard.size();
		request.setAttribute("mae_jaccard", mae_jaccard);
		
		
		ArrayList<Scores> evalResultsDice = queryProcessor.evaluateTestData(context.getResourceAsStream("/WEB-INF/classes/testQueries"),context.getResourceAsStream("/WEB-INF/classes/actualJudgements"), "dice");
		Double mae_dice = 0d;
		for (Scores score: evalResultsDice){
			mae_dice += Math.abs(score.getNumDocActual() - score.getNumDocRetrieved());
		}
		mae_dice /= evalResultsDice.size();
		request.setAttribute("mae_dice", mae_dice);
		
		
		request.setAttribute("results", evalResultsCosine);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/result.jsp");
		dispatcher.include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}