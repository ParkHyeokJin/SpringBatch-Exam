package com.example.springbatchexam.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MENU")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String item;
    private int price;
    private boolean status;

    public void success(){
        this.status = true;
    }
}
