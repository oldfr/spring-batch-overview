package com.example.springbatchplayground.config;

import com.example.springbatchplayground.steps.CustomerItemReader;
import com.example.springbatchplayground.steps.CustomerItemWriter;
import com.example.springbatchplayground.model.Customer;
import com.example.springbatchplayground.steps.CustomerItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.util.Arrays;

@Configuration
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean("firstBatchJob")
    public Job customerReportJob() throws Exception {
        return jobBuilders.get("customerReportJob")
                .start(taskletStep())
                .next(chunkStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return stepBuilders.get("taskletStep")
                .tasklet(tasklet())
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello from tasklet");
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
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Customer> reader() throws IOException {
        return new CustomerItemReader("test.json");
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
}
