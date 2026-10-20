package br.com.sistemas.chamados.dto;

import java.util.Date;

import br.com.sistemas.chamados.entity.Chamados;

public record ChamadosResponse(Long id, String titulo, String descricao, Date data_abertura) {
    public static ChamadosResponse de(Chamados c) {
        return new ChamadosResponse(c.getId(), c.getTitulo(), c.getDescricao(), c.getData_abertura());
    }
}
