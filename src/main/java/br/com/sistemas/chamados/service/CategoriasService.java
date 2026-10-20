package br.com.sistemas.chamados.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.entity.Categorias;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.CategoriasRepository;

@Service
public class CategoriasService {
    private final CategoriasRepository repository;

    public CategoriasService(CategoriasRepository repository) {
        this.repository = repository;
    }

    @Transactional 
    public CategoriasResponse criar(CategoriasRequest dto){
        if (repository.existsByNome(dto.nome())) {
            throw new RegraNegocioException("Já existe uma categoria com este nome." + dto.nome());
        }
        Categorias salvo = repository.save(new Categorias(dto.nome(), dto.descricao()));
        return CategoriasResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public List<CategoriasResponse> listar() {
        return repository.findAll().stream().map(CategoriasResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public CategoriasResponse buscar(Long id) {
        return CategoriasResponse.de(buscarEntity(id));
    }

    @Transactional
    public CategoriasResponse atualizar(Long id, CategoriasRequest dto) {
        Categorias categoria = buscarEntity(id);
        if (repository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new RegraNegocioException("Já existe uma categoria com este nome." + dto.nome());
        }
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return CategoriasResponse.de(categoria);
    }

    @Transactional
    public void excluir(Long id){
        repository.delete(buscarEntity(id));
    }

    private Categorias buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria" + id + " não encontrada."));
    }
}

