<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="${path}/resources/styles/login.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <title>会員認証</title>
  </head>
  <body onload="initMessage('${path}')">
    <div class="mainframe">
        <div class="title">
       		 ログイン
      	</div>
        <div class="loginInfo">
          <form name="frmLogin" action="${path}/signin" method="post" onsubmit="return checkForm()">
            <label for="inputId" class="col-form-label">会員ID</label> 
            <input type="text" class="form-control form-control-sm" id="id" name="user_id" autofocus/>
            <label for="inputPassword" class="col-form-label">パスワード</label> 
            <input type="password" class="form-control form-control-sm" id="pw" name="user_pw"/>
            <input class="btn btn-secondary" id="button" type="submit" value="ログイン">
          </form>
        </div>
        <div class="message">
        	<c:if test="${resultMessage ne null}">
        		${resultMessage}
        	</c:if>
        </div>
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
  	    	messages[0] = data["90"].messages["001"]; //"IDは半角数字で入力してください。"
  	  		messages[1] = data["90"].messages["002"]; //"IDを入力してください。"
  			messages[2] = data["90"].messages["003"]; //"IDは6桁の半角数字で入力してください。"
  			messages[3] = data["90"].messages["004"]; //パスワードを入力してください。"
  			messages[4] = data["90"].messages["005"]; //"パスワードは半角文字のみ使用できます。"
  			messages[5] = data["90"].messages["006"]; //"パスワードは8桁の半角文字で入力してください。"
  		
  	  	}).catch(error => console.error("Error fetching messages:", error));
  	}
  
  	function checkForm(path)
  	{
  	    const userId = document.frmLogin.user_id.value.trim();
  	  	const userPw = document.frmLogin.user_pw.value.trim();
  	    const messageDiv = document.querySelector(".message");

  	    // 전각문자 및 전각 숫자 검사 (全角記号 + 全角数字)
  	    const fullWidthPattern = /[^\u0020-\u007E]/;
  		// 영문자(a-z, A-Z)만 허용하는 정규식
  	    const onlyLettersPattern = /^[a-zA-Z]+$/;
  		// 반각 문자(ASCII)만 허용하는 정규식 (user_pw 검사용)
  	    const halfWidthPattern = /^[\x20-\x7E]+$/;

  	    // 입력값이 비어있거나 전각문자 또는 전각 숫자가 포함된 경우
  	    if (fullWidthPattern.test(userId) || onlyLettersPattern.test(userId)) {
  	        messageDiv.innerText = messages[0];
  	      document.frmLogin.user_id.focus();
  	        return false;  // 폼 제출 방지
  	    }
  	    
		if(userId　=== 　"")
		{
  	        messageDiv.innerText = messages[1];
  	        document.frmLogin.user_id.focus();
  	        return false;  // 폼 제출 방지
		}
  	    
		if(userId.length != 6)
		{
  	        messageDiv.innerText = messages[2];
  	        document.frmLogin.user_id.focus();
  	        return false;  // 폼 제출 방지
		}
  	    
		if(userPw === "")
		{
  	        messageDiv.innerText = messages[3];
  	        document.frmLogin.user_pw.focus();
  	        return false;  // 폼 제출 방지
		}
		
  		// user_pw가 반각 문자가 아닐 경우 차단
  	    if (!halfWidthPattern.test(userPw) || !onlyLettersPattern.test(userPw)) {
  	        messageDiv.innerText = messages[4];
  	        document.frmLogin.user_pw.focus();
  	        return false;  // 제출 방지
  	    }
  		
		if(userPw.length != 8)
		{
  	        messageDiv.innerText = messages[5];
  	        document.frmLogin.user_pw.focus();
  	        return false;  // 폼 제출 방지
		}

  	    messageDiv.innerText = "";  // 오류 메시지 초기화
  	    
  	    return true;  // 폼 기본 제출 막기
  	}
  </script>
</html>