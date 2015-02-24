<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="index.css">
<script>
function degreesToRadians(degrees) {
    return (degrees * Math.PI)/180;
}

function draw() {
	var c = document.getElementById("map");
	var ctx = c.getContext("2d");
	var routesDiv = document.getElementById("routes");
	ctx.canvas.width  = routesDiv.clientWidth;
	ctx.canvas.height = routesDiv.clientHeight;
	var routesContDiv = document.getElementById("routes-container");
	routesContDiv.setAttribute("style","height:" + routesDiv.clientHeight + "px;");
	
	alert(localities);
	alert("ff");
	ctx.beginPath();
	ctx.fillStyle = "grey";
	ctx.arc(260,240,10,0,degreesToRadians(360),true);
	ctx.fill();
	ctx.font = "italic 1.2em serif";
	ctx.fillText("Минск", 270, 240);
}
window.onload = draw;
</script>
</head>
<body>
	<div>
		<header>
		<div id="company_name">
			<h1 style="white-space: nowrap"><b>SmartTraveller</b><img src="images/smart_traveller_logo1.png" /></h1>
		</div>
		</header>
		<div id="choose_bar">
			<form name="choose_form" action="" method="get">
				<b>Select start locality:</b> <select id="start_locality">
					<c:forEach var="arrLocalities" items="${localities}">
        				<option value="${arrLocalities[0]}"><c:out value="${arrLocalities[1]}"/></option>
      				</c:forEach>
				</select> 
				<b>Select end locality:</b> <select id="start_locality">
					<c:forEach var="arrLocalities" items="${localities}">
        				<option value="${arrLocalities[0]}"><c:out value="${arrLocalities[1]}"/></option>
      				</c:forEach>
				</select> 
				<input type="submit" value="Find" />
			</form>
			<br>
		</div>
		<div class="flex-container">
			<div class="flex-item"></div>
			<div class="flex-item" id="routes_list">
				<div class="routes-container" id="routes-container">
					<div class="routes-item">one</div>
					<div class="routes-item">two</div>
					<div class="routes-item">three</div>
					<div class="routes-item">four</div>
					<div class="routes-item">one</div>
					<div class="routes-item">two</div>
					<div class="routes-item">three</div>
					<div class="routes-item">four</div>
					<div class="routes-item">one</div>
					<div class="routes-item">two</div>
					<div class="routes-item">three</div>
					<div class="routes-item">four</div>
					<div class="routes-item">one</div>
					<div class="routes-item">two</div>
					<div class="routes-item">three</div>
					<div class="routes-item">four</div>
				</div>
			</div>
			<div class="flex-item" id="routes">
			<canvas id="map" height=100% width=100%>
			</canvas>
			</div>
			<div class="flex-item"></div>
		</div>
		<footer>SmartTraveller - your best choice!</footer>
	</div>
</body>
</html>