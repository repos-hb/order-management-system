package com.hb.orderservice.controller;

import com.hb.orderservice.dto.OrderRequest;
import com.hb.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory-cb", fallbackMethod = "circuitBreakerOnFailure")
    @TimeLimiter(name = "inventory-cb")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> circuitBreakerOnFailure(OrderRequest orderRequest, RuntimeException runtimeException){
        if(runtimeException.getClass().getName().equalsIgnoreCase("java.lang.IllegalArgumentException"))
            return CompletableFuture.supplyAsync(()-> runtimeException.getMessage());
        else
            return CompletableFuture.supplyAsync(()-> "Something went wrong. Please try again!");
    }
}
