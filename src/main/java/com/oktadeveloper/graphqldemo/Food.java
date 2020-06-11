package com.oktadeveloper.graphqldemo;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


//@Entity
@Data
@Builder
@GraphQLType(name = "Food", description = "Food description")
public class Food {

    @GraphQLQuery(name = "id", description = "A food's id")
    private Long id;

    @GraphQLQuery(name = "name", description = "A food's name")
    private String name;

    @Builder.Default
    @GraphQLQuery(name = "nutritionalValues", description = "A food's name")
    private Set<FoodIngredient> nutritionalValues = new HashSet<>();

}