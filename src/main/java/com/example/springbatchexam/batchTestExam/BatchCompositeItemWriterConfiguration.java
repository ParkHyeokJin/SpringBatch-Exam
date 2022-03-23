package com.example.springbatchexam.batchTestExam;

import com.example.springbatchexam.domain.Menu;
import com.example.springbatchexam.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
public class BatchCompositeItemWriterConfiguration {
    public static final int CHUNK_SIZE = 10;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job batchCompositeExamJob(){
        return jobBuilderFactory.get("batchCompositeExamJob")
                .start(batchCompositeExamStep())
                .build();
    }

    @Bean
    public Step batchCompositeExamStep() {
        return stepBuilderFactory.get("batchCompositeExamStep")
                .<Menu, Menu> chunk(CHUNK_SIZE)
                .reader(batchCompositeItemReader())
                .processor(batchCompositeItemProcessor())
                .writer(batchCompositeWriterExam1())
                .build();
    }

    @Bean
    public ItemProcessor<Menu, Menu> batchCompositeItemProcessor() {
        return menu -> {
            menu.success();
            return menu;
        };
    }

    @Bean
    public CompositeItemWriter<Menu> batchCompositeWriterExam1(){
        CompositeItemWriter<Menu> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(batchCompositeUpdate1(), updateItems()));
        return compositeItemWriter;
    }

    @Bean
    public ItemWriter<Menu> batchCompositeUpdate1() {
        return items -> {
            for (Menu item : items) {
                log.info("다른 서비스 update 처리");
                log.info("item : {}", item.getItem());
            }
        };
    }

    public JpaItemWriter<Menu> updateItems() {
        JpaItemWriter<Menu> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public JpaPagingItemReader<Menu> batchCompositeItemReader() {
        return new JpaPagingItemReaderBuilder<Menu>()
                .queryString("SELECT m FROM Menu m WHERE status is false ")
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .name("batchCompositeItemReader")
                .build();
    }
}
