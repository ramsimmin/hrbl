package com.example.hrbl.repository;

import com.example.hrbl.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
        List<Booking> findAllByRoomAndTimeFromGreaterThanEqualAndTimeToLessThanEqualOrderByTimeFromAscTimeFromAsc(String room, LocalDateTime start, LocalDateTime end);
//        List<Booking> findAllByOrderByTimeFromAscTimeFromAsc();
 }

