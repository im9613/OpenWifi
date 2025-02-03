package com.wifi.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wifi.db.MysqlDB;

@WebServlet("/loadWifiList")
public class WifiLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        JSONArray wifiList = new JSONArray();

        try (Connection conn = MysqlDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM wifilist");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JSONObject wifi = new JSONObject();
                wifi.put("id", rs.getString("id"));
                wifi.put("district", rs.getString("district"));
                wifi.put("wifi_name", rs.getString("wifi_name"));
                wifi.put("road_address", rs.getString("road_address"));
                wifi.put("detailed_address", rs.getString("detailed_address"));
                wifi.put("install_floor", rs.getString("install_floor"));
                wifi.put("install_type", rs.getString("install_type"));
                wifi.put("install_agency", rs.getString("install_agency"));
                wifi.put("service_type", rs.getString("service_type"));
                wifi.put("network_type", rs.getString("network_type"));
                wifi.put("install_year", rs.getString("install_year"));
                wifi.put("indoor_outdoor", rs.getString("indoor_outdoor"));
                wifi.put("wifi_env", rs.getString("wifi_env"));
                wifi.put("lat", rs.getString("lat"));
                wifi.put("lng", rs.getString("lng"));
                wifi.put("work_time", rs.getString("work_time"));
                wifiList.put(wifi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.getWriter().write(wifiList.toString());
    }
}