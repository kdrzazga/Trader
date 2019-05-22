package org.kd.main.common.model.data;

import org.kd.main.common.entities.Customer;
import org.kd.main.common.entities.Bank;
import org.kd.main.common.entities.Transfer;

import java.util.List;

public interface DataService {

    void initApplication();

    /**
     * Only optionally applicable to in memory databases
     */
    void saveDb();

    List<Customer> loadCustomers();

    List<Bank> loadBanks();

    List<Transfer> loadTransfers();

    void saveBank(Bank bank);

    Customer loadCustomer(long id);

    void saveCustomer(Customer customer);

    Bank loadBank(long id);
}
