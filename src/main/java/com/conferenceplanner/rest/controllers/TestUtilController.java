package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/test-util")
public class TestUtilController {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @RequestMapping(method = RequestMethod.DELETE, value ="/clean-database", produces = "application/json")
    public ResponseEntity<String> cleanUpDatabase() {
        try {
            databaseCleaner.clear();

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Database cleaned up.", HttpStatus.OK);
    }
}
