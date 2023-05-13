package com.example.hrbl.controllers;

import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(value = "/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookings() {
        return ResponseEntity.ok(bookingService.getBookings());
    }

    @PostMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bookMeeting(@RequestBody BookingRequestDTO bookingRequestDTO) {
        return bookingService.bookMeeting(bookingRequestDTO);
    }

    @DeleteMapping(value = "/cancel/{booking_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelBooking(@PathVariable(name = "booking_id") String bookingId) {
        return bookingService.cancelBooking(bookingId);
    }


}
