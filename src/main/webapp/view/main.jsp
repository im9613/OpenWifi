<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 리스트</title>
</head>
<body>
	<h1>와이파이 리스트</h1>
	<button onclick="saveWifiList()">저장</button>
	<button onclick="LoadWifiList()">불러오기</button>
	
	<table id=wifiListTable" border="1">
		<thead>
			<tr>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
		</thead>
	</table>

</body>
</html>