package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 14/09/14.
 */

public class AllLoans
{
    private Integer customerId;
    private String name;
    private String surname;
    private List<Loan> loans = new ArrayList<>();

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

}
