package com.conferenceplanner.core.domain;

import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 06/09/14.
 */
@Entity
@Table(name="LOAN")
public class Loan
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="LOAN_REQUEST_ID", unique = true, nullable = false)
    private LoanRequest loanRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CUSTOMER_ID", nullable = false)
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name= "LOAN_ID")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REMOVE})
    private List<LoanExtension> loanExtensions = new ArrayList<>();

    @Column(name="CURRENT_INTEREST_RATE", precision = 10, scale = 2, nullable = false)
    private BigDecimal currInterestRate;

    @Column(name="CURRENT_INTEREST", precision = 10, scale = 2, nullable = false)
    private BigDecimal currInterest;

    @Column(name="START_DATE", nullable = false)
    private Date startDate;

    @Column(name="END_DATE", nullable = false)
    private Date endDate;

    public Loan()
    {}

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public LoanRequest getLoanRequest()
    {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest)
    {
        this.loanRequest = loanRequest;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public BigDecimal getCurrInterestRate()
    {
        return currInterestRate;
    }

    public void setCurrInterestRate(BigDecimal currInterestRate)
    {
        this.currInterestRate = currInterestRate;
    }

    public BigDecimal getCurrInterest()
    {
        return currInterest;
    }

    public void setCurrInterest(BigDecimal currInterest)
    {
        this.currInterest = currInterest;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public List<LoanExtension> getLoanExtensions()
    {
        return loanExtensions;
    }

    public void setLoanExtensions(List<LoanExtension> loanExtensions)
    {
        this.loanExtensions = loanExtensions;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;

        if (currInterest != null ? !currInterest.equals(loan.currInterest) : loan.currInterest != null) return false;
        if (currInterestRate != null ? !currInterestRate.equals(loan.currInterestRate) : loan.currInterestRate != null)
            return false;
        if (customer != null ? !customer.equals(loan.customer) : loan.customer != null) return false;
        if (endDate != null ? !endDate.equals(loan.endDate) : loan.endDate != null) return false;
        if (id != null ? !id.equals(loan.id) : loan.id != null) return false;
        if (loanExtensions != null ? !loanExtensions.equals(loan.loanExtensions) : loan.loanExtensions != null)
            return false;
        if (loanRequest != null ? !loanRequest.equals(loan.loanRequest) : loan.loanRequest != null) return false;
        if (startDate != null ? !startDate.equals(loan.startDate) : loan.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (loanRequest != null ? loanRequest.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (loanExtensions != null ? loanExtensions.hashCode() : 0);
        result = 31 * result + (currInterestRate != null ? currInterestRate.hashCode() : 0);
        result = 31 * result + (currInterest != null ? currInterest.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
