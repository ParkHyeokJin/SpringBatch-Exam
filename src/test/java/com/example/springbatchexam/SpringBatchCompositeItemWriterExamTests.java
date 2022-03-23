package com.example.springbatchexam;

import com.example.springbatchexam.batchTestExam.BatchCompositeItemWriterConfiguration;
import com.example.springbatchexam.config.BatchTestConfig;
import com.example.springbatchexam.domain.Menu;
import com.example.springbatchexam.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {BatchCompositeItemWriterConfiguration.class, BatchTestConfig.class})
class SpringBatchCompositeItemWriterExamTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void beforeEach() throws Exception{}

    @AfterEach
    void afterEach() throws Exception{
        menuRepository.deleteAll();
    }

    @Test
    @DisplayName("메뉴를 조회 하여 두군대의 writer 를 사용 하는 방법")
    public void 메뉴를_조회_하여_두군대의_writer를_사용_하는_방법() throws Exception {
        //given
        menuRepository.save(Menu.builder().item("김밥").price(1000).build());
        menuRepository.save(Menu.builder().item("떡볶이").price(3000).build());
        menuRepository.save(Menu.builder().item("라면").price(1500).build());

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(BatchStatus.COMPLETED.toString());

        List<Menu> menuList = menuRepository.findByStatusTrue();
        assertThat(menuList.size()).isEqualTo(3);
    }
}
