package br.com.sistemas.chamados.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemas.chamados.entity.Tecnicos;

public interface TecnicoRepository extends JpaRepository<Tecnicos, Long> {
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
}
