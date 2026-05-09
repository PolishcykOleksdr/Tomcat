package com.test.project.dao;

import com.test.project.entity.Order;
import com.test.project.entity.Product;
import com.test.project.inject.Injectable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: user,
 * date: 09.05.2026
 */

public class OrderDAO implements Injectable {
    private final List<Order> orders = new ArrayList<>();
    private int count = 1;

    public Order createOrder(List<Product> products){
        Order order = new Order(
                count++,
                LocalDateTime.now().toString(),
                products.stream().mapToDouble(Product   ::getCost).sum(),
                products
        );
        orders.add(order);

        return order;
    }

    public Order getOrderById(int id) {
        return orders.stream().filter(order -> order.getId() == id).findFirst().orElse(null);
    }

    public Order updateOrderById(int id, List<Product> products) {
        Order order = getOrderById(id);
        if(order != null){
            order.getProducts().addAll(products);
        }
        return order;
    }

    public void deleteOrderById(int id) {
        orders.remove(getOrderById(id));
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
