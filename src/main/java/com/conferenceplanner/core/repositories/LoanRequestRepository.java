package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.LoanRequest;

import java.util.List;

/**
 * Created by Ann on 06/09/14.
 */
public interface LoanRequestRepository
{
    void create(LoanRequest loanRequest);

    List<LoanRequest> getByRequestIp(String requestIp);

    LoanRequest getLast();
}
