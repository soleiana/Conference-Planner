package com.conferenceplanner.core.domain;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CONFERENCE_ROOM")
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "MAX_SEATS", nullable = false)
    private int maxSeats;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFERENCE_ROOM_ID")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems = new ArrayList<>();


    public ConferenceRoom() {}

    public ConferenceRoom(String name, String location, int maxSeats) {
        this.name = name;
        this.location = location;
        this.maxSeats = maxSeats;
    }

    public void addConferenceRoomAvailabilityItem(ConferenceRoomAvailabilityItem item) {
        getConferenceRoomAvailabilityItems().add(item);
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems() {
        return conferenceRoomAvailabilityItems;
    }

    public void setConferenceRoomAvailabilityItems(List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems) {
        this.conferenceRoomAvailabilityItems = conferenceRoomAvailabilityItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConferenceRoom that = (ConferenceRoom) o;

        if (maxSeats != that.maxSeats) return false;
        if (!name.equals(that.name)) return false;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + maxSeats;
        return result;
    }
}
