package com.ada.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.*;


@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends PanacheEntity {

    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;

}