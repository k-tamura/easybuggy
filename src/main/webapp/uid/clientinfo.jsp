<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE HTML>
<html>
<head>
<title><fmt:message key="section.client.info" /></title>
<c:import url="/dfi/style_bootstrap.html" /> 
</head>
<body style="margin:20px;">
	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.client.info" />
	</h2>
    <hr/>
	<ul>
		<li><p>User-Agent: <%=request.getHeader("user-agent")%></p></li>
		<li><p>Accept-Language: <%=request.getHeader("Accept-Language")%></p></li>
	</ul>
	<hr/>
	<p><fmt:message key="msg.note.unintended.file.disclosure" /></p>
</body>
</html>