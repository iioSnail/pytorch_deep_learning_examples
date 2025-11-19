package com.iioSnail.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Map<String, String> getOrderInfo(String orderCode) {
        logger.info("Query order info: {}", orderCode);
        return Map.of(
                "orderCode", orderCode,
                "createDate", "2025-11-19",
                "itemName", "Toy Car",
                "price", "$5.23"
        );
    }

    public void makeOrder(String itemName, Integer num) {
        logger.info("Make an order; itemName: {}, num: {}", itemName, num);
    }

}
