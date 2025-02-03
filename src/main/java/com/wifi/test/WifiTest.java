package com.wifi.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wifi.db.MysqlDB;

public class WifiTest {
    private static final String KEY = System.getenv("API_KEY");
    private static final String TYPE = "json";
    private static final String SERVICE = "TbPublicWifiInfo";
    private static final int TOTAL_RECORDS = 24498;
    private static final int PAGE_SIZE = 1000;

    public void fetchAndSaveWifiData() {
        for (int startIndex = 1; startIndex <= TOTAL_RECORDS; startIndex += PAGE_SIZE) {
            int endIndex = Math.min(startIndex + PAGE_SIZE - 1, TOTAL_RECORDS);
            try {
                // URL 생성
                StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
                urlBuilder.append("/").append(URLEncoder.encode(KEY, "UTF-8"));
                urlBuilder.append("/").append(TYPE);
                urlBuilder.append("/").append(SERVICE);
                urlBuilder.append("/").append(startIndex);
                urlBuilder.append("/").append(endIndex);

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                // 응답 코드 확인
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP GET Request Failed with Error Code: " + conn.getResponseCode());
                }

                // 응답 데이터 읽기
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                // JSON 파싱 및 DB 저장
                JSONObject responseJson = new JSONObject(sb.toString());
                JSONArray rowArray = responseJson.getJSONObject("TbPublicWifiInfo").getJSONArray("row");

                for (int i = 0; i < rowArray.length(); i++) {
                    JSONObject wifi = rowArray.getJSONObject(i);

                    // 필요한 데이터 추출
                    String id = wifi.getString("X_SWIFI_MGR_NO");
                    String district = wifi.getString("X_SWIFI_WRDOFC");
                    String wifi_name = wifi.getString("X_SWIFI_MAIN_NM");
                    String road_address = wifi.getString("X_SWIFI_ADRES1");
                    String detailed_address = wifi.getString("X_SWIFI_ADRES2");
                    String install_floor = wifi.getString("X_SWIFI_INSTL_FLOOR");
                    String install_type = wifi.getString("X_SWIFI_INSTL_TY");
                    String install_agency = wifi.getString("X_SWIFI_INSTL_MBY");
                    String service_type = wifi.getString("X_SWIFI_SVC_SE");
                    String network_type = wifi.getString("X_SWIFI_CMCWR");
                    String install_year = wifi.getString("X_SWIFI_CNSTC_YEAR");
                    String indoor_outdoor = wifi.getString("X_SWIFI_INOUT_DOOR");
                    String wifi_env = wifi.getString("X_SWIFI_REMARS3");
                    String lat = wifi.getString("LAT");
                    String lng = wifi.getString("LNT");
                    String work_time = wifi.getString("WORK_DTTM");

                    // DB에 데이터 삽입
                    MysqlDB.insertWifiData(id, district, wifi_name, road_address, detailed_address, install_floor, install_type, install_agency,
                            service_type, network_type, install_year, indoor_outdoor, wifi_env, lat, lng, work_time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
