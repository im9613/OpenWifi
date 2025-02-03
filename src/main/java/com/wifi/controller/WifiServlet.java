package com.wifi.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wifi.db.MysqlDB;
import com.wifi.model.WifiData;
import com.wifi.test.WifiTest;

@WebServlet({"/saveWifiList","/wifiList"})
public class WifiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // WifiTest의 main 메서드 로직 호출
            WifiTest wifiTest = new WifiTest();
            wifiTest.fetchAndSaveWifiData(); // API 호출 및 DB 저장 로직 실행

            // 응답 반환
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("데이터 저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("데이터 저장 실패: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        
        if (path.equals("/wifiList")) { // 조회 요청인지 확인
            System.out.println("===== WifiServlet: 데이터 조회 실행됨 =====");

            try {
                int currentPage = 1;
                String pageParam = request.getParameter("page");
                if (pageParam != null && pageParam.matches("\\d+")) {
                    currentPage = Integer.parseInt(pageParam);
                }

                MysqlDB mysqlDB = new MysqlDB();
                int totalCount = mysqlDB.getWifiDataCount();
                int totalPage = (int) Math.ceil(totalCount / (double) PAGE_SIZE);

                List<WifiData> wifiDataList = mysqlDB.getWifiDataByPage(currentPage, PAGE_SIZE);

                request.setAttribute("wifiDataList", wifiDataList);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPage", totalPage);

                System.out.println("데이터 개수: " + wifiDataList.size());
                System.out.println("이동할 JSP: /views/main.jsp");

                request.getRequestDispatcher("/views/main.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("데이터 조회 실패: " + e.getMessage());
            }
        } else {
            // 지원하지 않는 요청이면 404 응답
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
