package com.test.project.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.project.entity.Order;
import com.test.project.entity.Product;
import com.test.project.inject.Injector;
import com.test.project.service.OrderService;
import com.test.project.util.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * author: user,
 * date: 09.05.2026
 */

@WebServlet("/create")
public class CreateProductServlet extends HttpServlet {
    private final OrderService orderService = (OrderService) Injector.getInjected("service");
    private final ObjectMapper mapper = JsonMapper.create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Product> products = mapper.readValue(req.getInputStream(), new TypeReference<>() {});
        Order order = orderService.createOrder(products);

        if (order == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\":\"Order not found\"}");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(resp.getOutputStream(), order);
    }
}
