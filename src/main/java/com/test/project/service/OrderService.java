package com.test.project.service;

import com.test.project.entity.Order;
import com.test.project.entity.Product;
import com.test.project.inject.Injectable;
import com.test.project.dao.OrderDAO;

import java.util.List;

/**
 * author: user,
 * date: 09.05.2026
 */

public class OrderService implements Injectable {
    private final OrderDAO dao;

    public OrderService(OrderDAO dao) {
        this.dao = dao;
    }

    public Order createOrder(List<Product> products){
        return dao.createOrder(products);
    }

    public Order getById(int id){
        return dao.getOrderById(id);
    }

    public Order updateOrder(int id, List<Product> products) {
        return dao.updateOrderById(id, products);
    }

    public void deleteOrder(int id) {
        dao.deleteOrderById(id);
    }

    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }
}