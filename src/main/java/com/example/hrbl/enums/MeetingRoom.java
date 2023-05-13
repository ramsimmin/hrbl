package com.example.hrbl.enums;

public enum MeetingRoom {
    MAIN_CONFERENCE_ROOM("main conference room"),
    ALPHA_ROOM("alpha room"),
    BETA_ROOM("beta room");

    String room;

    MeetingRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return this.room;
    }
}
