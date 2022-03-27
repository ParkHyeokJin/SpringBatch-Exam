package com.example.springbatchexam.batchTestExam;

import com.example.springbatchexam.domain.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
public class BatchTestConfiguration {
    public static final int CHUNK_SIZE = 10;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job batchTestJob(){
        return jobBuilderFactory.get("batchTestJob")
                .start(menuBatchStep())
                .build();
    }

    @Bean
    public Step menuBatchStep() {
        return stepBuilderFactory.get("menuBatchStep")
                .<Menu, Menu> chunk(CHUNK_SIZE)
                .reader(readItem())
                .processor(itemProcessor())
                .writer(updateItems())
                .build();
    }

    @Bean
    public ItemProcessor<Menu, Menu> itemProcessor() {
        return menu -> {
            menu.success();
            log.info("{}", menu);
            return menu;
        };
    }

    @Bean
    public JpaItemWriter<Menu> updateItems() {
        JpaItemWriter<Menu> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public JpaPagingItemReader<Menu> readItem() {
        return new JpaPagingItemReaderBuilder<Menu>()
                .queryString("SELECT m FROM Menu m WHERE status is false ")
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .name("menuReader")
                .build();
    }
}
