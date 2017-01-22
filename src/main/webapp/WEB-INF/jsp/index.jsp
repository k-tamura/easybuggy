<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="indexpage" />
<!DOCTYPE html>
<html>
<head>
<title>EasyBuggy</title>
</head>
<body>

	<h1>
		<fmt:message key="section.troubles" />
	</h1>
	<p>
		<fmt:message key="description.troubles" />
	</p>
	<ul>
		<li><p>
			<a href="memoryleak" target="_blank"><fmt:message
						key="function.name.memory.leak" /></a>:
			<fmt:message key="function.description.memory.leak" />
		</p></li>
		<li><p>
			<a href="memoryleak2" target="_blank"><fmt:message
						key="function.name.memory.leak2" /></a>:
			<fmt:message key="function.description.memory.leak2" />
		</p></li>
		<li><p>
			<a href="memoryleak3" target="_blank"><fmt:message
						key="function.name.memory.leak3" /></a>:
			<fmt:message key="function.description.memory.leak3" />
		</p></li>
		<li><p>
			<a href="deadlock" target="_blank"><fmt:message
						key="function.name.dead.lock" /></a>:
			<fmt:message key="function.description.dead.lock" />
		</p></li>
		<li><p>
			<a href="deadlock2" target="_blank"><fmt:message
						key="function.name.dead.lock2" /></a>:
			<fmt:message key="function.description.dead.lock2" />
		</p></li>
		<li><p>
			<a href="endlesswaiting" target="_blank"><fmt:message
						key="function.name.endless.waiting.process" /></a>:
			<fmt:message key="function.description.endless.waiting.process" />
		</p></li>
		<li><p>
			<a href="infiniteloop" target="_blank"><fmt:message
						key="function.name.infinite.loop" /></a>:
			<fmt:message key="function.description.infinite.loop" /></li>
		<li><p>
			<a href="redirectloop" target="_blank"><fmt:message
						key="function.name.redirect.loop" /></a>:
			<fmt:message key="function.description.redirect.loop" />
		</p></li>
		<li><p>
			<a href="jvmcrasheav" target="_blank"><fmt:message
						key="function.name.jvm.crash.eav" /> </a>:
			<fmt:message key="function.description.jvm.crash.eav" />
		</p></li>
		<li><p>
			<a href="netsocketleak" target="_blank"><fmt:message
						key="function.name.network.socket.leak" /></a>:
			<fmt:message key="function.description.network.socket.leak" />
		</p></li>
		<li><p>
			<a href="dbconnectionleak " target="_blank"><fmt:message
						key="function.name.database.connection.leak" /></a> :
			<fmt:message key="function.description.database.connection.leak" />
		</p></li>
		<li><p>
			<a href="filedescriptorleak " target="_blank"><fmt:message
						key="function.name.file.descriptor.leak" /></a> :
			<fmt:message key="function.description.file.descriptor.leak" />
		</p></li>
	</ul>			

	<h1>
		<fmt:message key="section.performance.issue" />
	</h1>
	<p>
		<fmt:message key="description.performance.issue" />
	</p>
	<ul>
		<li><p>
			<fmt:message key="function.name.stop.the.world" />
				:
			<fmt:message key="function.description.stop.the.world" />
		</p></li>
	</ul>

	<h1>
		<fmt:message key="section.vulnerabilities" />
	</h1>
	<p>
		<fmt:message key="description.vulnerabilities" />
	</p>
	<ul>
		<li><p>
			<a href="xss" target="_blank"><fmt:message
						key="function.name.xss" /></a>:
			<fmt:message key="function.description.xss" />
		</p></li>
		<li><p>
			<a href="sqlijc" target="_blank"><fmt:message
						key="function.name.sql.injection" /></a>:
			<fmt:message key="function.description.sql.injection" />
		</p></li>
		<li><p>
			<fmt:message key="function.name.ldap.injection" />
				:
			<fmt:message key="function.description.ldap.injection" />
		</p></li>
	</ul>

	<h1>
		<fmt:message key="section.errors" />
	</h1>
	<p>
		<fmt:message key="description.errors" />
	</p>
	<ul>
		<li><p>
			<a href="eie" target="_blank"><fmt:message
						key="function.name.ei.error" /></a>:
			<fmt:message key="function.description.ei.error" />
		</p></li>
		<li><p>
			<a href="oome" target="_blank"><fmt:message
						key="function.name.oome.error" /> </a>:
			<fmt:message key="function.description.oome.error" />
		</p></li>
		<li><p>
			<a href="oome2" target="_blank"><fmt:message
						key="function.name.oome.error2" /></a>:
			<fmt:message key="function.description.oome.error2" />
		</p></li>
		<li><p>
			<a href="oome3" target="_blank"><fmt:message
						key="function.name.oome.error3" /></a>:
			<fmt:message key="function.description.oome.error3" />
		</p></li>
		<li><p>
			<a href="oome4" target="_blank"><fmt:message
						key="function.name.oome.error4" /></a>:
			<fmt:message key="function.description.oome.error4" />
		</p></li>
		<li><p>
			<a href="oome5" target="_blank"><fmt:message
						key="function.name.oome.error5" /></a>:
			<fmt:message key="function.description.oome.error5" />
		</p></li>
		<li><p>
			<a href="oome6" target="_blank"><fmt:message
						key="function.name.oome.error6" /></a>:
			<fmt:message key="function.description.oome.error6" />
		</p></li>
		<li><p>
			<a href="sofe" target="_blank"><fmt:message
						key="function.name.sof.error" /></a>:
			<fmt:message key="function.description.sof.error" />
		</p></li>
		<li><p>
			<a href="jnicall" target="_blank"><fmt:message
						key="function.name.ul.error" /></a>:
			<fmt:message key="function.description.ul.error" />
		</p></li>
	</ul>

	<h1>
		<fmt:message key="section.others" />
	</h1>
	<p>
		<fmt:message key="description.others" />
	</p>
	<ul>
		<li><p>
			<a href="iof" target="_blank"><fmt:message
						key="function.name.int.overflow" /></a>:
			<fmt:message key="function.description.int.overflow" />
		</p></li>
		<li><p>
			<a href="roe" target="_blank"><fmt:message
						key="function.name.round.off.error" /></a>:
			<fmt:message key="function.description.round.off.error" />
		</p></li>
		<li><p>
				<fmt:message
						key="function.name.truncation.error" />:
			<fmt:message key="function.description.truncation.error" />
		</p></li>
		<li><p>
				<fmt:message
						key="function.name.cancellation.of.significant.digits" />:
			<fmt:message key="function.description.cancellation.of.significant.digits" />
		</p></li>
		<li><p>
			<a href="lotd" target="_blank"><fmt:message
						key="function.name.loss.of.trailing.digits" /></a>:
			<fmt:message key="function.description.loss.of.trailing.digits" />
		</p></li>
	</ul>

	<h1>
		<fmt:message key="section.exceptions" />
	</h1>
	<p>
		<fmt:message key="description.section.exceptions" />
	</p>
	<ul>
		<li><p>
			<a href="ae" target="_blank">ArithmeticException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="ArithmeticException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="aioobe" target="_blank">ArrayIndexOutOfBoundsException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="ArrayIndexOutOfBoundsException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ase" target="_blank">ArrayStoreException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="ArrayStoreException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cce" target="_blank">ClassCastException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="ClassCastException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cme" target="_blank">ConcurrentModificationException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="ConcurrentModificationException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="fnfe" target="_blank">FileNotFoundException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="FileNotFoundException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="itse" target="_blank">IllegalThreadStateException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="IllegalThreadStateException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ioobe" target="_blank">IndexOutOfBoundsException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="IndexOutOfBoundsException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="murle" target="_blank">	MalformedURLException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="	MalformedURLException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="npe" target="_blank">NullPointerException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="NullPointerException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="nfe" target="_blank">NumberFormatException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="NumberFormatException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="uhe" target="_blank">UnknownHostException</a>:
			<fmt:message key="function.description.exception"><fmt:param value="UnknownHostException"/></fmt:message>
		</p></li>
	</ul>

</body>
</html>