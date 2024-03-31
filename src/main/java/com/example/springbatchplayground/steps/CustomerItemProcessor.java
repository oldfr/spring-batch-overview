package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        // process the customer
        Customer customerUpdated = new Customer("CUST_"+customer.getId(), customer.getName(),customer.getDepartment());
        return customerUpdated;
    }
}
