package com.jawbr.challenge.controller;

import com.jawbr.challenge.dto.PlaceDTO;
import com.jawbr.challenge.entity.Place;
import com.jawbr.challenge.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    /*
        TEST JSON - DELETE THIS LATER
    {
        "id": 1,
        "name": "place",
        "slug": "place",
        "city": "place",
        "state": "place",
        "createdAt": "2023-06-07T14:45:39.693689",
        "updatedAt": "2023-06-07T14:45:39.693689"
    }
    */

    // GET
    @GetMapping
    public List<PlaceDTO> getAll() {
        return placeService.getAll();
    }

    @GetMapping("{id}")
    public Optional<PlaceDTO> getPlaceById(@PathVariable int id) {
        return placeService.getById(id);
    }

    // POST
    @PostMapping()
    public ResponseEntity<?> createPlace(@Valid @RequestBody Place place) {
        placeService.save(place);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // PATCH
    @PatchMapping("{id}")
    public Optional<PlaceDTO> editPlace(@PathVariable int id, @RequestBody Place place) {
        return placeService.updatePatchPlace(id, place);
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id) {
        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
