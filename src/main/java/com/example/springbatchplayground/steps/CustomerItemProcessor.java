package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import com.example.springbatchplayground.model.PremiumCustomer;
import org.springframework.batch.item.ItemProcessor;
import java.util.UUID;

public class CustomerItemProcessor implements ItemProcessor<Customer, PremiumCustomer> {

    @Override
    public PremiumCustomer process(Customer customer) throws Exception {
        // process the customer
        return new PremiumCustomer(customer.getId(), UUID.randomUUID().toString(),"PREMIUM_"+customer.getName(), "VIP");
    }
}
