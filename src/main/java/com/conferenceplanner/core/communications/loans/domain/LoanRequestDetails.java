package com.conferenceplanner.core.communications.loans.domain;

import java.math.BigDecimal;


/**
 * Created by Ann on 12/09/14.
 */
public class LoanRequestDetails
{
    private Integer customerId;
    private String requestIp;
    private Integer term;
    private BigDecimal amount;

    public LoanRequestDetails()
    {}

    public Integer getCustomerId()
    {
        return customerId;
    }

    public String getRequestIp()
    {
        return requestIp;
    }

    public Integer getTerm()
    {
        return term;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setCustomerId(Integer customerId)
    {
        this.customerId = customerId;
    }

    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }

    public void setTerm(Integer term)
    {
        this.term = term;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
