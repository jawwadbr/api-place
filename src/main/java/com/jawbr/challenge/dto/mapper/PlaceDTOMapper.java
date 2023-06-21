package com.jawbr.challenge.dto.mapper;

import com.jawbr.challenge.dto.PlaceDTO;
import com.jawbr.challenge.entity.Place;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Function;

@Service
public class PlaceDTOMapper implements Function<Place, PlaceDTO> {

    @Override
    public PlaceDTO apply(Place place) {
        return new PlaceDTO(place.getName(),
                place.getCity(),
                place.getState(),
                place.getSlug(),
                place.getCreatedAt(),
                place.getUpdatedAt());
    }
}
