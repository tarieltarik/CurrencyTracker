package com.currency.exchagne.demo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="date")
    private LocalDateTime dateTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exchangeRate")
    private List<Currency> currencies;
}
