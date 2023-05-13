package com.example.hrbl.mapper;

import com.example.hrbl.dto.BookingRequestDTO;
import com.example.hrbl.dto.BookingSearchResultDTO;
import com.example.hrbl.entity.Booking;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookingMapper {
    List<BookingSearchResultDTO> entitiesToDtos(List<Booking> bookings);

    @Mapping(target = "id",  qualifiedByName = "uuidToString")
    @Mapping(source = "timeFrom", target = "date", qualifiedByName = "dateToString")
    @Mapping(source = "timeFrom", target = "timeFrom",  qualifiedByName = "timeToString")
    @Mapping(source = "timeTo", target = "timeTo",  qualifiedByName = "timeToString")
    BookingSearchResultDTO entityToDto(Booking booking);

    @Named("uuidToString")
    default String uuidToStringConverter(UUID uuid) {
        return uuid.toString();
    }

    @Named("timeToString")
    default String timeToStringConverter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDateTime = localDateTime.format(formatter);
        return formattedDateTime;
    }

    @Named("dateToString")
    default String localDatetimeToSimpleDateConverter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDateTime = localDateTime.format(formatter);
        return formattedDateTime;
    }



}