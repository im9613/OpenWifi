//package com.wifi.controller;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.wifi.db.MysqlDB;
//
//@WebServlet("/saveWifiList")
//public class WifiSaveServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // JSON 데이터를 읽어오기
//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
//        StringBuilder jsonBuilder = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            jsonBuilder.append(line);
//        }
//
//        String jsonData = jsonBuilder.toString();
//        System.out.println("Received JSON: " + jsonData);
//
//        try {
//            // JSON 데이터 파싱
//            JSONArray wifiArray = new JSONArray(jsonData);
//
//            // MySQL에 데이터 저장
//            for (int i = 0; i < wifiArray.length(); i++) {
//                JSONObject wifi = wifiArray.getJSONObject(i);
//
//                // 데이터 추출
//                String id = wifi.getString("X_SWIFI_MGR_NO");
//                String district = wifi.getString("X_SWIFI_WRDOFC");
//                String wifiName = wifi.getString("X_SWIFI_MAIN_NM");
//                String roadAddress = wifi.getString("X_SWIFI_ADRES1");
//                String detailedAddress = wifi.getString("X_SWIFI_ADRES2");
//                String installFloor = wifi.getString("X_SWIFI_INSTL_FLOOR");
//                String installType = wifi.getString("X_SWIFI_INSTL_TY");
//                String installAgency = wifi.getString("X_SWIFI_INSTL_MBY");
//                String serviceType = wifi.getString("X_SWIFI_SVC_SE");
//                String networkType = wifi.getString("X_SWIFI_CMCWR");
//                String installYear = wifi.getString("X_SWIFI_CNSTC_YEAR");
//                String indoorOutdoor = wifi.getString("X_SWIFI_INOUT_DOOR");
//                String wifiEnv = wifi.getString("X_SWIFI_REMARS3");
//                String lat = wifi.getString("LAT");
//                String lng = wifi.getString("LNT");
//                String workTime = wifi.getString("WORK_DTTM");
//
//                // DB에 저장
//                MysqlDB.insertWifiData(id, district, wifiName, roadAddress, detailedAddress, installFloor, installType,
//                        installAgency, serviceType, networkType, installYear, indoorOutdoor, wifiEnv, lat, lng, workTime);
//            }
//
//            response.setStatus(HttpServletResponse.SC_OK); // 성공 응답
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 서버 오류 응답
//        }
//    }
//}
