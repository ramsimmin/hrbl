package com.example.hrbl.validations;

import com.example.hrbl.dto.BookingRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationServiceTest {

    @Autowired
    ValidationService validationService;


    @Test
    void testValidBookingRequest() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("alpha room").date("14/05/2024").timeFrom("10:00").timeTo("11:00").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Assertions.assertTrue(errors.isEmpty());
    }

    @Test
    void testBookingRequestMissingMandatoryProps() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Email is mandatory",
                "Date is mandatory",
                "Room is mandatory",
                "Time from is mandatory",
                "Time to is mandatory"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestBlankMandatoryProps() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email(" ").room("").date("").timeFrom(" ").timeTo("  ").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Email is mandatory",
                "Date is mandatory",
                "Room is mandatory",
                "Time from is mandatory",
                "Time to is mandatory"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestBlankInvalidDates() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("alpha room").date("x").timeFrom("x").timeTo("x").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Date must be in the format dd/MM/yyyy",
                "Time must be a valid time slot in the 24h format HH:mm"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestDateInThePast() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("alpha room").date("13/05/2020").timeFrom("10:00").timeTo("11:00").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Date cannot be in the past"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestTimeSlotsNotOneHour() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("alpha room").date("13/05/2024").timeFrom("10:30").timeTo("11:00").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "A room can be booked for at least 1 hour or consecutive multiples of 1 hour"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestTimeToLessThanTimeFrom() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("alpha room").date("13/05/2024").timeFrom("12:00").timeTo("11:00").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Time to must be greater than time from"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }

    @Test
    void testBookingRequestInvalidRoom() {
        BookingRequestDTO bookingRequestDTO = BookingRequestDTO.builder().email("email").room("xx").date("13/05/2024").timeFrom("10:00").timeTo("11:00").build();
        Set<String> errors = validationService.validateBookingRequest(bookingRequestDTO);

        Set<String> expectedErrors = Set.of(
                "Room must be one of [main conference room, alpha room, beta room]"
        );

        Assertions.assertEquals(expectedErrors, errors, "Expected errors do not match actual errors");
    }



}