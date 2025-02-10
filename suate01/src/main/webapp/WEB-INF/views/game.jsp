<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%
	List<String> inputNumList = (List<String>)session.getAttribute("input_num_list");
	List<String> gameResultList = (List<String>)session.getAttribute("game_result_list");
%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${path}/resources/styles/suate.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <title>数当てゲーム</title>
</head>
<body onload="checkResult(${resultMessage})">
    <div class="mainframe">
      <div class="title">
       	 数当てゲーム
      </div>
    <div class="currentPoint">
        <h4>保有ポイント ${sessionScope.user_point}</h4>
    </div>
    <div class="secretNum">
        隠れ数字 : * * *
    </div>
    <div class="inputNum">
       <c:if test="${finishedFlag eq false}">
        <form action="${path}/input" method="post" onsubmit="return checkForm()">
           	 入力
            <input type="number" maxlength="1" oninput="lengthChk(this);" class="inputs" onkeyup="moveFocus(1, this, this.form.second)" id="first" name="first" autofocus>
            <input type="number" maxlength="1" oninput="lengthChk(this);" class="inputs" onkeyup="moveFocus(1, this, this.form.third)" id="second" name="second">
            <input type="number" maxlength="1" oninput="lengthChk(this);" class="inputs" id="third" name="third">
            <input type="submit" value="確認">
        </form>
       </c:if> 
    </div>
    <div class="inputRecord">
        <table class="table table-striped table-hover table-sm">
            <tr>
                <th>入力回数</td>
                <th>入力情報</td>
                <th>判定結果</td>
            </tr>
		<% for(int i = 0; i < 10; i++) { %>
			<tr>
    			<td><%= i + 1 %>回目</td>
    			<td><%= (inputNumList != null && inputNumList.size() > i) ? inputNumList.get(i) : "" %></td>
    			<td><%= (gameResultList != null && gameResultList.size() > i) ? gameResultList.get(i) : "" %></td>
			</tr>
		<% } %>
        </table>
    </div>
    <div class="message">
    	${resultMessage}
    </div>
    </div>
</body>
<script type="text/javascript">
var messages = [];
	  
function initMessage(path)
{
  	fetch(path + '/api/messages')
  	 .then(response => response.json())
  	 .then(data => 
  	 {
  	    messages[0] = data["90"].messages["101"]; //"1桁の数字を入力してください。"
  	  	messages[1] = data["90"].messages["102"]; //"空いている欄があります。数字を入力してください。"
  		messages[2] = data["90"].messages["103"]; //"修復していない数字を入力してください。"
  		messages[3] = data["90"].messages["104"]; //"今日はこれ以上プレイできません。"
  		
  	 }).catch(error => console.error("Error fetching messages:", error));
}

function lengthChk(object){
	if(object.value.length > object.maxLength){
	   object.value = object.value.slice(0, object.maxLength);
	}
}

function moveFocus(num, curr, next)
{
    let n = curr.value.length;
    if(n == num)
    {
        next.focus();
    }
}

function checkResult(resultMessage)
{
	if(resultMessage)
	{
		let popOption = "width = 300px, height = 400px";
		window.open('${path}/popup', 'pop', popOption);
	}
	else
	{
		initMessage('${path}');
	}
}

function checkForm()
{
	let inputs = document.querySelectorAll(".inputs");
	const messageDiv = document.querySelector(".message");
		
	for(let i = 0; i < inputs.length; i++)
	{		
		if(inputs[i].value.trim() === "")
		{			
			messageDiv.innerText = messages[1]; // "비어 있는 란이 있습니다. 숫자를 입력하세요"
			inputs[i].focus();
			return false;
		}
		else
		{
			if (!/^\d$/.test(inputs[i].value)) {
				messageDiv.innerText = messages[0];  // "한 자리 숫자를 입력하세요."
			    inputs[i].focus();
			    return false;
			}
		}
	}
	
	for(let i = 0; i < inputs.length; i++)
	{
		let temp = inputs[i].value.trim();
		
		for(let j = i + 1; j < inputs.length; j++)
		{
			if(temp === inputs[j].value.trim())
			{
				messageDiv.innerText = messages[2];  // "중복되지 않는 숫자를 입력하세요."
				return false;
			}	
		}
	}
	
	return true;
}
</script>
</html>