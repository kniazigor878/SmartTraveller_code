<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="locals" class="by.iharkaratkou.beans.Localities"
		scope="application">
	</jsp:useBean>
	<jsp:getProperty name="locals" property="locValues"></jsp:getProperty>
	<jsp:getProperty name="locals" property="testString"></jsp:getProperty>
</body>
</html>