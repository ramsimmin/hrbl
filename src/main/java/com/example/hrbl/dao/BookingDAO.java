package com.example.hrbl.dao;

import com.example.hrbl.dto.BookingDTO;
import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.dto.ResponseDTO;
import com.example.hrbl.entity.Booking;
import com.example.hrbl.mapper.BookingMapper;
import com.example.hrbl.repository.BookingRepository;
import com.example.hrbl.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingDAO {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommonUtils commonUtils;

    public ResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Booking bookingEntity = new Booking();
            bookingEntity.setId(UUID.randomUUID());
            bookingEntity.setEmail(bookingRequestDTO.getEmail());
            bookingEntity.setRoom(bookingRequestDTO.getRoom());
            bookingEntity.setTimeFrom(commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeFrom()));
            bookingEntity.setTimeTo(commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeTo()));
            bookingRepository.save(bookingEntity);

        } catch (Exception e) {
            responseDTO.setErrorMessages(Set.of("Unable to book meeting room: " + e.getMessage()));
        }
        return responseDTO;
    }


    public List<BookingDTO> getBookings() {
        return bookingMapper.entitiesToDtos(bookingRepository.findAllByOrderByTimeFromAscTimeFromAsc());
    }

    public boolean slotIsFree(BookingRequestDTO bookingRequestDTO) {
        try {
            LocalDateTime ldtFrom = commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeFrom());
            LocalDateTime ldtTo = commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeTo());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime dateStart = LocalDate.parse(bookingRequestDTO.getDate(), formatter).atTime(0, 0);
            LocalDateTime dateEnd = LocalDate.parse(bookingRequestDTO.getDate(), formatter).atTime(23, 59);
            List<Booking> bookings = bookingRepository.findAllByRoomAndTimeFromGreaterThanEqualAndTimeToLessThanEqual(bookingRequestDTO.getRoom(), dateStart, dateEnd);

            List<Booking> overlappingBookings = new ArrayList<>();

            bookings.forEach(booking -> {
                        if ((ldtFrom.isBefore(booking.getTimeTo())) && (booking.getTimeFrom().isBefore(ldtTo))) {
                            overlappingBookings.add(booking);
                        }
                    }
            );
            return overlappingBookings.isEmpty();
        } catch (ParseException e) {
            log.error("Unable to check overlapping slots " + e.getMessage());
            return false;
        }

    }


    public ResponseDTO delete(String bookingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<Booking> booking = bookingRepository.findById(UUID.fromString(bookingId));
            if (booking.isPresent()) {
                if (bookingIsInThePast(booking.get())) {
                    responseDTO.setErrorMessages(Set.of("Cannot delete a booking that exists in the past"));
                    return responseDTO;
                } else {
                    bookingRepository.deleteById(UUID.fromString(bookingId));
                }
            } else {
                responseDTO.setErrorMessages(Set.of("The given booking id does not exist"));
                return responseDTO;
            }

        } catch (Exception e) {
            responseDTO.setErrorMessages(Set.of("Unable to delete meeting: " + e.getMessage()));
            return responseDTO;
        }

        return responseDTO;

    }

    private boolean bookingIsInThePast(Booking booking) {
        return booking.getTimeFrom().isBefore(LocalDateTime.now());
    }


}
