package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.communications.loans.GetAllLoansQuery;
import com.conferenceplanner.core.communications.loans.GetAllLoansResponse;
import com.conferenceplanner.core.communications.loans.domain.AllLoansDetails;
import com.conferenceplanner.core.services.QueryExecutor;
import com.conferenceplanner.rest.domain.AllLoans;
import com.conferenceplanner.rest.factories.AllLoansRestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by Ann on 16/09/14.
 */

@Controller
@RequestMapping("/customers")
public class LoanInfoController
{
    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private AllLoansRestFactory allLoansRestFactory;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/loans",
                    produces = "application/json")
    public ResponseEntity<AllLoans> requestAllLoans(@PathVariable Integer id)
    {
        GetAllLoansQuery getAllLoansQuery = new GetAllLoansQuery(id);

        GetAllLoansResponse getAllLoansResponse = queryExecutor.execute(getAllLoansQuery);
        AllLoans allLoans = null;
        if(getAllLoansResponse.isEntityFound())
        {
            AllLoansDetails allLoansDetails = getAllLoansResponse.getAllLoansDetails();
            allLoans = allLoansRestFactory.getNewAllLoans(allLoansDetails);
            return new ResponseEntity<>(allLoans, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(allLoans, HttpStatus.NOT_FOUND);
    }
}
