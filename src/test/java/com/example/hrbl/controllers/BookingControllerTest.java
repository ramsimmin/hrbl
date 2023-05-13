package com.example.hrbl.controllers;

import com.example.hrbl.dto.BookingDTO;
import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.dto.ResponseDTO;
import com.example.hrbl.repository.BookingRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    BookingRepository bookingRepository;

    private static String bookingId;


    @BeforeAll
    void bookMeeting() throws Exception {
        String requestBody = mapper.writeValueAsString(BookingRequestDTO.builder().email("email").room("alpha room").date("14/05/2024").timeFrom("10:00").timeTo("11:00").build());
        mockMvc.perform(post("/book")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @Order(1)
    void bookOverlappingMeeting() throws Exception {
        String requestBody = mapper.writeValueAsString(BookingRequestDTO.builder().email("email").room("alpha room").date("14/05/2024").timeFrom("10:30").timeTo("11:30").build());
        MvcResult mvcResult = mockMvc.perform(post("/book")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDTO = mapper.readValue(response, ResponseDTO.class);

        Set<String> expectedErrors = Set.of("The requested time slot is already booked");
        assertEquals(expectedErrors, responseDTO.getErrorMessages(), "Error do not match the expected ones");
    }


    @Test
    @Order(2)
    void getBookings() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<BookingDTO> responseList = mapper.readValue(response, new TypeReference<>() {});
        Assertions.assertAll(
                () -> assertTrue(responseList.size() ==1, "Bookings list size must be 1"),
                () -> assertEquals("14/05/2024", responseList.get(0).getDate(), "Date does not match"),
                () -> assertEquals("10:00", responseList.get(0).getTimeFrom(), "Time from does not match"),
                () -> assertEquals("11:00", responseList.get(0).getTimeTo(), "Time to does not match")
        );

        bookingId = responseList.get(0).getId();
    }


    @Test
    @Order(3)
    void cancelBooking() throws Exception {
       mockMvc.perform(delete("/cancel/" + bookingId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }


    @AfterAll
    void cleanUp () {
        bookingRepository.deleteAll();
    }
}