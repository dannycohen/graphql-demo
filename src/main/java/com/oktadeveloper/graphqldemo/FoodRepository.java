package com.oktadeveloper.graphqldemo;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FoodRepository {

    private Map<Long, Food> foods = new HashMap<>();

    public List<Food> findAll(){
        return new ArrayList<>(foods.values());
    }

    public Optional<Food> findById(Long id){
        return Optional.ofNullable(foods.get(id));
    }

    public Food save(Food food){
        foods.put(food.getId(), food);
        return food;
    }

    public void deleteById(Long id){
        foods.remove(id);
    }


}