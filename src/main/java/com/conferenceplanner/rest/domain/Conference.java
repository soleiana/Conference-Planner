package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class Conference {

    private int id;
    private String name;
    private String startDateTime;
    private String endDateTime;
    private List<Integer> conferenceRooms = new ArrayList<>();

    public Conference() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<Integer> getConferenceRooms() {
        return conferenceRooms;
    }

    public void setConferenceRooms(List<Integer> conferenceRooms) {
        this.conferenceRooms = conferenceRooms;
    }
}
