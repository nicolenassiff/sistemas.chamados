package br.com.sistemas.chamados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import br.com.sistemas.chamados.entity.Chamados.Prioridade;

public record ChamadosRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size (max = 100, message = "O título deve ter no máximo 100 caracteres")
    String titulo,

    @Size (max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    String descricao,

    @NotNull(message = "A prioridade é obrigatória")
    Prioridade prioridade,

    @NotNull(message = "clienteId é obrigatório")
    Long clienteId,

    @NotNull(message = "categoriaId é obrigatório")
    Long categoriaId,

    Long tecnicoId
) {}
