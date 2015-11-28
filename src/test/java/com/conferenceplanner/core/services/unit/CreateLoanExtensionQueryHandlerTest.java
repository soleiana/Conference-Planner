package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.InjectMocksTest;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionResponse;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;
import com.conferenceplanner.core.domain.LoanExtensionFixture;
import com.conferenceplanner.core.domain.LoanFixture;
import com.conferenceplanner.core.repositories.LoanExtensionRepository;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.services.CreateLoanExtensionQueryHandler;
import com.conferenceplanner.core.services.common.Message;
import com.conferenceplanner.core.services.factories.LoanExtensionFactory;
import com.conferenceplanner.core.services.factories.LoanFactory;
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
public class CreateLoanExtensionQueryHandlerTest extends InjectMocksTest
{
    @InjectMocks
    private CreateLoanExtensionQueryHandler queryHandler;
    @Mock
    private LoanExtensionFactory loanExtensionFactory;
    @Mock
    private LoanFactory loanFactory;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LoanExtensionRepository loanExtensionRepository;

    private LoanExtension loanExtension;
    private Loan loan;
    private Integer loanId;

    @Before
    public void setUp()
    {
        loan = LoanFixture.standardLoan();
        loanExtension = LoanExtensionFixture.standardLoanExtension();
        loanId = 1;

        when(loanExtensionFactory.getNewLoanExtension(loanId)).thenReturn(loanExtension);
        when(loanFactory.getExtendedLoan(loanExtension)).thenReturn(loan);
    }

    @Test
    public void testExecute()
    {
        //Positive path of execution
        //Customer obtains an extension of the loan

        CreateLoanExtensionResponse expectedResponse =
                new CreateLoanExtensionResponse(true, Message.LOAN_EXTENSION_OBTAINED_MESSAGE);
        CreateLoanExtensionQuery query = new CreateLoanExtensionQuery(loanId);

        CreateLoanExtensionResponse response = queryHandler.execute(query);

        assertNotNull(response);
        assertEquals(expectedResponse.isCreated(), response.isCreated());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        verify(loanExtensionFactory, times(1)).getNewLoanExtension(loanId);
        verify(loanExtensionRepository, times(1)).create(loanExtension);
        verify(loanFactory, times(1)).getExtendedLoan(loanExtension);
        verify(loanRepository, times(1)).update(loan);
    }
}
