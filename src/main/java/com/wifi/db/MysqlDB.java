package com.wifi.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wifi.model.WifiData;

public class MysqlDB {
    
    private static String url;
    private static String user;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(fis);

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC 로드 실패 또는 db.properties 파일 로드 실패", e);
        }
    }
    
    // MySQL 연결 메서드
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }

    // 데이터 삽입 메서드
    public static void insertWifiData(String id, String district, String wifi_name, String road_address, String detailed_address, 
                                      String install_floor, String install_type, String install_agency, String service_type, 
                                      String network_type, String install_year, String indoor_outdoor, String wifi_env, 
                                      String lat, String lng, String work_time) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // DB 연결
            conn = getConnection();
            
            // 데이터 삽입 쿼리
            String insertQuery = "INSERT INTO wifilist (id, district, wifi_name, road_address, detailed_address, install_floor, install_type, install_agency, " +
                                 "service_type, network_type, install_year, indoor_outdoor, wifi_env, lat, lng, work_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            // PreparedStatement 생성
            stmt = conn.prepareStatement(insertQuery);
            
            // 데이터 설정
            stmt.setString(1, id);
            stmt.setString(2, district);
            stmt.setString(3, wifi_name);
            stmt.setString(4, road_address);
            stmt.setString(5, detailed_address);
            stmt.setString(6, install_floor);
            stmt.setString(7, install_type);
            stmt.setString(8, install_agency);
            stmt.setString(9, service_type);
            stmt.setString(10, network_type);
            stmt.setString(11, install_year);
            stmt.setString(12, indoor_outdoor);
            stmt.setString(13, wifi_env);
            stmt.setString(14, lat);
            stmt.setString(15, lng);
            stmt.setString(16, work_time);
            
            // 데이터 삽입 실행
            stmt.executeUpdate();
            System.out.println("데이터 삽입 완료: " + wifi_name);
            
        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 출력
        } finally {
            // 자원 해제
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
 // 페이지에 맞는 데이터 가져오기
    public List<WifiData> getWifiDataByPage(int currentPage, int pageSize) throws SQLException {
        String query = "SELECT * FROM wifilist LIMIT ? OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            int offset = (currentPage - 1) * pageSize;
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            List<WifiData> wifiDataList = new ArrayList<>();
            while (rs.next()) {
                WifiData wifi = new WifiData(
                    rs.getString("id"),
                    rs.getString("district"),
                    rs.getString("wifi_name"),
                    rs.getString("road_address"),
                    rs.getString("detailed_address"),
                    rs.getString("install_floor"),
                    rs.getString("install_type"),
                    rs.getString("install_agency"),
                    rs.getString("service_type"),
                    rs.getString("network_type"),
                    rs.getInt("install_year"),
                    rs.getString("indoor_outdoor"),
                    rs.getString("wifi_env"),
                    rs.getDouble("lat"),
                    rs.getDouble("lng"),
                    rs.getString("work_time")
                );
                wifiDataList.add(wifi);
            }
            return wifiDataList;
        }
    }
    public int getWifiDataCount() throws SQLException {
    	String Query = "SELECT COUNT(*) FROM wifilist";
    	try (Connection conn = DriverManager.getConnection(url, user, password);
    		PreparedStatement stmt = conn.prepareStatement(Query);
    			ResultSet rs = stmt.executeQuery()) {
    		if (rs.next()) {
    			return rs.getInt(1);
    		}
    		return 0;
    	}
    }
}
