<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="index.css">
<script>
	function degreesToRadians(degrees) {
		return (degrees * Math.PI) / 180;
	}
	
	function drawCities(ctx,canvasWidth,canvasHeight){
		var maxLocRarious = 10;
		
 		<c:forEach var="item" items="${localities}">
			var locName = '${item[1]}';
			var locWidth = '${item[2]}';
			var locHeigth = '${item[3]}';
			var locRadius = '${item[4]}';
			console.log(canvasWidth*locWidth + " " + canvasHeight*locHeigth + " " + maxLocRarious/locRadius + " " + locRadius);
			ctx.beginPath();
			ctx.fillStyle = "grey";
			ctx.arc(canvasWidth*locWidth, canvasHeight*locHeigth, maxLocRarious/locRadius, 0, degreesToRadians(360), true);
			ctx.fill();
			ctx.font = "italic " + 2/locRadius +"em serif";
			ctx.fillText(locName, canvasWidth*locWidth + 10, canvasHeight*locHeigth - 7);
 		</c:forEach>
		return;
	}

	function draw() {
		var c = document.getElementById("map");
		var ctx = c.getContext("2d");
		var routesDiv = document.getElementById("routes");
		var canvasWidth = routesDiv.clientWidth;
		var canvasHeight = routesDiv.clientHeight;
		ctx.canvas.width = canvasWidth;
		ctx.canvas.height = canvasHeight;
		var routesContDiv = document.getElementById("routes-container");
		routesContDiv.setAttribute("style", "height:" + routesDiv.clientHeight + "px;");
		drawCities(ctx,canvasWidth,canvasHeight);
	}
	window.onload = draw;
</script>
</head>
<body>
	<div>
		<header>
		<div id="company_name">
			<h1 style="white-space: nowrap">
				<b>SmartTraveller</b><img src="images/smart_traveller_logo1.png" />
			</h1>
		</div>
		</header>
		<div id="choose_bar">
			<c:set var="localitiesApp" value="${localities}" scope="application" />
			<form name="choose_form" action="MainController" method="post">
				<b>Select start locality:</b> <select name="start_locality">
					<c:forEach var="arrLocalitiesStrart" items="${localitiesApp}">
						<c:choose>
							<c:when test = "${arrLocalitiesStrart[0] == start_locality }">
							<option value="${arrLocalitiesStrart[0]}" selected ="selected"><c:out
								value="${arrLocalitiesStrart[1]}" /></option>
							</c:when>	
							<c:otherwise>
							<option value="${arrLocalitiesStrart[0]}" ><c:out
								value="${arrLocalitiesStrart[1]}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select> <b>Select end locality:</b> <select name="end_locality">
					<c:forEach var="arrLocalitiesEnd" items="${localitiesApp}">
						<c:choose>
							<c:when test = "${arrLocalitiesEnd[0] == end_locality }">
							<option value="${arrLocalitiesEnd[0]}" selected ="selected"><c:out
								value="${arrLocalitiesEnd[1]}" /></option>
							</c:when>	
							<c:otherwise>
							<option value="${arrLocalitiesEnd[0]}" ><c:out
								value="${arrLocalitiesEnd[1]}" /></option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select> <input type="submit" value="Find" />
			</form>
			<br>
		</div>
		<div class="flex-container">
			<div class="flex-item"></div>
			<div class="flex-item" id="routes_list">
				<div class="routes-container" id="routes-container">
					<div class="routes-item" id="headItem"><b>Proposed routes.<br>To see the graphic of route click necessary tab below:</b></div>
					<c:forEach var="arrFinalRoutesForView" items="${finalRoutesForView}">
						<div class="routes-item">${arrFinalRoutesForView}</div>
					</c:forEach>
				</div>
			</div>
			<div class="flex-item" id="routes">
				<canvas id="map" height=100% width=100%> </canvas>
			</div>
			<div class="flex-item"></div>
		</div>
		<footer>SmartTraveller - your best choice!</footer>
	</div>
</body>
</html>