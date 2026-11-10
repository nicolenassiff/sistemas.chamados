package br.com.sistemas.chamados.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.TecnicosRequest;
import br.com.sistemas.chamados.dto.TecnicosResponse;
import br.com.sistemas.chamados.entity.Tecnicos;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;

@Service
public class TecnicoService {
    private final TecnicoRepository repository;
    private final ChamadosRepository chamadosRepository;

    public TecnicoService(TecnicoRepository repository, ChamadosRepository chamadosRepository) {
        this.repository = repository;
        this.chamadosRepository = chamadosRepository;
    }

    @Transactional 
    public TecnicosResponse criar(TecnicosRequest dto){
        if (repository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("Já existe um técnico com este email." + dto.email());
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
        if (repository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new RegraNegocioException("Já existe um técnico com este email." + dto.email());
        }
        tecnico.setNome(dto.nome());
        tecnico.setEmail(dto.email());
        tecnico.setPerfil(dto.perfil());
        return TecnicosResponse.de(tecnico);
    }

    @Transactional
    public void excluir(Long id){
        Tecnicos tecnico = buscarEntity(id);
        if (chamadosRepository.existsByTecnicoId(id)) {
            throw new RegraNegocioException("Técnico possui chamados vinculados e não pode ser excluído");
        }
        repository.delete(buscarEntity(id));
    }

    private Tecnicos buscarEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico" + id + " não encontrado."));
    }
}

