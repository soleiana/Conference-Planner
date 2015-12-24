package com.conferenceplanner.core.domain;

import javax.persistence.*;

@Entity
@Table(name = "CONFERENCE_ROOM_CONFERENCE")
public class ConferenceRoomAvailabilityItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "AVAILABLE_SEATS", nullable = false)
    private int availableSeats;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONFERENCE_ROOM_ID", nullable = false)
    private ConferenceRoom conferenceRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONFERENCE_ID", nullable = false)
    private Conference conference;

    public ConferenceRoomAvailabilityItem() {}

    public Integer getId() {
        return id;
    }

    public ConferenceRoomAvailabilityItem(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    public boolean relatesToEmptyConferenceRoom() {
        return availableSeats == conferenceRoom.getMaxSeats();
    }

    public void takeAvailableSeat() {
        if (availableSeats == 0) {
            throw new IllegalStateException("Seat can not be taken!");
        }
        this.availableSeats--;
    }

    public void releaseAvailableSeat() {
        if (availableSeats == conferenceRoom.getMaxSeats()) {
            throw new IllegalStateException("Seat can not be released!");
        }
        this.availableSeats++;
    }


    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConferenceRoomAvailabilityItem that = (ConferenceRoomAvailabilityItem) o;

        return availableSeats == that.availableSeats;
    }

    @Override
    public int hashCode() {
        return availableSeats;
    }
}
