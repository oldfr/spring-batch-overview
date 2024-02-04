package com.example.springbatchplayground.steps;

import com.example.springbatchplayground.model.Customer;
import org.springframework.batch.item.ItemWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

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
    public void close() throws IOException {

    }

    @Override
    public void write(List<? extends Customer> list) {
//        writer.write(list.toString());
        System.out.println("customer list:"+list);
    }
}
