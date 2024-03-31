package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

@Component
//public class CustomerItemWriter extends FlatFileItemWriter<Customer> implements Closeable {
public class CustomerItemWriter implements ItemWriter<Customer>, Closeable {


    /*
    private final PrintWriter writer;

    public CustomerItemWriter() {
        OutputStream out;
        try {
            out = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            out = System.out;
        }
        this.writer = new PrintWriter(out);
    }*/

    @Override
    public void close() {

    }

    @Override
    public void write(List<? extends Customer> list) throws Exception {
//        writer.write(list.toString());
        System.out.println("writing final customer list:"+list);
/*        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriterBuilder<Customer>()
                .name("itemWriter")
                .resource(new FileSystemResource("src/main/resources/finalCustomerList.json"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
        flatFileItemWriter.write(list);*/
    }
}
