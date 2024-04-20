package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@StepScope
public class CustomerItemReader implements ItemReader<Customer> {

    private final String filename;

    private int nextCustomerIndex;
    private List<Customer> customers;

    public CustomerItemReader(final String filename) throws IOException {
        this.filename = filename;
      /*  File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }*/
        initialize();
    }

    private void initialize() throws IOException {
        customers = Arrays.asList(new ObjectMapper().readValue(new ClassPathResource(filename).getFile(), Customer[].class));
        nextCustomerIndex = 0;
    }

    @Override
    public Customer read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        Customer nextCustomer = null;

        if (nextCustomerIndex < customers.size()) {
            nextCustomer = (Customer) customers.get(nextCustomerIndex);
            nextCustomerIndex++;
        }
        else {
            nextCustomerIndex = 0;
        }

        return nextCustomer;
    }
}
