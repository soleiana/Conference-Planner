package com.conferenceplanner.core.services;

import com.conferenceplanner.core.communications.loans.CreateLoanQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanResponse;
import com.conferenceplanner.core.communications.loans.domain.LoanRequestDetails;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanRequest;
import com.conferenceplanner.core.domain.LoanRequestStatus;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.repositories.LoanRequestRepository;
import com.conferenceplanner.core.services.common.Message;
import com.conferenceplanner.core.services.factories.LoanFactory;
import com.conferenceplanner.core.services.factories.LoanRequestFactory;
import com.conferenceplanner.core.services.helpers.InputConstraintChecker;
import com.conferenceplanner.core.services.helpers.CreditExpert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Ann on 02/10/14.
 */
@Component
public class CreateLoanQueryHandler
        implements QueryHandler<CreateLoanQuery, CreateLoanResponse>
 {
    @Autowired
    private LoanRequestFactory loanRequestFactory;
    @Autowired
    private LoanFactory loanFactory;
    @Autowired
    private CreditExpert creditExpert;
    @Autowired
    private InputConstraintChecker inputConstraintChecker;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public CreateLoanResponse execute(CreateLoanQuery query)
    {
        //Precondition: customer already exists in database

        Boolean isLoanObtained = false;
        LoanRequestDetails requestDetails = query.getLoanRequestDetails();

        LoanRequest loanRequest = loanRequestFactory.getNewLoanRequest(requestDetails);
        loanRequestRepository.create(loanRequest);

        if((!inputConstraintChecker.checkAmountConstraint(loanRequest))||
             creditExpert.hasRisks(loanRequest))
                loanRequest.setStatus(LoanRequestStatus.REJECTED);
        else
          {
             loanRequest.setStatus(LoanRequestStatus.APPROVED);
             Loan loan = loanFactory.getNewLoan(loanRequest);
             loanRepository.create(loan);
             isLoanObtained = true;
           }
        if (isLoanObtained)
            return new CreateLoanResponse(true, Message.LOAN_OBTAINED_MESSAGE);
        else
            return new CreateLoanResponse(false, Message.LOAN_ERROR_MESSAGE);
    }

    @Override
    public Class getQueryType()
    {
        return CreateLoanQuery.class;
    }
}
