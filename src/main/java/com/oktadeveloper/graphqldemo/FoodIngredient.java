package com.oktadeveloper.graphqldemo;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodIngredient {

    @GraphQLQuery(name = "name", description = "A food's ingredient name")
    private String name;

    @GraphQLQuery(name = "value", description = "A food's ingredient value")
    private Integer value;

    @GraphQLQuery(name = "measurementUnit", description = "A food's ingredient measurement unit")
    private String measurementUnit;


}
