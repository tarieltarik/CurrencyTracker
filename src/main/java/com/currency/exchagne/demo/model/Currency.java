package com.currency.exchagne.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="currency")
public class Currency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code")
    private String code;

    @Column(name="exchanger_name")
    private String exchangerName;

    @Column(name="exchange_rate_value")
    private double exchangeRateValue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "exchange_rate")
    private ExchangeRate exchangeRate;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", exchangerName='" + exchangerName + '\'' +
                ", exchangeRateValue=" + exchangeRateValue +
                '}';
    }
}
