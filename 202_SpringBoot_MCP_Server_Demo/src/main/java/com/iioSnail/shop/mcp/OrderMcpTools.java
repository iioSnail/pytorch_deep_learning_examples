package com.iioSnail.shop.mcp;

import com.iioSnail.shop.service.OrderService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderMcpTools {

    @Autowired
    private OrderService orderService;

    @McpTool(name = "GetOrderInfo", description = "Get order information by order code")
    public Map<String, String> getOrderInfo(@McpToolParam(description = "order code") String orderCode) {
        return orderService.getOrderInfo(orderCode);
    }

    @McpTool(name = "makeOrder", description = "Make an order")
    public void makeOrder(@McpToolParam(description = "item name") String itemName,
                          @McpToolParam(description = "Number to buy", required = false) Integer num) {
        if (num == null) {
            num = 1;
        }
        orderService.makeOrder(itemName, num);
    }

}
