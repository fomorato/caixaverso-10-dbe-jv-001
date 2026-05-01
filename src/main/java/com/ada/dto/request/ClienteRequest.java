package com.ada.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteRequest {

    @NotBlank
    public String nome;

    @NotBlank
    public String documento;

    @NotBlank
    @Pattern(
            regexp = "\\d{5}-?\\d{3}",
            message = "CEP deve conter 8 dígitos e pode incluir hífen (ex.: 01001-000 ou 01001000)"
    )
    public String cep;

    @NotBlank
    public String numero;

    public String complemento;
}