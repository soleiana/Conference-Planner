package com.conferenceplanner.core.domain;

import java.util.List;

/**
 * Created by Ann on 14/09/14.
 */

public class AllLoans
{
    private Customer customer;

    private List<Loan> loans;

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public List<Loan> getLoans()
    {
        return loans;
    }

    public void setLoans(List<Loan> loans)
    {
        this.loans = loans;
    }
}
