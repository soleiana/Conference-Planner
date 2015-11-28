package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionResponse;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.*;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import com.conferenceplanner.core.services.CreateLoanExtensionQueryHandler;
import com.conferenceplanner.core.services.common.Message;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by Ann on 02/10/14.
 */
public class CreateLoanExtensionQueryHandlerTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    @Autowired
    private LoanExtensionRepository loanExtensionRepository;
    @Autowired
    private CreateLoanExtensionQueryHandler createLoanExtensionQueryHandler;

    private BankParams bankParams;
    private Customer customer;
    private LoanRequest loanRequest;
    private Loan loan;

    private CreateLoanExtensionQuery createCreateLoanExtensionQuery()
    {
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loan = LoanFixture.standardLoan();

        loanRequest.setCustomer(customer);
        loanRequestRepository.create(loanRequest);
        loan.setCustomer(customer);
        loan.setLoanRequest(loanRequest);
        loanRepository.create(loan);
        return new CreateLoanExtensionQuery(loan.getId());
    }

    @Before
    public void setUp()
    {
        dbCleaner.clear();

        bankParams = BankParamsFixture.standardBankParams();
        bankParamsRepository.create(bankParams);
        customer = CustomerFixture.standardCustomer();
        customerRepository.create(customer);
    }

    @Test
    @Transactional
    public void testExecute()
    {
        CreateLoanExtensionQuery createLoanExtensionQuery = createCreateLoanExtensionQuery();
        CreateLoanExtensionResponse expectedResponse =
                new CreateLoanExtensionResponse(true, Message.LOAN_EXTENSION_OBTAINED_MESSAGE);

        LoanExtension loanExtension = loanExtensionRepository.getLast();
        assertNull(loanExtension);

        CreateLoanExtensionResponse createLoanExtensionResponse =
                createLoanExtensionQueryHandler.execute(createLoanExtensionQuery);

        assertEquals(expectedResponse.isCreated(), createLoanExtensionResponse.isCreated());
        assertEquals(expectedResponse.getMessage(), createLoanExtensionResponse.getMessage());

        loanExtension = loanExtensionRepository.getLast();
        assertNotNull(loanExtension);
        assertEquals(loan.getId(), loanExtension.getLoan().getId());

    }
}
