package br.com.fiap.zerofoodapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InsumoRequestDTO(
        @NotNull(message = "O ID da categoria é obrigatório")
        Long idCategoria,

        @NotBlank(message = "O nome do insumo é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 20, message = "A unidade de medida deve ter no máximo 20 caracteres")
        String unidadeMedida
) {}