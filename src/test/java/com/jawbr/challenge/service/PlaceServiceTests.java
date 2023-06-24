package com.jawbr.challenge.service;

import com.jawbr.challenge.dto.PlaceDTO;
import com.jawbr.challenge.dto.mapper.PlaceDTOMapper;
import com.jawbr.challenge.entity.Place;
import com.jawbr.challenge.exception.PlaceNotFoundException;
import com.jawbr.challenge.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class PlaceServiceTests {

    @InjectMocks
    private PlaceService placeService;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceDTOMapper placeDTOMapper;

    private Place place;

    @BeforeEach
    public void init() {
        place = new Place();
        place.setName("Valid Name");
        place.setCity("Valid City");
        place.setState("Valid State");

        placeService = new PlaceService(placeRepository, placeDTOMapper);

    }

    @Test
    public void canGetAllTest() {
        Place place2 = new Place();

        List<PlaceDTO> expected = new ArrayList<>();
        expected.add(placeDTOMapper.apply(place));
        expected.add(placeDTOMapper.apply(place2));

        given(placeRepository.findAll(any(Example.class), any(Sort.class))).willReturn(expected);

        List<PlaceDTO> result = placeService.getAll(null);

        assertNotNull(result);
        assertEquals(2, placeService.getAll(null).size());
    }

    @Test
    public void cannotGetAllTest() {
        assertThrows(
                PlaceNotFoundException.class,
                () -> placeService.getAll(null),
                "No places found inside the database.");
    }

    @Test
    public void canGetByIdTest() {
        int placeId = 1;
        place.setId(placeId);

        when(placeRepository.findById(placeId)).thenReturn(Optional.ofNullable(place));

        Optional<PlaceDTO> placeReturn = placeService.getById(placeId);

        assertNotNull(placeReturn);
    }

    @Test
    public void cannotGetByIdTest() {
        int placeId = 0;
        assertThrows(PlaceNotFoundException.class,
                () -> placeService.getById(placeId), "Place with id of 1 not found.");
    }

    @Test
    public void canCreatePlaceTest() {
        place.setSlug("valid-name");

        when(placeRepository.save(any(Place.class))).thenReturn(place);

        placeService.save(place);

        verify(placeRepository, times(1)).save(argThat(
                verifiedPlace -> verifiedPlace.getName().equals(place.getName()) &&
                        verifiedPlace.getSlug().equals(place.getSlug()) &&
                        verifiedPlace.getCity().equals(place.getCity()) &&
                        verifiedPlace.getState().equals(place.getState())
        ));
    }

    @Test
    public void canUpdatePlaceTest() {
        int placeId = 1;
        place.setId(placeId);

        when(placeRepository.findById(placeId)).thenReturn(Optional.ofNullable(place));
        when(placeRepository.save(place)).thenReturn(place);

        Optional<PlaceDTO> updatedReturn = placeService.updatePatchPlace(placeId, place);

        assertNotNull(updatedReturn);
    }

    @Test
    public void cannotUpdatePlaceTest() {
        int placeId = 0;
        assertThrows(PlaceNotFoundException.class,
                () -> placeService.updatePatchPlace(placeId, null),
                "Place with id of 1 not found.");
    }

    @Test
    public void canDeletePlaceTest() {
        int placeId = 1;
        place.setId(placeId);

        when(placeRepository.findById(placeId)).thenReturn(Optional.ofNullable(place));
        doNothing().when(placeRepository).delete(place);

        assertAll(() -> placeService.delete(placeId));
    }

    @Test
    public void cannotDeletePlaceTest() {
        int placeId = 0;
        assertThrows(PlaceNotFoundException.class,
                () -> placeService.delete(placeId),
                "Place with id of 1 not found.");
    }
}
