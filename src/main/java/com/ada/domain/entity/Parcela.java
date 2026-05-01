package com.ada.domain.entity;

import com.ada.domain.enums.StatusParcela;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "parcelas")
public class Parcela extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(nullable = false)
    public Integer ordem;

    @Column(nullable = false)
    public LocalDate dataVencimento;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorAmortizacao;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorJuros;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorPrestacao;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal saldoDevedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StatusParcela status = StatusParcela.PENDENTE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "emprestimo_id", nullable = false)
    public Emprestimo emprestimo;
}
