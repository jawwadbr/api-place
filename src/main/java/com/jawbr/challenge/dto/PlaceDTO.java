package com.jawbr.challenge.dto;

import com.jawbr.challenge.entity.Place;

import java.time.LocalDateTime;

public record PlaceDTO(
        String name,
        String city,
        String state,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
