package org.kd.main.client.presenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kd.main.client.view.lib.PropertiesReader;
import org.kd.main.common.RestUtility;
import org.kd.main.common.TraderConfig;
import org.kd.main.common.entities.Account;
import org.kd.main.common.entities.Bank;
import org.kd.main.common.entities.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Import(TraderConfig.class)
public class TraderPresenter implements PresenterHandler {

    @Autowired
    private RestUtility restUtility;

    private final String port = new PropertiesReader().readKey("server_port");

    private static Long accountId = 2001L;

    private String serviceAddress = "http://localhost:" + port;
    private HttpMethod requestType;
    private String requestAsString;
    private String requestUrl;

    private final Logger log = LoggerFactory.getLogger(TraderPresenter.class);

    private final TypeReference<Bank> bankTypeReference = new TypeReference<>() {
    };
    private final TypeReference<Account> customerTypeReference = new TypeReference<>() {
    };
    private final TypeReference<List<Bank>> bankListTypeReference = new TypeReference<>() {
    };
    private final TypeReference<List<Account>> customerListTypeReference = new TypeReference<>() {
    };
    private final TypeReference<List<Transfer>> transferListTypeReference = new TypeReference<>() {
    };

    @Override
    public List<Bank> readBanks() {

        requestType = HttpMethod.GET;
        requestUrl = serviceAddress.concat("/banks");
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response == null) {
            log.error("REST error. Couldn't read banks from server.");
            return new Vector<>();
        }
        restUtility.retrieveResponseBodyAndStatusCode(response);
        List<Bank> banks;
        try {
            banks = new ObjectMapper()
                    .readValue(response.getBody()
                            , bankListTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new Vector<>();
        }

        return banks;
    }

    @Override
    public boolean createBank(String name, String shortname) {

        var contentType = APPLICATION_JSON_VALUE;
        var requestUrl = serviceAddress.concat("/bank");
        requestType = HttpMethod.POST;

        return saveBank(new Bank(name, shortname), contentType, requestUrl);
    }

