
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<title>IRProject</title>
</head>

<body>
<!-- ${pageContext.request.contextPath} -->
	<h1>Search</h1>
	<form action="${pageContext.request.contextPath}/evaluate" method="post">
		
			
			<button type="submit" id="submit">Evaluate Test Data (Time magazine collection)</button>
	</form>

</body>
</html>
