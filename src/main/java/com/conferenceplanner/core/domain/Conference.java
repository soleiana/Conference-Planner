package com.conferenceplanner.core.domain;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "CONFERENCE")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "START_DATE_TIME", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE_TIME", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime endDateTime;

    @Column(name = "CANCELLED", nullable = false)
    private boolean cancelled;

    @ManyToMany(mappedBy = "conferences")
    private List<ConferenceRoom> conferenceRooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CONFERENCE_PARTICIPANT",
            joinColumns = @JoinColumn(name = "CONFERENCE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PARTICIPANT_ID", referencedColumnName = "ID")
    )
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    public List<Participant> participants = new ArrayList<>();

    public Conference() {}

    public Conference(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean cancelled) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.cancelled = cancelled;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<ConferenceRoom> getConferenceRooms() {
        return conferenceRooms;
    }

    public void setConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        this.conferenceRooms = conferenceRooms;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conference that = (Conference) o;

        if (cancelled != that.cancelled) return false;
        if (!name.equals(that.name)) return false;
        if (!startDateTime.equals(that.startDateTime)) return false;
        return endDateTime.equals(that.endDateTime);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startDateTime.hashCode();
        result = 31 * result + endDateTime.hashCode();
        result = 31 * result + (cancelled ? 1 : 0);
        return result;
    }
}
