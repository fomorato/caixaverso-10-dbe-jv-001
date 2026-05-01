package com.ada.dto.request;

import com.ada.domain.enums.TipoAmortizacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class EmprestimoRequest {

    @NotNull
    public UUID clienteId;

    @NotNull
    @DecimalMin(value = "100", inclusive = true)
    @DecimalMax(value = "10000000", inclusive = true)
    public BigDecimal valorTotal;

    @NotNull
    @Min(1)
    @Max(480)
    public Integer quantidadeParcelas;

    @NotNull
    public TipoAmortizacao tipoAmortizacao;
}
