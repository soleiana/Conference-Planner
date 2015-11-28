package com.conferenceplanner.core.communications.loans.factories;

import com.conferenceplanner.core.domain.AllLoans;
import com.conferenceplanner.core.communications.loans.domain.LoanExtension;
import com.conferenceplanner.core.communications.loans.domain.AllLoansDetails;
import com.conferenceplanner.core.communications.loans.domain.Loan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 14/09/14.
 */

@Component
public class AllLoansDetailsFactory
{
    private Loan convert(com.conferenceplanner.core.domain.Loan fromLoan)
    {
        Loan toLoan = new Loan();
        toLoan.setId(fromLoan.getId());
        toLoan.setCurrInterestRate(fromLoan.getCurrInterestRate());
        toLoan.setCurrInterest(fromLoan.getCurrInterest());
        toLoan.setAmount(fromLoan.getLoanRequest().getAmount());
        toLoan.setStartDate(fromLoan.getStartDate());
        toLoan.setEndDate(fromLoan.getEndDate());
        return toLoan;
    }

    private LoanExtension convert(com.conferenceplanner.core.domain.LoanExtension fromLoanExtension)
    {
        LoanExtension toLoanExtension = new LoanExtension();
        toLoanExtension.setLoanId(fromLoanExtension.getLoan().getId());
        toLoanExtension.setInterestRate(fromLoanExtension.getInterestRate());
        toLoanExtension.setInterest(fromLoanExtension.getInterest());
        toLoanExtension.setStartDate(fromLoanExtension.getStartDate());
        toLoanExtension.setEndDate(fromLoanExtension.getEndDate());
        toLoanExtension.setSubmissionDate(fromLoanExtension.getSubmissionDate());
        return  toLoanExtension;
    }


    public AllLoansDetails getNewAllLoansDetails(AllLoans allLoans)
    {
        AllLoansDetails allLoansDetails = new AllLoansDetails();
        allLoansDetails.setCustomerId(allLoans.getCustomer().getId());
        allLoansDetails.setName(allLoans.getCustomer().getName());
        allLoansDetails.setSurname(allLoans.getCustomer().getSurname());

        List<Loan> toLoans = new ArrayList<>();
        for(com.conferenceplanner.core.domain.Loan fromLoan: allLoans.getLoans())
        {
            Loan toLoan = convert(fromLoan);
            List<LoanExtension> toLoanExtensions = new ArrayList<>();

            for(com.conferenceplanner.core.domain.LoanExtension fromLoanExtension:
                    fromLoan.getLoanExtensions())
                {

                    LoanExtension toLoanExtension = convert(fromLoanExtension);
                    toLoanExtensions.add(toLoanExtension);
                }
            toLoan.setLoanExtensions(toLoanExtensions);

            toLoans.add(toLoan);
        }
        allLoansDetails.setLoans(toLoans);
        return allLoansDetails;
    }
}
