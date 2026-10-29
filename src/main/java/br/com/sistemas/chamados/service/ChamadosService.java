package br.com.sistemas.chamados.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.entity.Chamados;
import br.com.sistemas.chamados.entity.Chamados.Prioridade;
import br.com.sistemas.chamados.entity.Chamados.StatusChamado;
import br.com.sistemas.chamados.entity.Cliente;
import br.com.sistemas.chamados.entity.Tecnicos;
import br.com.sistemas.chamados.entity.Categorias;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.CategoriasRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;

@Service
public class ChamadosService {
    private final ChamadosRepository repository;
    private final ClienteRepository clienteRepository;
    private final CategoriasRepository categoriaRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadosService(ChamadosRepository repository, ClienteRepository clienteRepository, CategoriasRepository categoriaRepository, TecnicoRepository tecnicoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional 
    public ChamadosResponse criar(ChamadosRequest dto){
        Chamados chamado = new Chamados();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setPrioridade(dto.prioridade());
        chamado.setCliente(buscarCliente(dto.clienteId()));
        chamado.setCategoria(buscarCategoria(dto.categoriaId()));
        if (dto.tecnicoId() != null) {
            chamado.setTecnico(buscarTecnico(dto.tecnicoId()));
        }
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        return ChamadosResponse.de(repository.save(chamado));
    }

    @Transactional(readOnly = true)
    public Page<ChamadosResponse> listar(StatusChamado status, Prioridade prioridade,
                                        Long clienteId, Pageable pageable) {
        return repository.filtrar(status, prioridade, clienteId, pageable).map(ChamadosResponse::de);
    }

    @Transactional(readOnly = true)
    public Page<ChamadosResponse> listarPorCliente(Long clienteId, Pageable pageable) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNaoEncontradoException("Cliente " + clienteId + " não encontrado");
        }
        return repository.findByClienteId(clienteId, pageable).map(ChamadosResponse::de);
    }

    @Transactional(readOnly = true)
    public ChamadosResponse buscar(Long id) {
        return ChamadosResponse.de(buscarEntity(id));
    }

    @Transactional
    public ChamadosResponse atualizar(Long id, ChamadosRequest dto) {
        Chamados chamado = buscarEntity(id);
        garantirNaoFechado(chamado);
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setPrioridade(dto.prioridade());
        chamado.setCategoria(buscarCategoria(dto.categoriaId()));
        if (dto.tecnicoId() != null) {
            chamado.setTecnico(buscarTecnico(dto.tecnicoId()));
        }
        return ChamadosResponse.de(chamado);
    }

    @Transactional
    public void excluir(Long id){
        repository.delete(buscarEntity(id));
    }

    @Transactional
    public ChamadosResponse atribuir(Long id, Long tecnicoId) {
        Chamados chamado = buscarEntity(id);
        garantirNaoFechado(chamado);
        chamado.setTecnico(buscarTecnico(tecnicoId));
        return ChamadosResponse.de(chamado);
    }

    @Transactional
    public ChamadosResponse alterarStatus(Long id, StatusChamado novo) {
        Chamados chamado = buscarEntity(id);
        garantirNaoFechado(chamado);
        if (!transicaoValida(chamado.getStatus(), novo)) {
            throw new RegraNegocioException(
                "Transição inválida de " + chamado.getStatus() + " para " + novo);
        }
        chamado.setStatus(novo);
        if (novo == StatusChamado.FECHADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }
        return ChamadosResponse.de(chamado);
    }

    private boolean transicaoValida(StatusChamado de, StatusChamado para) {
        return switch (de) {
            case ABERTO -> para == StatusChamado.EM_ATENDIMENTO || para == StatusChamado.FECHADO;
            case EM_ATENDIMENTO -> para == StatusChamado.AGUARDANDO_CLIENTE || para == StatusChamado.FECHADO;
            case AGUARDANDO_CLIENTE -> para == StatusChamado.EM_ATENDIMENTO || para == StatusChamado.FECHADO;
            case FECHADO -> false;
        };
    }

    private void garantirNaoFechado(Chamados chamado) {
        if (chamado.getStatus() == StatusChamado.FECHADO) {
            throw new RegraNegocioException("Chamado " + chamado.getId() + " está fechado e não pode ser alterado");
        }
    }

    private Chamados buscarEntity(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado " + id + " não encontrado"));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado"));
    }

    private Categorias buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + " não encontrada"));
    }

    private Tecnicos buscarTecnico(Long id) {
        return tecnicoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico " + id + " não encontrado"));
    }
}


