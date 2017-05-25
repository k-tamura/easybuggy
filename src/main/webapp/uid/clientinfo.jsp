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
<link rel="icon" type="image/vnd.microsoft.icon" href="/images/favicon.ico">
<c:import url="/dfi/style_bootstrap.html" />
</head>
<body style="margin-left: 20px; margin-right: 20px;">
	<table style="width: 100%;">
		<tr>
			<td>
				<h2>
					<span class="glyphicon glyphicon-globe"></span>&nbsp;
					<fmt:message key="section.client.info" />
				</h2>
			</td>
			<td align="right"><a href="/"><fmt:message key="label.go.to.main" /></a>
			</td>
		</tr>
	</table>
	<hr style="margin-top: 0px" />
	<ul>
		<li><p>
				User-Agent:
				<%=request.getHeader("user-agent")%></p></li>
		<li><p>
				Accept-Language:
				<%=request.getHeader("Accept-Language")%></p></li>
	</ul>
	<hr />
	<div class="alert alert-info" role="alert">
		<span class="glyphicon glyphicon-info-sign"></span>&nbsp;
		<fmt:message key="msg.note.unintended.file.disclosure" />
	</div>
</body>
</html>