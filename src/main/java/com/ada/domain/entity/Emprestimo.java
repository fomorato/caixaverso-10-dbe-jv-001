package com.ada.domain.entity;

import com.ada.domain.enums.StatusEmprestimo;
import com.ada.domain.enums.TipoAmortizacao;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "emprestimos")
public class Emprestimo extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(nullable = false)
    public UUID clienteId;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorTotal;

    @Column(nullable = false)
    public Integer quantidadeParcelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TipoAmortizacao tipoAmortizacao;

    @Column
    public Integer taxaJurosMensal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StatusEmprestimo status = StatusEmprestimo.PENDENTE;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Parcela> parcelas = new ArrayList<>();

    public void adicionarParcela(Parcela p) {
        if (p == null) return;
        p.emprestimo = this;
        this.parcelas.add(p);
    }
}
