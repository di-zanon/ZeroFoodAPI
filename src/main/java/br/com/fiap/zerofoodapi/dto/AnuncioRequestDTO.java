package br.com.fiap.zerofoodapi.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AnuncioRequestDTO(
        @NotNull(message = "O ID do lojista é obrigatório")
        Long idLojista,

        @NotNull(message = "O ID do insumo é obrigatório")
        Long idInsumo,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidadeEstoque,

        @NotNull(message = "O valor unitário é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        Double valorUnitario,

        @NotNull(message = "A data de validade é obrigatória")
        @FutureOrPresent(message = "A data de validade não pode estar no passado")
        LocalDate dataValidade
) {}