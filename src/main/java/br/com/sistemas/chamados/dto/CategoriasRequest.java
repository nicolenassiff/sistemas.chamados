package br.com.sistemas.chamados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriasRequest(
    @NotBlank(message = "O nome é obrigatório")
    @Size (max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String nome,

    @NotBlank (message = "A descrição é obrigatória")
    @Size (max = 700, message = "A descrição deve ter no máximo 200 caracteres")
    String descricao
) {}

