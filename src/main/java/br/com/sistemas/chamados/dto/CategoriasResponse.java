package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.Categorias;

public record CategoriasResponse(Long id, String nome, String descricao) {
    public static CategoriasResponse de(Categorias c) {
        return new CategoriasResponse(c.getId(), c.getNome(), c.getDescricao());
    }
}
