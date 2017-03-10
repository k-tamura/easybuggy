<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html>
<head>
<title>EasyBuggy</title>
<c:catch var="ex">
	<c:if test="${param.template != null}">
		<c:import url="<%= request.getParameter(\"template\")%>" /> 
	</c:if>
</c:catch>
</head>
<body style="margin:20px;">
	<header>
	<table width="720">
		<tr>
			<td><img src="../images/easybuggy.png"></td>
			<td><fmt:message key="description.design.page" /></td>
		</tr>
	</table>
	</header>
	<hr>
	<h2>
		<fmt:message key="section.design.test" />
	</h2>
	<p>
		<fmt:message key="description.design.test" />
	</p>
	<ul>
		<li><p>
			<a href=includable.jsp><fmt:message
						key="style.name.nonstyle" /></a>:
			<fmt:message key="style.description.nonstyle" />
		</p></li>
		<li><p>
			<a href=includable.jsp?template=../html/style_bootstrap.html><fmt:message
						key="style.name.bootstrap" /></a>:
			<fmt:message key="style.description.bootstrap" />
		</p></li>
		<li><p>
			<a href=includable.jsp?template=../html/style_google_mdl.html><fmt:message
						key="style.name.google.mdl" /></a>:
			<fmt:message key="style.description.google.mdl" />
		</p></li>
		<li><p>
			<a href=includable.jsp?template=../html/style_materialize.html><fmt:message
						key="style.name.materialize" /></a>:
			<fmt:message key="style.description.materialize" />
		</p></li>
	</ul>
	<p>
		<fmt:message key="msg.note.dangerous.file.inclusion" />
	</p>
	<hr>
	<footer>
	<img src="../images/easybuggyL.png">Copyright © 2016-17 T246 OSS Lab, all rights reserved.
	</footer>
</body>
</html>