package com.ada.dto;

import com.ada.model.Endereco;
import lombok.Builder;

@Builder
public record EnderecoResponse(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro
) {
    public static EnderecoResponse from(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoResponse.builder()
                .cep(endereco.getCep())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .build();
    }
}