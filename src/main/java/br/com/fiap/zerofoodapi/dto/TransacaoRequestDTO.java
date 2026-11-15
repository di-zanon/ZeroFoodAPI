package br.com.fiap.zerofoodapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransacaoRequestDTO(
        @NotNull(message = "O ID do anúncio é obrigatório")
        Long idAnuncio,

        @NotNull(message = "O ID do lojista comprador é obrigatório")
        Long idLojistaComprador,

        @NotNull(message = "A quantidade vendida é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidadeVendida
) {}