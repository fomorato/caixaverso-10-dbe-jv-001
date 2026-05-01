package com.ada.dto.response;

import com.ada.domain.enums.StatusEmprestimo;
import com.ada.domain.enums.TipoAmortizacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmprestimoResponse {
    public UUID id;
    public UUID clienteId;
    public BigDecimal valorTotal;
    public Integer quantidadeParcelas;
    public TipoAmortizacao tipoAmortizacao;
    public Integer taxaJurosMensal;
    public StatusEmprestimo status;
    public List<ParcelaResponse> parcelas = new ArrayList<>();
}
