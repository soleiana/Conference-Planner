package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;

import java.util.List;

/**
 * Created by Ann on 06/09/14.
 */
public interface LoanExtensionRepository
{
    void create(LoanExtension loanExtension);

    List<LoanExtension> getByLoan(Loan loan);

    LoanExtension getLast();
}
