<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${path}/resources/styles/popup.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <title>popup</title>
</head>
<body onload="initMessage('${path}')">
    <div class="mainframe">
        <div class="title">
            	ポップアップ
        </div>
        <div class="message">
          	 <c:if test="${resultMessage ne null}">${resultMessage}</c:if>
          	 <c:if test="${sessionScope.gameResultMessage ne null}">${sessionScope.gameResultMessage}</c:if>
        </div>
        <div class="point">
        	<c:if test="${sessionScope.user_point ne null}">
        		獲得ポイント : ${sessionScope.result_point}
        	</c:if>
        </div>
        <a href="${path}/game" class="btn btn-secondary">OK</a>
    </div>
</body>
<script>
var messages = [];

function initMessage(path)
{
  	fetch(path + '/api/messages')
  	 .then(response => response.json())
  	 .then(data => 
  	 {
  	    messages[0] = data["90"].messages["201"]; //"挑戦に成功しました。"
  	  	messages[1] = data["90"].messages["202"]; //"挑戦に失敗しました。"
  		messages[2] = data["90"].messages["203"]; //"ポイント : "
  		
  	 }).catch(error => console.error("Error fetching messages:", error));
}
</script>
</html>