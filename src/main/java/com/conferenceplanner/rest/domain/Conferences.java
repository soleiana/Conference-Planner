package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class Conferences {

    private String errorMessage;
    private List<Conference> conferences = new ArrayList<>();

    public Conferences() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Conference> getConferences() {
        return conferences;
    }

    public void setConferences(List<Conference> conferences) {
        this.conferences = conferences;
    }
}
