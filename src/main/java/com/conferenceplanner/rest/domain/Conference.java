package com.conferenceplanner.rest.domain;


import java.util.List;

public class Conference {

    private Integer id;
    private String name;
    private String startDateTime;
    private String endDateTime;
    private List<Integer> conferenceRoomIds;

    public Conference() {}

    public Conference(String name, String startDateTime, String endDateTime) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<Integer> getConferenceRoomIds() {
        return conferenceRoomIds;
    }

    public void setConferenceRoomIds(List<Integer> conferenceRoomIds) {
        this.conferenceRoomIds = conferenceRoomIds;
    }

}
