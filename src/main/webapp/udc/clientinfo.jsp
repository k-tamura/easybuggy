<!DOCTYPE HTML>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>EasyBuggy</title>
<c:import url="/dfi/style_bootstrap.html" /> 
</head>
<body style="margin:20px;">
	<ul>
		<li><p>User-Agent: <%=request.getHeader("user-agent")%></p></li>
		<li><p>Accept-Language: <%=request.getHeader("Accept-Language")%></p></li>
	</ul>
</body>
</html>