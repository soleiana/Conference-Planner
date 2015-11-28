package com.conferenceplanner.core.domain;

/**
 * Created by Ann on 10/09/14.
 */
public class CustomerFixture
{
    private static final String NAME = "James";
    private static final String SURNAME = "Bond";

    public static Customer standardCustomer()
    {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setSurname(SURNAME);
        return  customer;
    }
}
