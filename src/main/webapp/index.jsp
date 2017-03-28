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
<c:import url="/dfi/style_bootstrap.html" /> 
</head>
<body style="margin:20px;">
	<header>
	<table width="720">
		<tr>
			<td><img src="images/easybuggy.png"></td>
			<td><fmt:message key="description.all" /></td>
		</tr>
	</table>
	</header>
	<hr>
	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.troubles" />
	</h2>
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
			<a href="forwardloop" target="_blank"><fmt:message
						key="function.name.forward.loop" /></a>:
			<fmt:message key="function.description.forward.loop" />
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
		<li><p>
			<a href="threadleak" target="_blank"><fmt:message
						key="function.name.thread.leak" /></a>:
			<fmt:message key="function.description.thread.leak" />
		</p></li>
		<li><p>
			<a href="mojibake" target="_blank"><fmt:message
						key="function.name.mojibake" /></a>:
			<fmt:message key="function.description.mojibake" />
		</p></li>
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
				<a href="te" target="_blank"><fmt:message
						key="function.name.truncation.error" /></a>:
			<fmt:message key="function.description.truncation.error" />
		</p></li>
		<li><p>
			<a href="lotd" target="_blank"><fmt:message
						key="function.name.loss.of.trailing.digits" /></a>:
			<fmt:message key="function.description.loss.of.trailing.digits" />
		</p></li>
	</ul>

	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.vulnerabilities" />
	</h2>
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
			<a href="ldapijc" target="_blank"><fmt:message
						key="function.name.ldap.injection" /></a>:
			<fmt:message key="function.description.ldap.injection" />
		</p></li>
		<li><p>
			<a href="codeijc" target="_blank"><fmt:message
						key="function.name.code.injection" /></a>:
			<fmt:message key="function.description.code.injection" />
		</p></li>
 		<li><p>
			<a href="ognleijc" target="_blank"><fmt:message
						key="function.name.os.command.injection" /></a>:
			<fmt:message key="function.description.os.command.injection" />
		</p></li>
 		<li><p>
			<a href="mailheaderijct" target="_blank"><fmt:message
						key="function.name.mail.header.injection" /></a>:
			<fmt:message key="function.description.mail.header.injection" />
		</p></li>
		<li><p>
			<a href="ursupload" target="_blank"><fmt:message
						key="function.name.unrestricted.size.upload" /></a>:
			<fmt:message key="function.description.unrestricted.size.upload" />
		</p></li>
		<li><p>
			<a href="ureupload" target="_blank"><fmt:message
						key="function.name.unrestricted.ext.upload" /></a>:
			<fmt:message key="function.description.unrestricted.ext.upload" />
		</p></li>
		<li><p>
			<a href="admins/main?logintype=openredirect" target="_blank"><fmt:message
						key="function.name.open.redirect" /></a>:
			<fmt:message key="function.description.open.redirect" />
		</p></li>
		<li><p>
			<a href="admins/main?logintype=bruteforce" target="_blank"><fmt:message
						key="function.name.brute.force" /></a>:
			<fmt:message key="function.description.brute.force" />
		</p></li>
		<li><p>
			<a href="/verbosemsg/login" target="_blank"><fmt:message
						key="function.name.verbose.error.message" /></a>:
			<fmt:message key="function.description.verbose.error.message" />
		</p></li>
		<li><p>
			<a href="/dfi/includable.jsp?template=style_bootstrap.html" target="_blank"><fmt:message
						key="function.name.dangerous.file.inclusion" /></a>:
			<fmt:message key="function.description.dangerous.file.inclusion" />
		</p></li>
		<li><p>
			<a href="/dt/includable.jsp?template=basic" target="_blank"><fmt:message
						key="function.name.directory.traversal" /></a>:
			<fmt:message key="function.description.directory.traversal" />
		</p></li>
		<li><p>
			<a href="/uid/clientinfo.jsp" target="_blank"><fmt:message
						key="function.name.unintended.file.disclosure" /></a>:
			<fmt:message key="function.description.unintended.file.disclosure" />
		</p></li>
		<li><p>
			<a href="/admins/csrf" target="_blank"><fmt:message
						key="function.name.csrf" /></a>:
			<fmt:message key="function.description.csrf" />
		</p></li>
		<li><p>
			<a href="/admins/clickjacking" target="_blank"><fmt:message
						key="function.name.clickjacking" /></a>:
			<fmt:message key="function.description.clickjacking" />
		</p></li>
	</ul>

	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.performance.issue" />
	</h2>
	<p>
		<fmt:message key="description.performance.issue" />
	</p>
	<ul>
		<li><p>
			<a href="slowre" target="_blank"><fmt:message
						key="function.name.slow.regular.expression" /></a>:
			<fmt:message key="function.description.slow.regular.expression" />
		</p></li>
		<li><p>
			<a href="strplusopr" target="_blank"><fmt:message
						key="function.name.slow.string.plus.operation" /></a>:
			<fmt:message key="function.description.slow.string.plus.operation" />
		</p></li>
		<!-- <li><p>
			<fmt:message key="function.name.stop.the.world" />
				:
			<fmt:message key="function.description.stop.the.world" />
		</p></li> -->
	</ul>

	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.errors" />
	</h2>
	<p>
		<fmt:message key="description.errors" />
	</p>
	<ul>
		<li><p>
			<a href="fce" target="_blank">FactoryConfigurationError</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="FactoryConfigurationError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="eie" target="_blank"><fmt:message
						key="function.name.ei.error" /></a>:
			<fmt:message key="function.description.ei.error" />
		</p></li>
		<li><p>
			<a href="oome" target="_blank">OutOfMemoryError (Java heap space)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="oome2" target="_blank">OutOfMemoryError (Requested array size exceeds VM limit)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="oome3" target="_blank">OutOfMemoryError (unable to create new native thread)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="oome4" target="_blank">OutOfMemoryError (GC overhead limit exceeded)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="oome5" target="_blank">OutOfMemoryError (PermGen space)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="oome6" target="_blank">OutOfMemoryError (Direct buffer memory)</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="OutOfMemoryError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="sofe" target="_blank">StackOverflowError</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="StackOverflowError"/></fmt:message>
		</p></li>
		<li><p>
			<a href="jnicall" target="_blank">UnsatisfiedLinkError</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="UnsatisfiedLinkError"/></fmt:message>
		</p></li>
	</ul>

	<h2>
		<span class="glyphicon glyphicon-knight"></span>&nbsp;
		<fmt:message key="section.exceptions" />
	</h2>
	<p>
		<fmt:message key="description.section.exceptions" />
	</p>
	<ul>
		<li><p>
			<a href="ae" target="_blank">ArithmeticException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ArithmeticException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="aioobe" target="_blank">ArrayIndexOutOfBoundsException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ArrayIndexOutOfBoundsException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ase" target="_blank">ArrayStoreException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ArrayStoreException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="boe" target="_blank">BufferOverflowException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="BufferOverflowException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="bue" target="_blank">BufferUnderflowException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="BufferUnderflowException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cre" target="_blank">CannotRedoException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="CannotRedoException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cue" target="_blank">CannotUndoException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="CannotUndoException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cce" target="_blank">ClassCastException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ClassCastException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="cme" target="_blank">ConcurrentModificationException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ConcurrentModificationException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ese" target="_blank">EmptyStackException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="EmptyStackException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="iae" target="_blank">IllegalArgumentException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IllegalArgumentException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="imse" target="_blank">IllegalMonitorStateException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IllegalMonitorStateException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ipse" target="_blank">IllegalPathStateException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IllegalPathStateException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="iase" target="_blank">IllegalStateException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IllegalStateException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="itse" target="_blank">IllegalThreadStateException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IllegalThreadStateException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="imoe" target="_blank">ImagingOpException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="ImagingOpException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ioobe" target="_blank">IndexOutOfBoundsException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="IndexOutOfBoundsException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="ime" target="_blank">InputMismatchException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="InputMismatchException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="mpte" target="_blank">MalformedParameterizedTypeException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="MalformedParameterizedTypeException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="mre" target="_blank">MissingResourceException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="MissingResourceException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="nase" target="_blank">NegativeArraySizeException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="NegativeArraySizeException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="nsee" target="_blank">NoSuchElementException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="NoSuchElementException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="npe" target="_blank">NullPointerException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="NullPointerException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="nfe" target="_blank">NumberFormatException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="NumberFormatException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="se" target="_blank">SecurityException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="SecurityException"/></fmt:message>
		</p></li>
		<li><p>
			<a href="uoe" target="_blank">UnsupportedOperationException</a>:
			<fmt:message key="function.description.throwable"><fmt:param value="UnsupportedOperationException"/></fmt:message>
		</p></li>
	</ul>

	<hr>
	<footer>
	<img src="images/easybuggyL.png">Copyright © 2016-17 T246 OSS Lab, all rights reserved.
	</footer>
</body>
</html>