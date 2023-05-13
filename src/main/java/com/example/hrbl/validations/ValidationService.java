package com.example.hrbl.validations;

import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.dto.BookingSearchDTO;
import com.example.hrbl.enums.MeetingRoom;
import com.example.hrbl.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private final CommonUtils commonUtils;

    public Set<String> validateBookingRequestDTO(BookingRequestDTO bookingRequestDTO) {
        Set<String> errorMessages = new HashSet<>();
        validateEmail(bookingRequestDTO.getEmail(), errorMessages);
        validateDate(bookingRequestDTO.getDate(), errorMessages);
        validateMeetingRoom(bookingRequestDTO.getRoom(), errorMessages);
        validateTime(bookingRequestDTO.getTimeFrom(), errorMessages, "Time from");
        validateTime(bookingRequestDTO.getTimeTo(), errorMessages, "Time to");
        validateTimeSlots(bookingRequestDTO, errorMessages);
        validateDateAndTimeFromNotExistInThePast(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeFrom(), errorMessages);
        return errorMessages;
    }

    public Set<String> validateBookingSearchDTO(BookingSearchDTO bookingSearchDTO) {
        Set<String> errorMessages = new HashSet<>();
        validateMeetingRoom(bookingSearchDTO.getRoom(), errorMessages);
        validateDate(bookingSearchDTO.getDate(), errorMessages );
        return errorMessages;
    }


    public void validateEmail(String email, Set<String> errorMessages) {
        if (StringUtils.isBlank(email)) {
            errorMessages.add("Email is mandatory");
        }
    }

    public void validateDate(String dateString, Set<String> errorMessages) {
        if (StringUtils.isNotBlank(dateString)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate.parse(dateString, formatter);
            } catch (DateTimeException e) {
                errorMessages.add("Date must be in the format dd/MM/yyyy");
            }
        } else {
            errorMessages.add("Date is mandatory");
        }
    }

    public void validateMeetingRoom(String meetingRoom, Set<String> errorMessages) {
        if (StringUtils.isNotBlank(meetingRoom)) {
            List<String> availableRooms = Arrays.stream(MeetingRoom.values()).map(MeetingRoom::getRoom).collect(Collectors.toList());
            if (!availableRooms.contains(meetingRoom)) {
                errorMessages.add("Room must be one of " + availableRooms);
            }
        } else {
            errorMessages.add("Room is mandatory");
        }
    }

    public void validateTime(String time, Set<String> errorMessages, String attribute) {
        if (StringUtils.isBlank(time)) {
            errorMessages.add(attribute + " is mandatory");
        } else {
            validateTimeFormat(time, errorMessages);
        }
    }

    public void validateTimeFormat(String timeSlot, Set<String> errorMessages) {
        try {
            LocalTime.parse(timeSlot);
        } catch (Exception e) {
            errorMessages.add("Time must be a valid time slot in the 24h format HH:mm");
        }
    }

    public void validateTimeSlots(BookingRequestDTO bookingRequestDTO, Set<String> errorMessages) {
        try {
            LocalDateTime from = commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeFrom());
            LocalDateTime to = commonUtils.getLocalDateTimeFromString(bookingRequestDTO.getDate(), bookingRequestDTO.getTimeTo());

            Duration duration = Duration.between(from, to);
            if (duration.isNegative() || duration.isZero()) {
                errorMessages.add("Time to must be greater than time from");
            }

            if (duration.toMinutesPart() % 60 != 0) {
                errorMessages.add("A room can be booked for at least 1 hour or consecutive multiples of 1 hour");
            }

        } catch (Exception e) {
            log.error("Unable to parse time slots");
        }
    }


    public void validateDateAndTimeFromNotExistInThePast(String dateString, String timeFromString, Set<String> errorMessages) {
        try {
            String dateAndTimeStr = dateString + " " + timeFromString;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime meetingLdt = LocalDateTime.parse(dateAndTimeStr, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (meetingLdt.isBefore(now)) {
                errorMessages.add("Date cannot be in the past");
            }
        } catch (Exception e) {
           log.error("Unable to validate date and time from existing in the past");
        }
    }


}
