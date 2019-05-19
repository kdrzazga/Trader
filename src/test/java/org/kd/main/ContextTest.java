package org.kd.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kd.main.server.TraderServer;
import org.kd.main.server.model.data.dao.BankDaoRepo;
import org.kd.main.server.model.data.dao.FundDaoRepo;
import org.kd.main.server.model.data.dao.TransferDaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = {TraderServer.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ContextTest {

    @Autowired
    private FundDaoRepo customerDao;

    @Autowired
    private BankDaoRepo bankDao;

    @Autowired
    private TransferDaoRepo transferDao;

    @Test
    public void contextSetup() {
        assertNotNull(customerDao);
        assertNotNull(bankDao);
        assertNotNull(transferDao);
    }
}