package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Transacao;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
        Long idTransacao,
        Long idAnuncio,
        String nomeInsumo,
        Long idLojistaComprador,
        String nomeFantasiaComprador,
        LocalDateTime dataHora,
        Integer quantidadeVendida,
        Double valorTotal,
        Double taxaTakeRate,
        Character status
) {
    public TransacaoResponseDTO(Transacao transacao) {
        this(
                transacao.getIdTransacao(),
                transacao.getAnuncio() != null ? transacao.getAnuncio().getIdAnuncio() : null,
                transacao.getAnuncio() != null ? transacao.getAnuncio().getInsumo().getNome() : null,
                transacao.getLojistaComprador() != null ? transacao.getLojistaComprador().getIdLojista() : null,
                transacao.getLojistaComprador() != null ? transacao.getLojistaComprador().getNomeFantasia() : null,
                transacao.getDataHora(),
                transacao.getQuantidadeVendida(),
                transacao.getValorTotal(),
                transacao.getTaxaTakeRate(),
                transacao.getStatus()
        );
    }
}