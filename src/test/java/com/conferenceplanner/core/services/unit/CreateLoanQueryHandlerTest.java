package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.InjectMocksTest;
import com.conferenceplanner.core.communications.domain.LoanRequestDetailsFixture;
import com.conferenceplanner.core.communications.loans.CreateLoanQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanResponse;
import com.conferenceplanner.core.communications.loans.domain.*;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.repositories.LoanRequestRepository;
import com.conferenceplanner.core.services.CreateLoanQueryHandler;
import com.conferenceplanner.core.services.common.Message;
import com.conferenceplanner.core.services.factories.LoanFactory;
import com.conferenceplanner.core.services.factories.LoanRequestFactory;
import com.conferenceplanner.core.services.helpers.InputConstraintChecker;
import com.conferenceplanner.core.services.helpers.RiskConstraintChecker;
import com.conferenceplanner.core.services.helpers.CreditExpert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Ann on 02/10/14.
 */
public class CreateLoanQueryHandlerTest extends InjectMocksTest
{
    @InjectMocks
    private CreateLoanQueryHandler queryHandler;
    @Mock
    private LoanRequestFactory loanRequestFactory;
    @Mock
    private LoanFactory loanFactory;
    @Mock
    private CreditExpert creditExpert;
    @Mock
    private RiskConstraintChecker riskConstraintChecker;
    @Mock
    private InputConstraintChecker inputConstraintChecker;
    @Mock
    private LoanRequestRepository loanRequestRepository;
    @Mock
    private LoanRepository loanRepository;

    private LoanRequest loanRequest;
    private Loan loan;
    private LoanRequestDetails loanRequestDetails;

    @Before
    public void setUp()
    {
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loan = LoanFixture.standardLoan();
        loanRequestDetails = LoanRequestDetailsFixture.standardLoanRequestDetails();

        when(loanRequestFactory.getNewLoanRequest(any(LoanRequestDetails.class))).thenReturn(loanRequest);
        when(loanFactory.getNewLoan(any(LoanRequest.class))).thenReturn(loan);

    }

    @Test
    public void testExecute_1()
    {
         //Positive path of execution
        //Customer obtains a loan

        when(creditExpert.hasRisks(any(LoanRequest.class))).thenReturn(false);
        when(inputConstraintChecker.checkAmountConstraint(any(LoanRequest.class))).thenReturn(true);

        CreateLoanResponse expectedResponse = new CreateLoanResponse(true, Message.LOAN_OBTAINED_MESSAGE);
        CreateLoanQuery query = new CreateLoanQuery(loanRequestDetails);

        CreateLoanResponse response = queryHandler.execute(query);
        assertNotNull(response);
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.isCreated(), response.isCreated());
        verify(loanRequestFactory, times(1)).getNewLoanRequest(loanRequestDetails);
        verify(loanRequestRepository, times(1)).create(loanRequest);
        verify(inputConstraintChecker, times(1)).checkAmountConstraint(loanRequest);
        verify(creditExpert, times(1)).hasRisks(loanRequest);
        verify(loanFactory, times(1)).getNewLoan(loanRequest);
        verify(loanRepository, times(1)).create(loan);
    }

    @Test
    public void testExecute_2()
    {
        //Negative path of execution
        //Customer is refused a loan because of the risks surrounding the loan request

        when(creditExpert.hasRisks(any(LoanRequest.class))).thenReturn(true);
        when(inputConstraintChecker.checkAmountConstraint(any(LoanRequest.class))).thenReturn(true);

        CreateLoanResponse expectedResponse = new CreateLoanResponse(false, Message.LOAN_ERROR_MESSAGE);
        CreateLoanQuery query =  new CreateLoanQuery(loanRequestDetails);

        CreateLoanResponse response = queryHandler.execute(query);

        assertNotNull(response);
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.isCreated(), response.isCreated());
        verify(loanRequestFactory, times(1)).getNewLoanRequest(loanRequestDetails);
        verify(loanRequestRepository, times(1)).create(loanRequest);
        verify(inputConstraintChecker, times(1)).checkAmountConstraint(loanRequest);
        verify(creditExpert, times(1)).hasRisks(loanRequest);
        verify(loanFactory, times(0)).getNewLoan(loanRequest);
        verify(loanRepository, times(0)).create(loan);
    }

    @Test
    public void testExecute_3()
    {
        //Negative path of execution
        //Customer is refused a loan because the requested loan amount exceeds the maximum

        when(creditExpert.hasRisks(any(LoanRequest.class))).thenReturn(false);
        when(inputConstraintChecker.checkAmountConstraint(any(LoanRequest.class))).thenReturn(false);

        CreateLoanResponse expectedResponse = new CreateLoanResponse(false, Message.LOAN_ERROR_MESSAGE);
        CreateLoanQuery query =  new CreateLoanQuery(loanRequestDetails);

        CreateLoanResponse response = queryHandler.execute(query);

        assertNotNull(response);
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        assertEquals(expectedResponse.isCreated(), response.isCreated());
        verify(loanRequestFactory, times(1)).getNewLoanRequest(loanRequestDetails);
        verify(loanRequestRepository, times(1)).create(loanRequest);
        verify(inputConstraintChecker, times(1)).checkAmountConstraint(loanRequest);
        verify(creditExpert, times(0)).hasRisks(loanRequest);
        verify(loanFactory, times(0)).getNewLoan(loanRequest);
        verify(loanRepository, times(0)).create(loan);
    }
}
