package br.com.fiap.zerofoodapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LojistaRequestDTO(
        @NotBlank(message = "A razão social é obrigatória")
        @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
        String razaoSocial,

        @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres")
        String nomeFantasia,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Size(min = 18, max = 18, message = "O CNPJ deve ter exatamente 18 caracteres (ex: 00.000.000/0000-00)")
        String cnpj,

        @Email(message = "E-mail inválido")
        String email,

        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres")
        String telefone,

        @Size(max = 500, message = "O endereço deve ter no máximo 500 caracteres")
        String endereco
) {
}