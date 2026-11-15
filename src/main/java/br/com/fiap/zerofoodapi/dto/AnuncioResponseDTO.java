package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Anuncio;
import java.time.LocalDate;

public record AnuncioResponseDTO(
        Long idAnuncio,
        Long idLojista,
        String nomeFantasiaLojista,
        Long idInsumo,
        String nomeInsumo,
        String nomeCategoria,
        Integer quantidadeEstoque,
        Double valorUnitario,
        LocalDate dataValidade,
        Character status
) {
    public AnuncioResponseDTO(Anuncio anuncio) {
        this(
                anuncio.getIdAnuncio(),
                anuncio.getLojista() != null ? anuncio.getLojista().getIdLojista() : null,
                anuncio.getLojista() != null ? anuncio.getLojista().getNomeFantasia() : null,
                anuncio.getInsumo() != null ? anuncio.getInsumo().getIdInsumo() : null,
                anuncio.getInsumo() != null ? anuncio.getInsumo().getNome() : null,
                anuncio.getInsumo() != null && anuncio.getInsumo().getCategoria() != null ? anuncio.getInsumo().getCategoria().getNomeCategoria() : null,
                anuncio.getQuantidadeEstoque(),
                anuncio.getValorUnitario(),
                anuncio.getDataValidade(),
                anuncio.getStatus()
        );
    }
}