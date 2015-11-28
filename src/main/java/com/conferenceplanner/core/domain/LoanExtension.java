package com.conferenceplanner.core.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ann on 06/09/14.
 */
@Entity
@Table(name="LOAN_EXTENSION")
public class LoanExtension
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="LOAN_ID", nullable = false)
    private Loan loan;

    @Column(name="START_DATE", nullable = false)
    private Date startDate;

    @Column(name="END_DATE", nullable = false)
    private Date endDate;

    @Column(name="INTEREST_RATE", precision = 10, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name="INTEREST", precision = 10, scale = 2, nullable = false)
    private BigDecimal interest;

    @Column(name="SUBMISSION_DATE", nullable = false)
    private Date submissionDate;

    public LoanExtension()
    {}

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Loan getLoan()
    {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate) {
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

    public BigDecimal getInterestRate()
    {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate)
    {
        this.interestRate = interestRate;
    }

    public BigDecimal getInterest()
    {
        return interest;
    }

    public void setInterest(BigDecimal interest)
    {
        this.interest = interest;
    }

    public Date getSubmissionDate()
    {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate)
    {
        this.submissionDate = submissionDate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanExtension that = (LoanExtension) o;

        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (interest != null ? !interest.equals(that.interest) : that.interest != null) return false;
        if (interestRate != null ? !interestRate.equals(that.interestRate) : that.interestRate != null) return false;
        if (loan != null ? !loan.equals(that.loan) : that.loan != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (submissionDate != null ? !submissionDate.equals(that.submissionDate) : that.submissionDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (loan != null ? loan.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (interestRate != null ? interestRate.hashCode() : 0);
        result = 31 * result + (interest != null ? interest.hashCode() : 0);
        result = 31 * result + (submissionDate != null ? submissionDate.hashCode() : 0);
        return result;
    }
}
