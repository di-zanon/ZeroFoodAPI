package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Insumo;

public record InsumoResponseDTO(
        Long idInsumo,
        Long idCategoria,
        String nomeCategoria,
        String nome,
        String unidadeMedida
) {
    public InsumoResponseDTO(Insumo insumo) {
        this(
                insumo.getIdInsumo(),
                insumo.getCategoria() != null ? insumo.getCategoria().getIdCategoria() : null,
                insumo.getCategoria() != null ? insumo.getCategoria().getNomeCategoria() : null,
                insumo.getNome(),
                insumo.getUnidadeMedida()
        );
    }
}