package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.Tecnicos;

public record TecnicosResponse(Long id, String nome, String email, String perfil) {
    public static TecnicosResponse de(Tecnicos t) {
        return new TecnicosResponse(t.getId(), t.getNome(), t.getEmail(), t.getPerfil());
    }
}
