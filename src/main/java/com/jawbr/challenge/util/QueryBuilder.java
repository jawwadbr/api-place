package com.jawbr.challenge.util;

import com.jawbr.challenge.entity.Place;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {

    public static Example<Place> createQuery(Place place) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(place, exampleMatcher);
    }
}
