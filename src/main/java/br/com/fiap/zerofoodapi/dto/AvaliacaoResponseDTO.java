package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Avaliacao;

public record AvaliacaoResponseDTO(
        Long idAvaliacao,
        Long idLojistaAvaliador,
        String nomeFantasiaAvaliador,
        Long idLojistaAvaliado,
        String nomeFantasiaAvaliado,
        Long idTransacao,
        Integer quantidadeEstrelas,
        String comentario
) {
    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this(
                avaliacao.getIdAvaliacao(),
                avaliacao.getLojistaAvaliador() != null ? avaliacao.getLojistaAvaliador().getIdLojista() : null,
                avaliacao.getLojistaAvaliador() != null ? avaliacao.getLojistaAvaliador().getNomeFantasia() : null,
                avaliacao.getLojistaAvaliado() != null ? avaliacao.getLojistaAvaliado().getIdLojista() : null,
                avaliacao.getLojistaAvaliado() != null ? avaliacao.getLojistaAvaliado().getNomeFantasia() : null,
                avaliacao.getTransacao() != null ? avaliacao.getTransacao().getIdTransacao() : null,
                avaliacao.getQuantidadeEstrelas(),
                avaliacao.getComentario()
        );
    }
}
