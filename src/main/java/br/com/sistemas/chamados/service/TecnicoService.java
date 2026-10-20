package br.com.sistemas.chamados.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.TecnicosRequest;
import br.com.sistemas.chamados.dto.TecnicosResponse;
import br.com.sistemas.chamados.entity.Tecnicos;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.TecnicoRepository;

@Service
public class TecnicoService {
    private final TecnicoRepository repository;

    public TecnicoService(TecnicoRepository repository) {
        this.repository = repository;
    }

    @Transactional 
    public TecnicosResponse criar(TecnicosRequest dto){
        if (repository.existsByNome(dto.nome())) {
            throw new RegraNegocioException("Já existe um técnico com este nome." + dto.nome());
        }
        Tecnicos salvo = repository.<Tecnicos>save(new Tecnicos(dto.nome(), dto.email(), dto.perfil()));
        return TecnicosResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public List<TecnicosResponse> listar() {
        return repository.findAll().stream().map(TecnicosResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public TecnicosResponse buscar(Long id) {
        return TecnicosResponse.de(buscarEntity(id));
    }

    @Transactional
    public TecnicosResponse atualizar(Long id, TecnicosRequest dto) {
        Tecnicos tecnico = buscarEntity(id);
        if (repository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new RegraNegocioException("Já existe um técnico com o nome." + dto.nome());
        }
        tecnico.setNome(dto.nome());
        tecnico.setEmail(dto.email());
        tecnico.setPerfil(dto.perfil());
        return TecnicosResponse.de(tecnico);
    }

    @Transactional
    public void excluir(Long id){
        repository.delete(buscarEntity(id));
    }

    private Tecnicos buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico" + id + " não encontrado."));
    }
}

