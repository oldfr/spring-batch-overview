package com.example.springbatchplayground.config;

import com.example.springbatchplayground.model.Customer;
import com.example.springbatchplayground.steps.CustomerItemProcessor;
import com.example.springbatchplayground.steps.CustomerItemReader;
import com.example.springbatchplayground.steps.CustomerItemWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Configuration
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean("firstBatchJob")
    public Job customerReportJob() throws Exception {
        return jobBuilders.get("customerReportJob")
                .start(taskletStep("inputCustomerList.json"))
                .next(chunkStep())
                .build();
    }

    @Bean
    public Step taskletStep(String fileName) {
        return stepBuilders.get("taskletStep")
                .tasklet(tasklet(fileName))
                .build();
    }

    @Bean
    public Tasklet tasklet(String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println("Hello from tasklet");
            List<Customer> customers = Arrays.asList(new ObjectMapper().readValue(new ClassPathResource(fileName).getFile(), Customer[].class));
            Collections.sort(customers, Comparator.comparing(Customer::getId));
            File file = new File("src/main/resources/sortedCustomerList.json");
            if(!file.exists()){
                System.out.println("FILE DOES NOT EXISTS. CREATED NOW");
                file.createNewFile();
            }
            System.out.println("after sorting:"+customers);
            Files.writeString(Path.of(file.getPath()),new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(customers));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Step chunkStep() throws Exception {
        return stepBuilders.get("chunkStep")
                .<Customer, Customer>chunk(3)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.parseInt("100"))
                .stream(dupItemWriter())
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Customer> reader() throws IOException {
        return new CustomerItemReader("sortedCustomerList.json");
    }

    @StepScope
    @Bean
    public ItemWriter<Customer> writer() throws Exception {
        return new CustomerItemWriter();
    }

    @StepScope
    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        final CompositeItemProcessor<Customer, Customer> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(new CustomerItemProcessor()));
        return processor;
    }

    @Bean(name = "duplicateItemWriter")
    public JsonFileItemWriter<Customer> dupItemWriter(){

        return new JsonFileItemWriterBuilder<Customer>()
                .name("duplicateItemWriter")
                .resource(new FileSystemResource("src/main/resources/finalCustomerList.json"))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
//                .lineAggregator(new PassThroughLineAggregator<>())
                .append(false)
                .shouldDeleteIfExists(true)
                .build();
    }
}
