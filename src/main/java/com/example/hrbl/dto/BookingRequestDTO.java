package com.example.hrbl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingRequestDTO {
    private String email;
    private String room;
    private String date;
    private String timeFrom;
    private String timeTo;
}
