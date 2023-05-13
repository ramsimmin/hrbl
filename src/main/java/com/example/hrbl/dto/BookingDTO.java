package com.example.hrbl.dto;

import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String id;
    private String email;
    private String room;
    private String date;
    private String timeFrom;
    private String timeTo;
}
