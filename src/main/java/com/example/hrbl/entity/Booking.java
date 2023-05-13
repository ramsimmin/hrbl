package com.example.hrbl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String room;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime timeFrom;

    @Column(nullable = false)
    private LocalDateTime timeTo;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

}
