package com.ada.dto.response;

import com.ada.domain.entity.Cliente;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ClienteResponse(
        UUID id,
        String nome,
        String documento,
        EnderecoResponse endereco
) {
    public static ClienteResponse from(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .documento(cliente.getDocumento())
                .endereco(EnderecoResponse.from(cliente.getEndereco()))
                .build();
    }
}