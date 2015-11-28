package com.conferenceplanner.core.services.factories;

import com.conferenceplanner.core.domain.AllLoans;
import com.conferenceplanner.core.domain.Customer;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;
import com.conferenceplanner.core.repositories.CustomerRepository;
import com.conferenceplanner.core.repositories.LoanExtensionRepository;
import com.conferenceplanner.core.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Ann on 14/09/14.
 */
@Component
public class AllLoansCoreFactory

{
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LoanExtensionRepository loanExtensionRepository;

    public AllLoans getNewAllLoans(Integer customerId)
    {
        AllLoans allLoans = new AllLoans();
        Customer customer = customerRepository.getById(customerId);
        allLoans.setCustomer(customer);
        List<Loan> loans = loanRepository.getByCustomer(customer);
        for(Loan loan: loans)
            {
                List<LoanExtension> loanExtensions = loanExtensionRepository.getByLoan(loan);
                loan.setLoanExtensions(loanExtensions);
            }
        allLoans.setLoans(loans);
        return  allLoans;
    }
}
