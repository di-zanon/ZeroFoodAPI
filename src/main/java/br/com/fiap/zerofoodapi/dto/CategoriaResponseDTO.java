package br.com.fiap.zerofoodapi.dto;

import br.com.fiap.zerofoodapi.model.Categoria;

public record CategoriaResponseDTO(
        Long idCategoria,
        String nomeCategoria
) {
    public CategoriaResponseDTO(Categoria categoria) {
        this(categoria.getIdCategoria(), categoria.getNomeCategoria());
    }
}