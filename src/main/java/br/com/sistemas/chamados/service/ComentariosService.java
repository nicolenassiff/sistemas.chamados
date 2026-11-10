package br.com.sistemas.chamados.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.dto.ComentarioResponse;
import br.com.sistemas.chamados.entity.Chamados;
import br.com.sistemas.chamados.entity.Comentarios;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.ComentarioRepository;

@Service
public class ComentariosService {

    private final ComentarioRepository repository;
    private final ChamadosRepository chamadosRepository;

    public ComentariosService(ComentarioRepository repository, ChamadosRepository chamadosRepository) {
        this.repository = repository;
        this.chamadosRepository = chamadosRepository;
    }

    @Transactional
    public ComentarioResponse criar(Long chamadoId, ComentarioRequest dto) {
        Chamados chamado = buscarChamados(chamadoId);
        Comentarios salvo = repository.save(new Comentarios(dto.texto(), dto.autor(), chamado));
        return ComentarioResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listar(Long chamadoId) {
        buscarChamados(chamadoId);
        return repository.findByChamadoIdOrderByDataAsc(chamadoId).stream()
            .map(ComentarioResponse::de).toList();
    }

    private Chamados buscarChamados(Long id) {
        return chamadosRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado " + id + " não encontrado"));
    }
}