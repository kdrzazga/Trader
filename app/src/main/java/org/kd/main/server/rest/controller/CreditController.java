package org.kd.main.server.rest.controller;

import org.kd.main.common.entities.Credit;
import org.kd.main.server.model.data.dao.CreditDaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
class CreditController {

    private final CreditDaoRepo creditDaoRepo;

    @Autowired
    public CreditController(CreditDaoRepo creditDaoRepo) {
        this.creditDaoRepo = creditDaoRepo;
    }

    @GetMapping(path = "/credit/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Credit> read(@PathVariable long id) {
        var credit = creditDaoRepo.read(id);

        return credit != null ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Access-Control-Allow-Origin", "*")
                        .body(credit)
                :
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .header("message", "Couldn't read " + Credit.class.getSimpleName() + " with id = " + id)
                        .build();
    }

    @GetMapping(path = "/credits")
    public ResponseEntity<List<Credit>> readAll() {
        var creditList = creditDaoRepo.readAll();

        return creditList != null ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Access-Control-Allow-Origin", "*")
                        .body(creditList)
                :
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("message", "Error reading list of " + Credit.class.getSimpleName())
                        .build();
    }

    @GetMapping(path = "/credits/{accountId}")
    public ResponseEntity<List<Credit>> readAccountCards(@PathVariable Long accountId) {
        var credits = creditDaoRepo.readAccountCredits(accountId);

        return credits != null ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(credits)
                :
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .header("message", "Error reading list of "
                                + Credit.class.getSimpleName() + "for account " + accountId)
                        .build();
    }
}
