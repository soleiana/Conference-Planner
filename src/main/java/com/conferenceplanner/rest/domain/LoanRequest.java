package com.conferenceplanner.rest.domain;

import java.math.BigDecimal;

/**
 * Created by Ann on 07/09/14.
 */
public class LoanRequest
{
    private Integer customerId;
    private String requestIp;
    private Integer term;
    private BigDecimal amount;

    public LoanRequest()
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

    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
