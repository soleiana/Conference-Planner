package com.conferenceplanner.core.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;

/**
 * Created by Ann on 06/09/14.
 */
@Entity
@Table(name="BANK_PARAMS")
public class BankParams
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private Integer id;

    @Column(name="MAX_LOAN_AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal maxLoanAmount;

    @Column(name="BASE_INTEREST_RATE", precision = 10, scale = 2, nullable = false)
    private BigDecimal baseInterestRate;

    @Column(name="INTEREST_RATE_FACTOR", precision = 10, scale = 2, nullable = false)
    private BigDecimal interestRateFactor;

    @Column(name="MAX_LOAN_ATTEMPTS", nullable = false)
    private Byte maxLoanAttempts;

    @Column(name="RISK_TIME_START", nullable = false)
    private Time riskTimeStart;

    @Column(name="RISK_TIME_END", nullable = false)
    private Time riskTimeEnd;

    @Column(name="LOAN_EXTENSION_TERM", nullable = false)
    private Short loanExtensionTerm;

    public BankParams()
    {}

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public BigDecimal getMaxLoanAmount()
    {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(BigDecimal maxLoanAmount)
    {
        this.maxLoanAmount = maxLoanAmount;
    }

    public BigDecimal getBaseInterestRate()
    {
        return baseInterestRate;
    }

    public void setBaseInterestRate(BigDecimal baseInterestRate)
    {
        this.baseInterestRate = baseInterestRate;
    }

    public BigDecimal getInterestRateFactor()
    {
        return interestRateFactor;
    }

    public void setInterestRateFactor(BigDecimal interestRateFactor)
    {
        this.interestRateFactor = interestRateFactor;
    }

    public void setMaxLoanAttempts(Byte maxLoanAttempts)
    {
        this.maxLoanAttempts = maxLoanAttempts;
    }

    public Byte getMaxLoanAttempts()
    {
        return maxLoanAttempts;
    }

    public Time getRiskTimeStart()
    {
        return riskTimeStart;
    }

    public void setRiskTimeStart(Time riskTimeStart)
    {
        this.riskTimeStart = riskTimeStart;
    }

    public Time getRiskTimeEnd()
    {
        return riskTimeEnd;
    }

    public void setRiskTimeEnd(Time riskTimeEnd)
    {
        this.riskTimeEnd = riskTimeEnd;
    }

    public Short getLoanExtensionTerm()
    {
        return loanExtensionTerm;
    }

    public void setLoanExtensionTerm(Short loanExtensionTerm)
    {
        this.loanExtensionTerm = loanExtensionTerm;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankParams that = (BankParams) o;

        if (baseInterestRate != null ? !baseInterestRate.equals(that.baseInterestRate) : that.baseInterestRate != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (interestRateFactor != null ? !interestRateFactor.equals(that.interestRateFactor) : that.interestRateFactor != null)
            return false;
        if (loanExtensionTerm != null ? !loanExtensionTerm.equals(that.loanExtensionTerm) : that.loanExtensionTerm != null)
            return false;
        if (maxLoanAmount != null ? !maxLoanAmount.equals(that.maxLoanAmount) : that.maxLoanAmount != null)
            return false;
        if (maxLoanAttempts != null ? !maxLoanAttempts.equals(that.maxLoanAttempts) : that.maxLoanAttempts != null)
            return false;
        if (riskTimeEnd != null ? !riskTimeEnd.equals(that.riskTimeEnd) : that.riskTimeEnd != null) return false;
        if (riskTimeStart != null ? !riskTimeStart.equals(that.riskTimeStart) : that.riskTimeStart != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (maxLoanAmount != null ? maxLoanAmount.hashCode() : 0);
        result = 31 * result + (baseInterestRate != null ? baseInterestRate.hashCode() : 0);
        result = 31 * result + (interestRateFactor != null ? interestRateFactor.hashCode() : 0);
        result = 31 * result + (maxLoanAttempts != null ? maxLoanAttempts.hashCode() : 0);
        result = 31 * result + (riskTimeStart != null ? riskTimeStart.hashCode() : 0);
        result = 31 * result + (riskTimeEnd != null ? riskTimeEnd.hashCode() : 0);
        result = 31 * result + (loanExtensionTerm != null ? loanExtensionTerm.hashCode() : 0);
        return result;
    }
}
