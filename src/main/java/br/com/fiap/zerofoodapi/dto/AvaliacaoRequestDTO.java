package br.com.fiap.zerofoodapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequestDTO(
        @NotNull(message = "O ID do lojista avaliador é obrigatório")
        Long idLojistaAvaliador,

        @NotNull(message = "O ID do lojista avaliado é obrigatório")
        Long idLojistaAvaliado,

        @NotNull(message = "O ID da transação é obrigatório")
        Long idTransacao,

        @NotNull(message = "A quantidade de estrelas é obrigatória")
        @Min(value = 1, message = "A nota mínima é 1 estrela")
        @Max(value = 5, message = "A nota máxima é 5 estrelas")
        Integer quantidadeEstrelas,

        @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
        String comentario
) {}