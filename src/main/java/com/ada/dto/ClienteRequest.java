package com.ada.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteRequest {

    @NotBlank
    public String nome;

    @NotBlank
    public String documento;

    // Aceita "01001000" ou "01001-000"
    @NotBlank
    @Pattern(
            regexp = "\\d{5}-?\\d{3}",
            message = "CEP deve conter 8 dígitos e pode incluir hífen (ex.: 01001-000 ou 01001000)"
    )
    public String cep;

    // Não retornado pelo ViaCEP -> obrigatório para completar o endereço
    @NotBlank
    public String numero;

    // Opcional e sempre vem do usuário (NÃO usar o valor do ViaCEP)
    public String complemento;
}