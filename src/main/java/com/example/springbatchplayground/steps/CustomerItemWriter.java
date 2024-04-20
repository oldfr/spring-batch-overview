package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Closeable;
import java.util.List;

@Component
public class CustomerItemWriter implements ItemWriter<Customer>, Closeable {

    @Autowired
    JsonFileItemWriter<Customer> writer;


    @Override
    public void close() {

    }

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        System.out.println("writing final customer list:"+list);
        writer.write(list);
        System.out.println("writing to final file completed");
    }
}