    @Override
    public Optional<Bank> readBank(Long id) {

        requestType = HttpMethod.GET;
        requestUrl = serviceAddress.concat("/bank/").concat(id.toString());
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response == null) {
            log.error("REST error. Couldn't read bank with id={} from server.", id);
            return Optional.empty();
        }
        restUtility.retrieveResponseBodyAndStatusCode(response);
        Bank bank;
        try {
            bank = new ObjectMapper()
                    .readValue(response.getBody()
                            , bankTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(bank);
    }

    @Override
    public boolean updateBank(Bank bank) {

        var contentType = APPLICATION_JSON_VALUE;
        var requestUrl = serviceAddress.concat("/bank");
        requestType = HttpMethod.PUT;

        return saveBank(bank, contentType, requestUrl);
    }

    @Override
    public List<Account> readAccounts() {

        requestType = HttpMethod.GET;
        requestUrl = serviceAddress.concat("/corporate-accounts");
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response == null) {
            log.error("REST error. Couldn't read accounts from server.");
            return new Vector<>();
        }
        restUtility.retrieveResponseBodyAndStatusCode(response);
        List<Account> accounts;
        try {
            accounts = new ObjectMapper()
                    .readValue(response.getBody()
                            , customerListTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new Vector<>();
        }

        return accounts;
    }

    @Override
    public Optional<Account> readAccount(Long id) {

        requestType = HttpMethod.GET;
        requestUrl = serviceAddress.concat("/account/").concat(id.toString());
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response == null) {
            log.error("REST error. Couldn't read account with id={} from server.", id);
            return Optional.empty();
        }
        restUtility.retrieveResponseBodyAndStatusCode(response);
        Account account;
        try {
            account = new ObjectMapper()
                    .readValue(response.getBody()
                            , customerTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(account);
    }

    @Override
    public boolean updateAccount(Account account) {

        var contentType = APPLICATION_JSON_VALUE;
        var requestUrl = serviceAddress.concat("/account");

        try {
            String customerJson = new ObjectMapper().writeValueAsString(account);
            var response = restUtility.processHttpRequest(HttpMethod.PUT, customerJson, requestUrl, contentType);

            restUtility.retrieveResponseBodyAndStatusCode(response);
            if (!HttpStatus.OK.equals(response.getStatusCode()))
                log.error(restUtility.getErrorResponseStatusCode() + " " + restUtility.getErrorResponseBody());

            return HttpStatus.OK.equals(response.getStatusCode());
        } catch (JsonProcessingException e) {
            System.err.println("You provided shitty JSON, bro");
            return false;
        }
    }

    @Override
    public List<Transfer> readTransfers() {

        requestType = HttpMethod.GET;
        requestUrl = serviceAddress.concat("/transfers");
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response == null) {
            log.error("REST error. Couldn't read transfers from server.");
            return new Vector<>();
        }
        restUtility.retrieveResponseBodyAndStatusCode(response);
        List<Transfer> transfers;
        try {
            transfers = new ObjectMapper().readValue(response.getBody(), transferListTypeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new Vector<>();
        }

        return transfers;
    }

    @Override
    public boolean deleteTransfer(Long id) {

        requestType = HttpMethod.DELETE;
        requestUrl = serviceAddress.concat("/transfer/").concat(id.toString());
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        log.info(response.getBody());

        return HttpStatus.OK.equals(response.getStatusCode());
    }

    @Override
    public boolean deleteBank(Long id) {
        requestType = HttpMethod.DELETE;
        requestUrl = serviceAddress.concat("/bank/").concat(id.toString());
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        if (response.getBody() != null) log.info(response.getBody());

        return HttpStatus.OK
                .equals(response.getStatusCode());
    }

    @Override
    public boolean createAccount(String name, String shortname, Double units, Long bankId) {
        //TODO

        return false;
    }

    @Override
    public boolean deleteAccount(Long id) {

        requestType = HttpMethod.DELETE;
        requestUrl = serviceAddress.concat("/account/").concat(id.toString());
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        Optional.ofNullable(response.getBody()).ifPresent(log::info);

        return HttpStatus.OK.equals(response.getStatusCode());
    }

    @Override
    public boolean bookTransfer() {
        //TODO log.info("Book Transfer not finished");
        requestType = HttpMethod.POST;
        requestUrl = serviceAddress.concat("/transfer/");
        requestAsString = "";

        ResponseEntity<String> response = restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
        Optional.ofNullable(response.getBody()).ifPresent(log::info);

        return HttpStatus.OK
                .equals(response.getStatusCode());
    }

    @Override
    public void stopServer() {
        requestType = HttpMethod.POST;
        requestUrl = serviceAddress.concat("/stop");
        requestAsString = "";
        restUtility.processHttpRequest(requestType, requestAsString, requestUrl, APPLICATION_JSON_VALUE);
    }

    @Override
    public void initApplication() {
        serviceAddress = "http://localhost:" + port;
    }

    @Override
    public void saveDb() {
    }

    private boolean saveBank(Bank bank, String contentType, String requestUrl) {
        try {
            String bankJson = new ObjectMapper().writeValueAsString(bank);

            var response = restUtility.processHttpRequest(requestType, bankJson, requestUrl, contentType);
            if (response == null) {
                log.error("REST error. Couldn't save bank.");
            }
            restUtility.retrieveResponseBodyAndStatusCode(response);
            if (!HttpStatus.OK.equals(response.getStatusCode()))
                log.error(restUtility.getErrorResponseStatusCode() + " " + restUtility.getErrorResponseBody());

            return HttpStatus.OK.equals(response.getStatusCode());

        } catch (JsonProcessingException e) {
            System.err.println("You've given shitty JSON");
            return false;
        }
    }

    @Override
    public void setAccountId(Long accountId) {
        TraderPresenter.accountId = accountId;
    }

    @Override
    public Long getAccountId() {
        return accountId;
    }
}
