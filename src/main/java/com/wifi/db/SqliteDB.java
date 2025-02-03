package com.wifi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteDB {

	 public static Connection connect() throws SQLException {
	        // SQLite 데이터베이스 파일 경로 (파일이 없으면 자동으로 생성)
	        String url = "jdbc:sqlite:wifi_data.db";
	        return DriverManager.getConnection(url);
	    }

	    // 테이블 생성 메서드
	    public static void createTable() {
	        // 테이블 생성 SQL 쿼리
	        String createTableSQL = "CREATE TABLE IF NOT EXISTS wifi_info ("
	                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
	                + "wifi_name TEXT, "
	                + "address TEXT, "
	                + "manager_number TEXT, "
	                + "installation_type TEXT, "
	                + "installation_year TEXT"
	                + ");";

	        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
	            // 테이블 생성 실행
	            stmt.execute(createTableSQL);
	            System.out.println("테이블이 생성되었습니다.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // 데이터 삽입 메서드
	    public static void insertWifiData(String wifiName, String address, String managerNumber,
	                                      String installationType, String installationYear) {
	        String insertSQL = "INSERT INTO wifi_info (wifi_name, address, manager_number, installation_type, installation_year) "
	                         + "VALUES (?, ?, ?, ?, ?);";

	        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
	            // 파라미터 설정
	            pstmt.setString(1, wifiName);
	            pstmt.setString(2, address);
	            pstmt.setString(3, managerNumber);
	            pstmt.setString(4, installationType);
	            pstmt.setString(5, installationYear);

	            // 데이터 삽입 실행
	            pstmt.executeUpdate();
	            System.out.println("데이터가 삽입되었습니다.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // 데이터 조회 및 콘솔 출력
	    public static void printWifiData() {
	        String selectSQL = "SELECT * FROM wifi_info";

	        try (Connection conn = connect(); Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(selectSQL)) {

	            while (rs.next()) {
	                System.out.println("Wi-Fi 이름: " + rs.getString("wifi_name"));
	                System.out.println("주소: " + rs.getString("address"));
	                System.out.println("관리번호: " + rs.getString("manager_number"));
	                System.out.println("설치 유형: " + rs.getString("installation_type"));
	                System.out.println("설치 연도: " + rs.getString("installation_year"));
	                System.out.println("-----------------------------------");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

}
