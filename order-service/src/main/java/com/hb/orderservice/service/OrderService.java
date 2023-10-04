package com.hb.orderservice.service;

import com.hb.orderservice.dto.InventoryResponse;
import com.hb.orderservice.dto.OrderRequest;
import com.hb.orderservice.dto.OrderRequestLineItems;
import com.hb.orderservice.model.Order;
import com.hb.orderservice.model.OrderLineItems;
import com.hb.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        List<OrderLineItems> orderLineItemsList = new ArrayList<>();
        if(null != orderRequest){
            orderLineItemsList = orderRequest.getOrderRequestLineItemsList().stream()
                    .map(this::mapOrderRequestToLineItems)
                    .collect(Collectors.toList());
        }

        if(checkInventory(orderLineItemsList)){
            Order order = new Order();
            order.setOrderNum(UUID.randomUUID().toString());
            order.setLineItemsList(orderLineItemsList);

            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product Out of Stock! Review Order!");
        }




    }

    private boolean checkInventory(List<OrderLineItems> orderLineItemsList) {

        // create a list of sku ids
        List<String> skuIds = orderLineItemsList.stream().map(OrderLineItems::getSkuId)
                .collect(Collectors.toList());

        // call inventory service
        InventoryResponse[] stocks = webClient.get()
                                            .uri("http://localhost:8085/api/inventory",
                                                    uriBuilder -> uriBuilder.queryParam("skuIds", skuIds).build())
                                            .retrieve()
                                            .bodyToMono(InventoryResponse[].class)
                                            .block();

        // check stock status for each sku
        return stocks.length > 0 && Arrays.stream(stocks).allMatch(InventoryResponse::getIsInStock);
    }

    private OrderLineItems mapOrderRequestToLineItems(OrderRequestLineItems orderRequestLineItems) {
        return  OrderLineItems.builder()
                .skuId(orderRequestLineItems.getSkuId())
                .price(orderRequestLineItems.getPrice())
                .quantity(orderRequestLineItems.getQuantity())
                .build();
    }
}
