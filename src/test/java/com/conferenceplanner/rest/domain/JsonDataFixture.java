package com.conferenceplanner.rest.domain;

/**
 * Created by Ann on 16/09/14.
 */
public class JsonDataFixture
{
    public static String standardLoanRequestJSON()
    {
        return "{\"customerId\": 2, \"requestIp\": null, \"term\": 30, \"amount\" : 200}";
    }

    public static String standardAllLoansResponseJSON()
    {
       String result =
               "{\"customerId\":2,\"name\":\"James\",\"surname\":\"Bond\","+
                 "\"loans\":" +
                       "[{" +
                       "\"id\":5," +
                       "\"currInterestRate\":150," +
                       "\"currInterest\":600," +
                       "\"amount\":500," +
                       "\"startDate\":\"2014-09-01\"," +
                       "\"endDate\":\"2014-09-20\"," +
                       "\"loanExtensions\":" +
                                 "[{" +
                                    "\"loanId\":5," +
                                    "\"interestRate\":150," +
                                    "\"interest\":600," +
                                    "\"startDate\":\"2014-09-10\"," +
                                    "\"endDate\":\"2014-09-20\"," +
                                    "\"submissionDate\":\"2014-09-10\"" +
                                 "}]" +
                       "}]" +
               "}";
        return  result;

    }

}
