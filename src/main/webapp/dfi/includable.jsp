<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	<c:if test="${param.template != null && !fn:contains(param.template,'../') && !fn:startsWith(param.template,'/')}">
		<c:import url="<%= request.getParameter(\"template\")%>" /> 
	</c:if>
</c:catch>
</head>
<body style="margin:20px;">
	<header>
	<table width="720">
		<tr>
			<td><img src="/images/easybuggy.png"></td>
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
			<a href="includable.jsp"><fmt:message
						key="style.name.nonstyle" /></a>:
			<fmt:message key="style.description.nonstyle" />
		</p></li>
		<li><p>
			<a href="includable.jsp?template=style_bootstrap.html"><fmt:message
						key="style.name.bootstrap" /></a>:
			<fmt:message key="style.description.bootstrap" />
		</p></li>
		<li><p>
			<a href="includable.jsp?template=style_google_mdl.html"><fmt:message
						key="style.name.google.mdl" /></a>:
			<fmt:message key="style.description.google.mdl" />
		</p></li>
		<li><p>
			<a href="includable.jsp?template=style_materialize.html"><fmt:message
						key="style.name.materialize" /></a>:
			<fmt:message key="style.description.materialize" />
		</p></li>
	</ul>
	<div class="alert alert-info" role="alert">
		<span class="glyphicon glyphicon-info-sign"></span>&nbsp;
		<fmt:message key="msg.note.dangerous.file.inclusion" />
	</div>
	<hr>
	<footer>
	<img src="/images/easybuggyL.png">Copyright &copy; 2016-17 T246 OSS Lab, all rights reserved.
	</footer>
</body>
</html>