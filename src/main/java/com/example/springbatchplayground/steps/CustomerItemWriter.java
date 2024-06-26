package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.PremiumCustomer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Closeable;
import java.util.List;

@Component
public class CustomerItemWriter implements ItemWriter<PremiumCustomer>, Closeable {

    @Autowired
    JsonFileItemWriter<PremiumCustomer> writer;


    @Override
    public void close() {

    }

    @Override
    public void write(List<? extends PremiumCustomer> list) throws Exception {
        System.out.println("writing final customer list:"+list);
        writer.write(list);
        System.out.println("writing to final file completed");
    }
}
