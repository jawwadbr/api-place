package com.jawbr.challenge.service;

import com.github.slugify.Slugify;
import com.jawbr.challenge.dto.PlaceDTO;
import com.jawbr.challenge.dto.mapper.PlaceDTOMapper;
import com.jawbr.challenge.entity.Place;
import com.jawbr.challenge.exception.PlaceNotFoundException;
import com.jawbr.challenge.repository.PlaceRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceDTOMapper placeDTOMapper;
    private Slugify slugify;

    public PlaceService(PlaceRepository placeRepository, PlaceDTOMapper placeDTOMapper) {
        this.placeRepository = placeRepository;
        this.placeDTOMapper = placeDTOMapper;
        this.slugify = slugify.builder().build();
    }

    // Get all
    public List<PlaceDTO> getAll() {
        return Optional.of(placeRepository.findAll(Sort.by("name").ascending()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new PlaceNotFoundException("No places found inside the database.", System.currentTimeMillis()))
                .stream()
                .map(placeDTOMapper)
                .collect(Collectors.toList());
    }

    // Get by id
    public Optional<PlaceDTO> getById(int id) {
        return Optional.ofNullable(placeRepository.findById(id)
                        .orElseThrow(
                                () -> new PlaceNotFoundException(
                                        "Place with id of " + id + " not found.",
                                        System.currentTimeMillis())))
                .map(placeDTOMapper);
    }

    // Create
    public void save(Place placeRequest) {
        var p = new Place(
                placeRequest.getName(),
                slugify.slugify(placeRequest.getName()),
                placeRequest.getCity(),
                placeRequest.getState());

        placeRepository.save(p);
    }

    // Update
    public Optional<PlaceDTO> updatePatchPlace(int id, Place placeRequest) {
        Optional<Place> place = Optional.ofNullable(placeRepository.findById(id).orElseThrow(
                () -> new PlaceNotFoundException(
                        "Place with id of " + id + " not found.",
                        System.currentTimeMillis())));

        final String name = StringUtils.hasText(placeRequest.getName()) ? placeRequest.getName() : place.get().getName();
        final String city = StringUtils.hasText(placeRequest.getCity()) ? placeRequest.getCity() : place.get().getCity();
        final String state = StringUtils.hasText(placeRequest.getState()) ? placeRequest.getState() : place.get().getState();

        Place patchedPlace = new Place(place.get().getId(), name, city, state, slugify.slugify(placeRequest.getName()), place.get().getCreatedAt(), place.get().getUpdatedAt());
        placeRepository.save(patchedPlace);
        return placeRepository.findById(patchedPlace.getId()).map(placeDTOMapper);
    }

    // Delete
    public void delete(int id) {
        placeRepository.findById(id).ifPresentOrElse(
                placeRepository::delete, () -> {
                    throw new PlaceNotFoundException("Place with id of " + id + " not found.", System.currentTimeMillis());
                }
        );

    }

}
