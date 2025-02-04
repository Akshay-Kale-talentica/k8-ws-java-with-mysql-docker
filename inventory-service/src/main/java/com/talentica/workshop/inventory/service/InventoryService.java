package com.talentica.workshop.inventory.service;

import com.talentica.workshop.inventory.model.Ingredient;
import com.talentica.workshop.inventory.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private IngredientRepository ingredientRepository;

    private final Map<String, Ingredient> stock = new HashMap<>();

    public void initializeInventory() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for(Ingredient ingredient: ingredients){
            stock.put(ingredient.getName(), ingredient);
        }
    }

    public Map<String, Ingredient> getStock() {
        initializeInventory();
        return stock;
    }

    public boolean checkStock(String ingredient, int requiredQuantity) {
        Optional<Ingredient> item = ingredientRepository.findById(ingredient);
        return item.isPresent() && item.get().getQuantity() >= requiredQuantity;
    }

    public void useIngredient(String ingredient, int quantity) {
        Optional<Ingredient> item = ingredientRepository.findById(ingredient);
        if (item.isPresent()) {
            Ingredient ingredientInDb = item.get();
            ingredientInDb.reduceQuantity(quantity);
            ingredientRepository.save(ingredientInDb);
            stock.put(ingredient, ingredientInDb);
        }
    }

    public void replenishIngredient(Ingredient ingredient) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findById(ingredient.getName());
        if(existingIngredient.isPresent()){
            Ingredient ingredientInDb = existingIngredient.get();
            ingredientInDb.setQuantity(ingredient.getQuantity() + ingredientInDb.getQuantity());
            ingredientRepository.save(ingredientInDb);
            stock.put(ingredient.getName(), ingredientInDb);
        } else {
            ingredientRepository.save(ingredient);
            stock.put(ingredient.getName(), ingredient);
        }
    }
}