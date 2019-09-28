package org.kd.main.server.rest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kd.main.common.RestUtility;
import org.kd.main.common.TraderConfig;
import org.kd.main.common.entities.Bank;
import org.kd.main.server.TraderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {TraderServer.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Import(TraderConfig.class)
public class BankEndpointTest {

    @Autowired
    private RestUtility restUtility;

    @Test
    public void testUpdateBank() {
        TraderServer.start();
        var contentType = "application/json";
        var requestUrl = "http://localhost:8080/bank";
        var testBank = new Bank(2, "Test Bank", "TST-B");

        Gson gsonBuilder = new GsonBuilder().create();
        String bankJson = gsonBuilder.toJson(testBank);

        var response = restUtility.processHttpRequest(HttpMethod.PUT, bankJson, requestUrl, contentType);

        restUtility.retrieveResponseBodyAndStatusCode(response);

        assertEquals("Have you started TraderServer? ", "200", restUtility.getResponseStatusCode());
        assertThat("Wrong response:" + restUtility.getResponseBody(), restUtility.getResponseBody(), containsString(bankJson));
    }
}