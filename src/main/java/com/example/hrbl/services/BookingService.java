package com.example.hrbl.services;

import com.example.hrbl.dao.BookingDAO;
import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.dto.BookingSearchDTO;
import com.example.hrbl.dto.ResponseDTO;
import com.example.hrbl.validations.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingDAO bookingDAO;
    private final ValidationService validationService;

    public ResponseEntity<?> getBookingsByRoomAndDate(BookingSearchDTO bookingSearchDTO) {
        Set<String> errorMessages = validationService.validateBookingSearchDTO(bookingSearchDTO);
        ResponseDTO responseDTO = new ResponseDTO();

        if (!errorMessages.isEmpty()) {
            responseDTO.setErrorMessages(errorMessages);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }

        return ResponseEntity.ok().body(bookingDAO.getBookingsByRoomAndDate(bookingSearchDTO.getDate(), bookingSearchDTO.getRoom()));
    }

    public ResponseEntity<ResponseDTO> bookMeeting(BookingRequestDTO bookingRequestDTO) {
        Set<String> errorMessages =  validationService.validateBookingRequestDTO(bookingRequestDTO);
        ResponseDTO responseDTO = new ResponseDTO();

        if (!errorMessages.isEmpty()) {
            responseDTO.setErrorMessages(errorMessages);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }

        if (!bookingDAO.slotIsFree(bookingRequestDTO) ) {
            responseDTO.setErrorMessages(Set.of("The requested time slot is already booked"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }

        responseDTO = bookingDAO.save(bookingRequestDTO);
        if (CollectionUtils.isEmpty(responseDTO.getErrorMessages())) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }


    public ResponseEntity<ResponseDTO> cancelBooking(String bookingId) {
       ResponseDTO responseDTO = bookingDAO.delete(bookingId);

        if (CollectionUtils.isEmpty(responseDTO.getErrorMessages())) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }
}
