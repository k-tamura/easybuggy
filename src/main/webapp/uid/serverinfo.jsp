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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title><fmt:message key="section.server.info" /></title>
<c:import url="/dfi/style_bootstrap.html" /> 
</head>
<body style="margin:20px;">
<table style="width:760px;">
<tr><td>
<h2>
<span class="glyphicon glyphicon-globe"></span>&nbsp;<fmt:message key="section.server.info" />
</h2>
</td><td align="right">
<fmt:message key="label.login.user.id" />: <%=session.getAttribute("userid")%>
<br>
<a href="/logout"><fmt:message key="label.logout" /></a>
</td></tr>
</table>
<hr/>
<% request.setAttribute("systemProperties", java.lang.System.getProperties()); %>
 <table width="760">
  <tr>
   <th>Key</th>
   <th>Value</th>
  </tr>
  <c:forEach var="entry" items="${systemProperties}">
   <tr>
    <td>
     <c:out value="${entry.key}" />
    </td>
    <td>
     <c:out value="${entry.value}" />
    </td>
   </tr>
  </c:forEach>
 </table>
</body>
</html>