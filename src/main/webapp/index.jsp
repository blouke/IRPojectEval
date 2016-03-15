
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<title>IRProject</title>
</head>

<body>
<!-- ${pageContext.request.contextPath} -->
	<h1>Test Data Evaluation (Time magazine collection)</h1>
	<form action="${pageContext.request.contextPath}/evaluate" method="post">
		
			
			<button type="submit" id="submit">Evaluate</button>
	</form>

</body>
</html>
