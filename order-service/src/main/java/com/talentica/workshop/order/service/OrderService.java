package com.talentica.workshop.order.service;

import com.talentica.workshop.order.model.Order;
import com.talentica.workshop.order.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for @Transactional
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final RestTemplate restTemplate = new RestTemplate();
    private Map<String, Map<String, Integer>> coffees;

    @Value("${inventory.url}")
    String inventoryUrl;

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    private void initializeCoffee() {
        coffees = new HashMap<>();
        coffees.put("cappuccino", Map.of("espressoShot", 1,
                "milk", 200,
                "milkFoam", 50)
        );

        coffees.put("americano", Map.of("espressoShot", 1,
                "hotWater", 150)
        );
    }

    @Transactional // Add this annotation
    public Order placeOrder(String coffeeType, int quantity) {
        if (ObjectUtils.isEmpty(coffeeType)) {
            coffeeType = "cappuccino";
        }
        Map<String, Integer> ingredients = getIngredient(coffeeType, quantity);
        boolean available = restTemplate.postForObject(
                inventoryUrl + "/inventory/used",
                ingredients, boolean.class);

        if (available) {
            Order order = new Order(coffeeType, quantity, "Confirmed");
            orderRepository.save(order);
            return order;
        } else {
            return new Order(coffeeType, quantity, "Out of Stock");
        }
    }

    private Map<String, Integer> getIngredient(String coffeeType, int quantity) {
        Map<String, Integer> ingredientsOrg = coffees.get(coffeeType);
        Map<String, Integer> ingredients = new HashMap<>(ingredientsOrg);
        for (String key: ingredients.keySet()) {
            ingredients.put(key, ingredients.get(key) * quantity);
        }
        return ingredients;
    }

    public List<Order> getOrderHistory() {
        return orderRepository.findAll();
    }
}