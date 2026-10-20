package br.com.sistemas.chamados.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.entity.Chamados;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ChamadosRepository;

@Service
public class ChamadosService {
    private final ChamadosRepository repository;

    public ChamadosService(ChamadosRepository repository) {
        this.repository = repository;
    }

    @Transactional 
    public ChamadosResponse criar(ChamadosRequest dto){
        if (repository.existsByTitulo(dto.titulo())) {
            throw new RegraNegocioException("Já existe um chamado com este título." + dto.titulo());
        }
        Chamados salvo = repository.save(new Chamados(dto.titulo(), dto.descricao(), dto.data_abertura(), dto.prioridade(), dto.status()));
        return ChamadosResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public List<ChamadosResponse> listar() {
        return repository.findAll().stream().map(ChamadosResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ChamadosResponse buscar(Long id) {
        return ChamadosResponse.de(buscarEntity(id));
    }

    @Transactional
    public ChamadosResponse atualizar(Long id, ChamadosRequest dto) {
        Chamados chamado = buscarEntity(id);
        if (repository.existsByTituloAndIdNot(dto.titulo(), id)) {
            throw new RegraNegocioException("Já existe um chamado com este título." + dto.titulo());
        }
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setData_abertura(dto.data_abertura());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(dto.status());
        return ChamadosResponse.de(chamado);
    }

    @Transactional
    public void excluir(Long id){
        repository.delete(buscarEntity(id));
    }

    private Chamados buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado" + id + " não encontrado."));
    }
}

