package com.test.project.servlets;

import com.test.project.inject.Injector;
import com.test.project.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * author: user,
 * date: 09.05.2026
 */

@WebServlet("/delete/*")
public class DeleteOrderById extends HttpServlet {
    private final OrderService orderService = (OrderService) Injector.getInjected("service");

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String requestURI = req.getRequestURI();
        String[] uriPaths = requestURI.split("/");

        int id = Integer.parseInt(uriPaths[uriPaths.length - 1]);

        orderService.deleteOrder(id);

        resp.getWriter().write("Deleted order with id - " + id);
    }
}
