package com.example.springbatchexam.repository;

import com.example.springbatchexam.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStatusTrue();
}
