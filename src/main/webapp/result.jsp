<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


</head>
<body>
	<h2>Evaluation of system over test data: 423 Time Magazine Articles (from 1963)</h2> 
	<table>
		<colgroup>
			<col width="100">
			<col width="100">
			<col width="100">
			<col width="100">
			<col width="150">
			<col width="150">
		</colgroup>
		<tr>
			<th>Query Number</th>
			<th>Precision</th>
			<th>Recall</th>
			<th>F Measure</th>
			<th>Our system retrieved documents</th>
			<th>Number of documents (test data judgments)</th>
		</tr>
		<c:choose>
			<c:when test="${fn:length(results)>0}">
				<c:forEach var="result" items="${results}">
					<tr>
						<td align="center"><c:out value="${result.queryNum}" /></td>
						<td align="center"><fmt:formatNumber type="number" value="${result.precision}" maxFractionDigits="3" /></td>
						<td align="center"><fmt:formatNumber type="number" value="${result.recall}" maxFractionDigits="3"/></td>
						<td align="center"><fmt:formatNumber type="number" value="${result.fmeasure}" maxFractionDigits="3"/></td>
						<td align="center"><c:out value="${result.numDocRetrieved}" /></td>
						<td align="center"><c:out value="${result.numDocActual}" /></td>
                                               
				</c:forEach>
				<h3>Mean Absoulte Error</h3>
				<div>Cosine similarity :<fmt:formatNumber type="number" value="${mae_cosine}" maxFractionDigits="3"/></div>
				<div>Dice similarity :<fmt:formatNumber type="number" value="${mae_dice}" maxFractionDigits="3"/></div>
				<div>Jaccard similarity :<fmt:formatNumber type="number" value="${mae_jaccard}" maxFractionDigits="3"/></div>
				<div><h3>Precision, Recall and F1 measure of test data queries using COSINE similarity.</h3></div>
			</c:when>
			<c:otherwise>
				<p>No relevant documents found.</p>
			</c:otherwise>
		</c:choose>
	</table>

</body>
</html>