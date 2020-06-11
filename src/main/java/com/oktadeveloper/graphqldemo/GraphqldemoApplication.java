package com.oktadeveloper.graphqldemo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class GraphqldemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqldemoApplication.class, args);
    }


    @Bean
    ApplicationRunner init(FoodService foodService) {
        return args -> {
            Stream.of(
                    Food.builder().id(1L).name("Pizza").build()
                    ,Food.builder().id(2L).name("Spam").build()
                    ,Food.builder().id(3L).name("Eggs").build()
                    ,Food.builder().id(4L).name("Avocado").build()
            ).forEach(f -> {
                f.getNutritionalValues().add( FoodIngredient.builder().name("calories").value(100 * f.getId().intValue() ).measurementUnit("100 grams").build() );
                Food food = f;
                foodService.saveFood(food);
            });
            foodService.getFoods().forEach(System.out::println);
        };
    }
}
