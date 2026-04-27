package com.ada.service;

import com.ada.client.ViaCepRestClient;
import com.ada.dto.ClienteRequest;
import com.ada.dto.ViaCepResponse;
import com.ada.model.Cliente;
import com.ada.model.Endereco;
import com.ada.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ClienteService {

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    @RestClient
    private ViaCepRestClient cepRestClient;

    @Transactional
    public Cliente criar(ClienteRequest req) {

        // Regra: documento único
        if (clienteRepository.findByDocumento(req.documento).isPresent()) {
            throw new WebApplicationException("Documento já cadastrado.", Response.Status.CONFLICT);
        }

        // Normaliza CEP: aceita "01001-000" e "01001000"
        String cep = normalizeCep(req.cep);

        // Consulta ViaCEP
        ViaCepResponse via;
        try {
            via = cepRestClient.getByCep(cep);
        } catch (ProcessingException e) {
            throw new WebApplicationException("Falha ao consultar serviço de CEP.", Response.Status.BAD_GATEWAY);
        }

        if (via == null || Boolean.TRUE.equals(via.getErro())) {
            throw new WebApplicationException("CEP inválido ou não encontrado.", Response.Status.BAD_REQUEST);
        }


        // complemento sempre do usuário; se vier em branco, salva null
        String complementoUsuario = (req.complemento != null && !req.complemento.isBlank())
            ? req.complemento
            : null;

        Endereco endereco = Endereco.builder()
            .cep(via.getCep())
            .logradouro(via.getLogradouro())
            .bairro(via.getBairro())
            .numero(req.numero)
            .complemento(complementoUsuario) // <-- NUNCA via.getComplemento()
            .build();

        // Monta cliente
        Cliente c = new Cliente();
        c.setNome(req.nome);
        c.setDocumento(req.documento);
        c.setEndereco(endereco);

        clienteRepository.persist(c);
        return c;
    }

    public List<Cliente> listar() {
        return clienteRepository.listAll();
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Cliente não encontrado.", Response.Status.NOT_FOUND));
    }

    public Cliente buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento)
                .orElseThrow(() -> new WebApplicationException("Cliente não encontrado.", Response.Status.NOT_FOUND));
    }

    @Transactional
    public Cliente atualizar(UUID id, ClienteRequest req) {
        Cliente c = buscarPorId(id);

        // Se mudar documento, valida unicidade
        clienteRepository.findByDocumento(req.documento).ifPresent(outro -> {
            if (!outro.getId().equals(c.getId())) {
                throw new WebApplicationException("Documento já cadastrado.", Response.Status.CONFLICT);
            }
        });

        c.setNome(req.nome);
        c.setDocumento(req.documento);

        // Atualiza endereço com base no CEP informado
        String cep = normalizeCep(req.cep);

        ViaCepResponse via;
        try {
            via = cepRestClient.getByCep(cep);
        } catch (ProcessingException e) {
            throw new WebApplicationException("Falha ao consultar serviço de CEP.", Response.Status.BAD_GATEWAY);
        }

        if (via == null || Boolean.TRUE.equals(via.getErro())) {
            throw new WebApplicationException("CEP inválido ou não encontrado.", Response.Status.BAD_REQUEST);
        }


        String complementoUsuario = (req.complemento != null && !req.complemento.isBlank())
            ? req.complemento
            : null;

        Endereco endereco = Endereco.builder()
            .cep(via.getCep())
            .logradouro(via.getLogradouro())
            .bairro(via.getBairro())
            .numero(req.numero)
            .complemento(complementoUsuario) // <-- NUNCA via.getComplemento()
            .build();

        c.setEndereco(endereco);

        return c;
    }

    @Transactional
    public void remover(UUID id) {
        Cliente c = buscarPorId(id);
        clienteRepository.delete(c);
    }

    private String normalizeCep(String cepInput) {
        String cep = cepInput == null ? "" : cepInput.replaceAll("\\D", "");
        if (cep.length() != 8) {
            throw new WebApplicationException("CEP deve conter 8 dígitos.", Response.Status.BAD_REQUEST);
        }
        return cep;
    }
}