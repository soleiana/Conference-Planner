package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.communications.loans.CreateLoanExtensionQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanExtensionResponse;
import com.conferenceplanner.core.services.QueryExecutor;
import com.conferenceplanner.rest.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by Ann on 06/09/14.
 */
@Controller
@RequestMapping("/loans/{id}/extensions")
public class LoanExtensionController
{
    @Autowired
    private QueryExecutor queryExecutor;

    private boolean validate(Integer loanId)
    {
        if (loanId == null)
            return  false;
        if (loanId <= 0)
            return  false;
        return  true;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> createLoanExtension(@PathVariable Integer id)
    {
        if (!validate(id))
            return new ResponseEntity<>(Message.INVALID_INPUT_FORMAT,
                    HttpStatus.BAD_REQUEST);

        CreateLoanExtensionQuery createLoanExtensionQuery =
                new CreateLoanExtensionQuery(id);

        CreateLoanExtensionResponse createLoanExtensionResponse =
                queryExecutor.execute(createLoanExtensionQuery);
        String message = createLoanExtensionResponse.getMessage();

        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }
}
