package com.conferenceplanner.core.communications.domain;

import com.conferenceplanner.core.communications.loans.domain.AllLoansDetails;
import com.conferenceplanner.core.communications.loans.domain.Loan;
import com.conferenceplanner.core.communications.loans.domain.LoanExtension;
import com.conferenceplanner.core.domain.Customer;
import com.conferenceplanner.core.domain.CustomerFixture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 02/10/14.
 */
public class AllLoanDetailsFixture
{
    private static final Integer CUSTOMER_ID = 1;

    public static AllLoansDetails standardAllLoansDetails()
    {
        AllLoansDetails allLoansDetails = new AllLoansDetails();
        Customer customer = CustomerFixture.standardCustomer();
        Loan loan = LoanFixture.standardLoan();
        LoanExtension loanExtension = LoanExtensionFixture.standardLoanExtension();
        List<LoanExtension> loanExtensions = new ArrayList<>();
        loanExtensions.add(loanExtension);
        loan.setLoanExtensions(loanExtensions);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        allLoansDetails.setLoans(loans);
        allLoansDetails.setCustomerId(CUSTOMER_ID);
        allLoansDetails.setName(customer.getName());
        allLoansDetails.setSurname(customer.getSurname());

        return  allLoansDetails;
    }
}
