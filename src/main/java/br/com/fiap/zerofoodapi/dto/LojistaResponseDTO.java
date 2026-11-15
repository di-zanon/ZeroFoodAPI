package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Lojista;

public record LojistaResponseDTO(
        Long idLojista,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        String endereco
) {
    // Construtor que converte a Entidade em DTO
    public LojistaResponseDTO(Lojista lojista) {
        this(
                lojista.getIdLojista(),
                lojista.getRazaoSocial(),
                lojista.getNomeFantasia(),
                lojista.getCnpj(),
                lojista.getEmail(),
                lojista.getTelefone(),
                lojista.getEndereco()
        );
    }
}