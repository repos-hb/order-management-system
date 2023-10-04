package com.hb.orderservice.service;

import com.hb.orderservice.dto.OrderRequest;
import com.hb.orderservice.dto.OrderRequestLineItems;
import com.hb.orderservice.model.Order;
import com.hb.orderservice.model.OrderLineItems;
import com.hb.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        List<OrderLineItems> orderLineItemsList = new ArrayList<>();
        if(null != orderRequest){
            orderLineItemsList = orderRequest.getOrderRequestLineItemsList().stream()
                    .map(this::mapOrderRequestToLineItems)
                    .collect(Collectors.toList());
        }

        Order order = new Order();
        order.setOrderNum(UUID.randomUUID().toString());
        order.setLineItemsList(orderLineItemsList);

        orderRepository.save(order);
    }

    private OrderLineItems mapOrderRequestToLineItems(OrderRequestLineItems orderRequestLineItems) {
        return  OrderLineItems.builder()
                .skuId(orderRequestLineItems.getSkuId())
                .price(orderRequestLineItems.getPrice())
                .quantity(orderRequestLineItems.getQuantity())
                .build();
    }
}
