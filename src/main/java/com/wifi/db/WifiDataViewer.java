package com.wifi.db;

import java.sql.*;

public class WifiDataViewer {
    public static void main(String[] args) {
        // 데이터베이스 연결
        try (Connection conn = DriverManager.getConnection("jdbc:mysql:wifi_data.db")) {
        	
        	
        	// 테이블 명
        	String checkName = "SELECT name FROM sqlite_master WHERE type='table'";
        	try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(checkName)){
        		while (rs.next()) {
        			System.out.println("테이블명 :" + rs.getString("name"));
        		}
			}
            // SELECT 쿼리 실행
            String sql = "SELECT * FROM wifi_info";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                // 결과 출력
                while (rs.next()) {
                    System.out.println("Wi-Fi 이름: " + rs.getString("wifi_name"));
                    System.out.println("주소: " + rs.getString("address"));
                    System.out.println("관리번호: " + rs.getString("manager_number"));
                    System.out.println("설치 유형: " + rs.getString("installation_type"));
                    System.out.println("설치 연도: " + rs.getString("installation_year"));
                    System.out.println("====================================");
                    
                }
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 오류: " + e.getMessage());
        }
    }
}
