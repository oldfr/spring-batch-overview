package com.example.springbatchplayground.config;

import com.example.springbatchplayground.model.Customer;
import com.example.springbatchplayground.model.PremiumCustomer;
import com.example.springbatchplayground.steps.CustomerItemProcessor;
import com.example.springbatchplayground.steps.CustomerItemReader;
import com.example.springbatchplayground.steps.CustomerItemWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
//@DependsOn("mainConfig")
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean("firstBatchJob")
    public Job customerReportJob(@Qualifier("TaskletStep") Step taskletStep) throws Exception {
        return jobBuilders.get("customerReportJob")
                .start(taskletStep)
                .next(chunkStep())
                .build();
    }

    @Bean("TaskletStep")
    public Step taskletStep(@Qualifier("Tasklet") Tasklet tasklet) {
        return stepBuilders.get("taskletStep")
                .tasklet(tasklet)
                .build();
    }

    @Bean("JobParameters")
    public JobParameters getJobParameters() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("inputFileLocation", "inputCustomerList.json");
        jobParametersBuilder.addString("sortedFileLocation", "src/main/resources/sortedCustomerList.json");
        return jobParametersBuilder.toJobParameters();
    }

    @Bean("Tasklet")
    @StepScope
    public Tasklet tasklet(@Qualifier("JobParameters") JobParameters jobParameters) {
        return (contribution, chunkContext) -> {
            System.out.println("Starting tasklet step");
            List<Customer> customers = Arrays.asList(new ObjectMapper().readValue(new ClassPathResource(jobParameters.getParameters().get("inputFileLocation").toString()).getFile(), Customer[].class));
            Collections.sort(customers, Comparator.comparing(Customer::getId));
            File file = new File(jobParameters.getParameters().get("sortedFileLocation").toString());
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
                .<Customer, PremiumCustomer>chunk(3)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .stream(dupItemWriter())// stream of writer to open it before writing to it
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Customer> reader() throws IOException {
        return new CustomerItemReader("src/main/resources/sortedCustomerList.json");
    }

    @StepScope
    @Bean
    public ItemWriter<PremiumCustomer> writer() throws Exception {
        return new CustomerItemWriter();
    }

    @StepScope
    @Bean
    public ItemProcessor<Customer, PremiumCustomer> processor() {
        final CompositeItemProcessor<Customer, PremiumCustomer> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(new CustomerItemProcessor()));
        return processor;
    }

    @Bean(name = "duplicateItemWriter")
    public JsonFileItemWriter<PremiumCustomer> dupItemWriter(){
        return new JsonFileItemWriterBuilder<PremiumCustomer>()
                .name("duplicateItemWriter")
                .resource(new FileSystemResource("src/main/resources/finalCustomerList.json"))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .append(false)
                .shouldDeleteIfExists(true)
                .build();
    }
}
