package com.conferenceplanner.core.communications.loans.domain;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ann on 14/09/14.
 */
public class LoanExtension
{
    private Integer loanId;
    private BigDecimal interestRate;
    private BigDecimal interest;
    private Date startDate;
    private Date endDate;
    private Date submissionDate;

    public LoanExtension()
    {}

    public Integer getLoanId()
    {
        return loanId;
    }

    public BigDecimal getInterestRate()
    {
        return interestRate;
    }

    public BigDecimal getInterest()
    {
        return interest;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public Date getSubmissionDate()
    {
        return submissionDate;
    }

    public void setLoanId(Integer loanId)

    {
        this.loanId = loanId;
    }

    public void setSubmissionDate(Date submissionDate)
    {
        this.submissionDate = submissionDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setInterest(BigDecimal interest)
    {
        this.interest = interest;
    }

    public void setInterestRate(BigDecimal interestRate)
    {
        this.interestRate = interestRate;
    }
}
