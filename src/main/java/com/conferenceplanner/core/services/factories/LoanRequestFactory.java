package com.conferenceplanner.core.services.factories;

import com.conferenceplanner.core.domain.Customer;
import com.conferenceplanner.core.domain.LoanRequest;
import com.conferenceplanner.core.domain.LoanRequestStatus;
import com.conferenceplanner.core.communications.loans.domain.LoanRequestDetails;
import com.conferenceplanner.core.repositories.CustomerRepository;
import com.conferenceplanner.core.services.common.DateTimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Created by Ann on 13/09/14.
 */
@Component
public class LoanRequestFactory
{
    @Autowired
    CustomerRepository customerRepository;

    public LoanRequest getNewLoanRequest(LoanRequestDetails loanRequestDetails)
    {
        //We assume that customer already exists in DB
        Integer id = loanRequestDetails.getCustomerId();
        Customer customer = customerRepository.getById(id);

        LoanRequest loanRequest = new LoanRequest();

        loanRequest.setRequestIp(loanRequestDetails.getRequestIp());
        loanRequest.setCustomer(customer);
        loanRequest.setAmount(loanRequestDetails.getAmount());
        loanRequest.setTerm(loanRequestDetails.getTerm());
        loanRequest.setStatus(LoanRequestStatus.NEW);

        Date dNow = new Date();

        java.sql.Date submissionDate = DateTimeUtility.getSqlDate(dNow);
        java.sql.Time submissionTime = DateTimeUtility.getSqlTime(dNow);

        loanRequest.setSubmissionDate(submissionDate);
        loanRequest.setSubmissionTime(submissionTime);

        return loanRequest;
    }
}
