package br.com.sistemas.chamados.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sistemas.chamados.entity.Chamados;
import br.com.sistemas.chamados.entity.Chamados.Prioridade;
import br.com.sistemas.chamados.entity.Chamados.StatusChamado;

public interface ChamadosRepository extends JpaRepository<Chamados, Long> {
boolean existsByClienteId(Long clienteId);
    boolean existsByCategoriaId(Long categoriaId);
    boolean existsByTecnicoId(Long tecnicoId);

    Page<Chamados> findByClienteId(Long clienteId, Pageable pageable);

    @Query("""
        select c from Chamado c
        where (:status is null or c.status = :status)
          and (:prioridade is null or c.prioridade = :prioridade)
          and (:clienteId is null or c.cliente.id = :clienteId)
        """)
    Page<Chamados> filtrar(@Param("status") StatusChamado status,
                          @Param("prioridade") Prioridade prioridade,
                          @Param("clienteId") Long clienteId,
                          Pageable pageable);
}
