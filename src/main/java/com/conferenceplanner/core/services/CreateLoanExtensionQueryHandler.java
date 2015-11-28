package com.conferenceplanner.core.services;

import com.conferenceplanner.core.communications.loans.CreateLoanExtensionQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionResponse;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;
import com.conferenceplanner.core.repositories.LoanExtensionRepository;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.services.common.Message;
import com.conferenceplanner.core.services.factories.LoanExtensionFactory;
import com.conferenceplanner.core.services.factories.LoanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Ann on 02/10/14.
 */
@Component
public class CreateLoanExtensionQueryHandler
        implements QueryHandler<CreateLoanExtensionQuery, CreateLoanExtensionResponse>
{
    @Autowired
    private LoanFactory loanFactory;
    @Autowired
    private LoanExtensionFactory loanExtensionFactory;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanExtensionRepository loanExtensionRepository;

    @Override
    public CreateLoanExtensionResponse execute(CreateLoanExtensionQuery query)
    {
        //Precondition: customer already exists in database
        //Precondition: loan, subject to extension, exists in database

        Integer loanId = query.getLoanId();

        LoanExtension loanExtension = loanExtensionFactory.getNewLoanExtension(loanId);
        loanExtensionRepository.create(loanExtension);
        Loan extendedLoan = loanFactory.getExtendedLoan(loanExtension);
        loanRepository.update(extendedLoan);
        return new CreateLoanExtensionResponse(true, Message.LOAN_EXTENSION_OBTAINED_MESSAGE);
    }

    @Override
    public Class getQueryType()
    {
        return CreateLoanExtensionQuery.class;
    }
}
