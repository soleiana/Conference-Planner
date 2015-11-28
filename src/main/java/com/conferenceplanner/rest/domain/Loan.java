package com.conferenceplanner.rest.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 14/09/14.
 */
public class Loan
{
    private Integer id;
    private BigDecimal currInterestRate;
    private BigDecimal currInterest;
    private BigDecimal amount;
    private Date startDate;
    private Date endDate;
    private List<LoanExtension> loanExtensions = new ArrayList<>();

    public Loan()
    {}

    public BigDecimal getCurrInterestRate() {
        return currInterestRate;
    }

    public BigDecimal getCurrInterest() {
        return currInterest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<LoanExtension> getLoanExtensions() {
        return loanExtensions;
    }

    public void setLoanExtensions(List<LoanExtension> loanExtensions) {
        this.loanExtensions = loanExtensions;
    }

    public void setCurrInterestRate(BigDecimal currInterestRate) {
        this.currInterestRate = currInterestRate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrInterest(BigDecimal currInterest) {
        this.currInterest = currInterest;
    }
}
