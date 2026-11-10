package br.com.sistemas.chamados.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import br.com.sistemas.chamados.dto.AlterarStatusRequest;
import br.com.sistemas.chamados.dto.AtribuirTecnicoRequest;
import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.entity.Chamados.Prioridade;
import br.com.sistemas.chamados.entity.Chamados.StatusChamado;
import br.com.sistemas.chamados.service.ChamadosService;

/** Sem @RequestMapping na classe, pois também expõe GET /clientes/{id}/chamados. */
@RestController
public class ChamadoController {

    private final ChamadosService service;

    public ChamadoController(ChamadosService service) {
        this.service = service;
    }

    @PostMapping("/chamados")
    public ResponseEntity<ChamadosResponse> criar(@Valid @RequestBody ChamadosRequest dto) {
        ChamadosResponse criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/chamados/" + criado.id())).body(criado);
    }

    /** Ex.: /chamados?status=ABERTO&prioridade=ALTA&clienteId=1&page=0&size=10&sort=dataAbertura,desc */
    @GetMapping("/chamados")
    public Page<ChamadosResponse> listar(
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 10, sort = "dataAbertura") Pageable pageable) {
        return service.listar(status, prioridade, clienteId, pageable);
    }

    @GetMapping("/chamados/{id}")
    public ChamadosResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/chamados/{id}")
    public ChamadosResponse atualizar(@PathVariable Long id, @Valid @RequestBody ChamadosRequest dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/chamados/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/chamados/{id}/atribuir")
    public ChamadosResponse atribuir(@PathVariable Long id, @Valid @RequestBody AtribuirTecnicoRequest dto) {
        return service.atribuir(id, dto.tecnicoId());
    }

    @PatchMapping("/chamados/{id}/status")
    public ChamadosResponse alterarStatus(@PathVariable Long id, @Valid @RequestBody AlterarStatusRequest dto) {
        return service.alterarStatus(id, dto.status());
    }

    @GetMapping("/clientes/{id}/chamados")
    public Page<ChamadosResponse> listarPorCliente(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataAbertura") Pageable pageable) {
        return service.listarPorCliente(id, pageable);
    }
}