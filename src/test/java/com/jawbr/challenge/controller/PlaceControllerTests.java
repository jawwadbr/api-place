package com.jawbr.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jawbr.challenge.dto.PlaceDTO;
import com.jawbr.challenge.entity.Place;
import com.jawbr.challenge.service.PlaceService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaceControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlaceService placeService;

    private static final String PATH = "/api/places";

    private PlaceDTO placeDTO;
    private Place place;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        placeDTO = new PlaceDTO(
                "Valid Name",
                "Valid City",
                "Valid State",
                "valid-name",
                null,
                null);
        place = new Place(
                "Valid Name",
                "valid-name",
                "Valid City",
                "Valid State");
    }

    @Test
    @Order(3)
    @DisplayName("POST Endpoint - Create a Place")
    public void createPlaceTest() throws Exception {

        // Convert the Place object to JSON
        String placeJson = objectMapper.writeValueAsString(place);

        doNothing().when(placeService).save(place);

        // Perform the POST request
        mvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeJson.getBytes()))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print()).andReturn();

        // Verify that the placeService.save() method is called
        verify(placeService, times(1)).save(any(Place.class));
    }

    @Test
    @Order(1)
    @DisplayName("GET Endpoint - Get all Places")
    public void getAllPlacesTest() throws Exception {
        List<PlaceDTO> list = Collections.singletonList(placeDTO);

        when(placeService.getAll(null)).thenReturn(list);

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        String jsonExpected = "[{\"name\":\"Valid Name\"," +
                "\"city\":\"Valid City\"," +
                "\"state\":\"Valid State\"," +
                "\"slug\":\"valid-name\"," +
                "\"createdAt\":null," +
                "\"updatedAt\":null}]";
        Assertions.assertEquals(jsonExpected, response.getContentAsString());
    }

    @Test
    @Order(2)
    @DisplayName("GET Endpoint - Get by Id")
    public void getByIdTest() throws Exception {
        when(placeService.getById(1)).thenReturn(Optional.of(placeDTO));

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get(PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        String jsonExpected = "{\"name\":\"Valid Name\"," +
                "\"city\":\"Valid City\"," +
                "\"state\":\"Valid State\"," +
                "\"slug\":\"valid-name\"," +
                "\"createdAt\":null," +
                "\"updatedAt\":null}";
        Assertions.assertEquals(jsonExpected, response.getContentAsString());
    }

    @Test
    @Order(4)
    @DisplayName("PATCH Endpoint - Edit a Place")
    public void editPlaceTest() throws Exception {
        PlaceDTO newPlaceDTO = new PlaceDTO(
                "New Valid Name",
                "New Valid City",
                "New Valid State",
                "new-valid-name",
                null,
                null);

        place.setId(1);

        given(placeService.updatePatchPlace(eq(1), any(Place.class)))
                .willReturn(Optional.of(newPlaceDTO));

        String placeJson = objectMapper.writeValueAsString(place);

        ResultActions response = mvc.perform(patch(PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(placeJson));

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MockHttpServletResponse httpResponse = response.andReturn().getResponse();
        String responseBody = httpResponse.getContentAsString();

        Assertions.assertNotNull(responseBody);

        response.andExpect(jsonPath("$.name", CoreMatchers.is(newPlaceDTO.name())));
        response.andExpect(jsonPath("$.city", CoreMatchers.is(newPlaceDTO.city())));
        response.andExpect(jsonPath("$.state", CoreMatchers.is(newPlaceDTO.state())));
        response.andExpect(jsonPath("$.slug", CoreMatchers.is(newPlaceDTO.slug())));
        response.andExpect(jsonPath("$.createdAt", CoreMatchers.nullValue()));
        response.andExpect(jsonPath("$.updatedAt", CoreMatchers.nullValue()));
    }

    @Test
    @Order(5)
    @DisplayName("DELETE Endpoint - Delete a Place by Id")
    public void deletePlaceByIdTest() throws Exception {

        doNothing().when(placeService).delete(1);

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete(PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        verify(placeService, times(1)).delete(1);
    }
}
