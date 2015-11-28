package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Customer;
import com.conferenceplanner.core.domain.CustomerFixture;

import com.conferenceplanner.core.repositories.tools.DBCleaner;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ann on 10/09/14.
 */
public class CustomerRepositoryImplTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;

    @Before
    public void setUp()
    {
        dbCleaner.clear();
        customer = CustomerFixture.standardCustomer();
    }
    @Test
    @Transactional
    public void testCreate()
    {
        customerRepository.create(customer);
        assertNotNull(customer.getId());
    }

    @Test
    @Transactional
    public void testGetById()
    {
        customerRepository.create(customer);
        Integer id = customer.getId();
        assertEquals(customer, customerRepository.getById(id));
    }
}
