package br.com.sistemas.chamados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChamadosRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size (max = 100, message = "O título deve ter no máximo 100 caracteres")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória")
    @Size (max = 700, message = "A descrição deve ter no máximo 700 caracteres")
    String descricao,

    @NotBlank(message = "A data de abertura é obrigatória")
    String data_abertura,

    @NotBlank(message = "A data de fechamento é obrigatória")
    String data_fechamento,

    @NotBlank(message = "A prioridade é obrigatória")
    @Size (max = 50, message = "A prioridade deve ter no máximo 50 caracteres")
    String prioridade,

    @NotBlank(message = "O status é obrigatório")
    @Size (max = 100, message = "O status deve ter no máximo 100 caracteres")
    String status
) {}
