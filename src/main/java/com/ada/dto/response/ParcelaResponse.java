package com.ada.dto.response;

import com.ada.domain.enums.StatusParcela;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ParcelaResponse {
    public UUID id;
    public Integer ordem;
    public LocalDate dataVencimento;
    public BigDecimal valorAmortizacao;
    public BigDecimal valorJuros;
    public BigDecimal valorPrestacao;
    public StatusParcela status;
    public BigDecimal saldoDevedor;
}
