package com.test.project.inject;

import com.test.project.dao.OrderDAO;
import com.test.project.service.OrderService;

import java.util.HashMap;
import java.util.Map;

/**
 * author: user,
 * date: 09.05.2026
 */
public class Injector {
    private static final Map<String, Injectable> injectables = new HashMap<>();

    private Injector() {}

    static{
        OrderDAO dao = new OrderDAO();
        OrderService service = new OrderService(dao);

        injectables.put("dao", dao);
        injectables.put("service", service);
    }

    public static Injectable getInjected(String name){
        return injectables.get(name);
    }
}