package com.example.springbatchplayground;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableBatchProcessing
public class SpringBatchPlaygroundApplication {

	public static void main(String[] args) {
//		prepareTestData(1000);
		ConfigurableApplicationContext context = SpringApplication.run(SpringBatchPlaygroundApplication.class, args);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("firstBatchJob");
		System.out.println("Starting the batch job");
		try {
//			JobParameters jobParameters = new JobParametersBuilder().addDate("Date",new Date()).addString("inputFileLocation","TestJob").toJobParameters();
//			new JobParametersBuilder().addJobParameters(jobParameters);
			JobExecution execution = jobLauncher.run(job, context.getBean(JobParameters.class));
			System.out.println("Job Status : " + execution.getStatus());
			System.out.println("Job completed");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Job failed");
		}
	}

}
