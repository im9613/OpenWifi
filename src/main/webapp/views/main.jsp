<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="com.wifi.model.WifiData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
<style>


</style>
	
<meta charset="UTF-8">
<title>와이파이 리스트</title>
</head>
<body>
    <h1>와이파이 리스트</h1>
    <button onclick="getLocation()">내 위치 가져오기</button><br>
    
    <div>
	    <span>lat:</span> 
	    <input type="text" id="latitude" readonly placeholder="위도">
	    
	    <span>lng:</span>
	    <input type="text" id="longitude" readonly placeholder="경도"><br>
    </div>
    <div>
    	<button onclick="searchNearbyWifi()">근처 와이파이 검색</button>
    </div>
    
    <button onclick="saveWifiList()">저장</button>
    <button onclick="goToPage(1)">불러오기</button>
    
    <table id="wifiListTable" border="1">
        <thead>
            <tr id="main">
                <th>ID</th>
                <th>자치구</th>
                <th>Wi-Fi 이름</th>
                <th>도로명주소</th>
                <th>상세주소</th>
                <th>설치위치(층)</th>
                <th>설치유형</th>
                <th>설치기관</th>
                <th>서비스구분</th>
                <th>망종류</th>
                <th>설치년도</th>
                <th>실내외구분</th>
                <th>Wi-Fi 접속환경</th>
                <th>Y좌표</th>
                <th>X좌표</th>
                <th>작업시간</th>
            </tr>
        </thead>
        
        <tbody>
	        <c:if test="${empty wifiDataList}">
	    		<tr>
	    			<td colspan="14">표시할 데이터가 없습니다.</td>
	    		</tr>
			</c:if>
        
		    <c:forEach var="wifi" items="${wifiDataList}">
		        <tr>
		            <td>${wifi.id}</td>
		            <td>${wifi.district}</td>
		            <td>${wifi.wifi_name}</td>
		            <td>${wifi.road_address}</td>
		            <td>${wifi.detailed_address}</td>
		            <td>${wifi.install_floor}</td>
		            <td>${wifi.install_type}</td>
		            <td>${wifi.install_agency}</td>
		            <td>${wifi.service_type}</td>
		            <td>${wifi.network_type}</td>
		            <td>${wifi.install_year}</td>
		            <td>${wifi.indoor_outdoor}</td>
		            <td>${wifi.wifi_env}</td>
		            <td>${wifi.lat}</td>
		            <td>${wifi.lng}</td>
		            <td>${wifi.work_time}</td>
		        </tr>
		    </c:forEach>
        </tbody>
    </table>
    
    
    <!-- 페이징 -->
		<div>
    <%
        Object currentPageObj = request.getAttribute("currentPage");
        Object totalPageObj = request.getAttribute("totalPage");

        int currentPage = (currentPageObj instanceof Integer) ? (Integer) currentPageObj : 1;
        int totalPage = (totalPageObj instanceof Integer) ? (Integer) totalPageObj : 1;
    %>
		    <button onclick="goToPage(1)">&lt;&lt;</button>
		    <button onclick="goToPage(<%= currentPage - 1 %>)" <%= currentPage <= 1 ? "disabled" : "" %> >&lt;</button>
		    <span><%= currentPage %> / <%= totalPage %></span>
		    <button onclick="goToPage(<%= currentPage + 1 %>)" <%= currentPage >= totalPage ? "disabled" : "" %> >&gt;</button>
		    <button onclick="goToPage(<%= totalPage %>)">&gt;&gt;</button>
		</div>
    
    <script>
    function goToPage(pageNumber) {
        window.location.href = '<%= request.getContextPath() %>/wifiList?page=' + pageNumber;
}

    function saveWifiList() {
        fetch('<%= request.getContextPath() %>/saveWifiList', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ action: 'save' }) // 필요에 따라 추가 데이터 전달 가능
        })
        .then(response => response.text())
        .then(data => {
            alert(data); // 서버 응답 메시지 표시
        })
        .catch(error => {
            console.error('Error:', error);
            alert('데이터 저장에 실패했습니다.');
        });
    }
    
    function getLocation() {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;
                    console.log("위도:", latitude, "경도:", longitude);
                    document.getElementById("latitude").value = latitude; 
                    document.getElementById("longitude").value = longitude; 
                },
                (error) => {
                    document.getElementById("location").textContent = "위치를 가져올 수 없습니다.";
                    console.error(error);
                }
            );
        } else {
            document.getElementById("location").textContent = "지원하지 않는 브라우저 입니다.";
        }
    }
    
    function haversine(lat1, lng1, lat2, lng2) {
    	const R = 6371;
    	const dLat = toRoad(lat2 - lat1);
    	const dLng = toRoad(lng2 - lng1);
    	const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(toRoad(lat1)) * Math.cos(toRoad(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    	const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    	const distance = R * c;
    	return distance;
    }
    
    function toRoad(degrees) {
    	return degrees *(Math.PI / 180);
    }
    
    function searchNearbyWifi() {
    	const userLat = parseFloat(document.getElementById("latitude").value);
    	const userLng = parseFloat(document.getElementById("longitude").value);
    	
    	if (isNaN(userLat) || isNaN(userLng)) {
    		alert("위도와 경도를 입력하세요");
    		return;
    	}
    	
    	const wifiDistances = wifiDataList.map(wifi => {
    		const distance = haversine(userLat, userLng, wifi.lat, wifi.lng);
    		return {wifi, distance};	
    	});
    	 wifiDistances.sort((a, b) => a.distance - b.distance);

    	    // 근처 10개만 출력
    	    const nearestWifiList = wifiDistances.slice(0, 10);

    	    // 새로 갱신
    	    const wifiTableBody = document.querySelector("#wifiListTable tbody");
    	    wifiTableBody.innerHTML = ""; // 기존 테이블 내용 삭제

    	    nearestWifiList.forEach(item => {
    	        const wifi = item.wifi;
    	        const row = `
    	            <tr>
    	                <td>${wifi.id}</td>
    	                <td>${wifi.district}</td>
    	                <td>${wifi.wifi_name}</td>
    	                <td>${wifi.road_address}</td>
    	                <td>${wifi.detailed_address}</td>
    	                <td>${wifi.install_floor}</td>
    	                <td>${wifi.install_type}</td>
    	                <td>${wifi.install_agency}</td>
    	                <td>${wifi.service_type}</td>
    	                <td>${wifi.network_type}</td>
    	                <td>${wifi.install_year}</td>
    	                <td>${wifi.indoor_outdoor}</td>
    	                <td>${wifi.wifi_env}</td>
    	                <td>${wifi.lat}</td>
    	                <td>${wifi.lng}</td>
    	                <td>${wifi.work_time}</td>
    	            </tr>
    	        `;
    	        wifiTableBody.insertAdjacentHTML('beforeend', row);
    	    });
    }
    </script>
    
    

   

</body>
</html>
