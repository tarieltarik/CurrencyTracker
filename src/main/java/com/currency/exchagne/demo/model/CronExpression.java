package com.currency.exchagne.demo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cron_expression")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronExpression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="hour")
    private String hour;

    @Column(name = "minute")
    private String min;

    @Column(name = "second")
    private String second;
}
