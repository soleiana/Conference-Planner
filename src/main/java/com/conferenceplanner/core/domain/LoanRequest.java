package com.conferenceplanner.core.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by Ann on 06/09/14.
 */
@Entity
@Table(name="LOAN_REQUEST")
public class LoanRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CUSTOMER_ID", nullable = false)
    private Customer customer;

    @Column(name="REQUEST_IP",  nullable = false)
    private String requestIp;

    @Column(name="SUBMISSION_DATE", nullable = false)
    private Date submissionDate;

    @Column(name="SUBMISSION_TIME", nullable = false)
    private Time submissionTime;

    @Column(name="TERM", nullable = false)
    private Integer term;

    @Column(name="AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name="STATUS", nullable = false)
    @Enumerated (EnumType.STRING)
    private LoanRequestStatus status;

    public LoanRequest()
    {}

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getRequestIp()
    {
        return requestIp;
    }

    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }

    public Date getSubmissionDate()
    {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate)
    {
        this.submissionDate = submissionDate;
    }

    public Time getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Time submissionTime)
    {
        this.submissionTime = submissionTime;
    }

    public Integer getTerm()
    {
        return term;
    }

    public void setTerm(Integer term)
    {
        this.term = term;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public LoanRequestStatus getStatus()
    {
        return status;
    }

    public void setStatus(LoanRequestStatus status)
    {
        this.status = status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanRequest that = (LoanRequest) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        if (submissionDate != null ? !submissionDate.equals(that.submissionDate) : that.submissionDate != null)
            return false;
        if (submissionTime != null ? !submissionTime.equals(that.submissionTime) : that.submissionTime != null)
            return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (submissionDate != null ? submissionDate.hashCode() : 0);
        result = 31 * result + (submissionTime != null ? submissionTime.hashCode() : 0);
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
