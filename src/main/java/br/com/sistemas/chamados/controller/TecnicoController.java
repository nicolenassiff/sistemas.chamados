package br.com.sistemas.chamados.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import br.com.sistemas.chamados.dto.TecnicosRequest;
import br.com.sistemas.chamados.dto.TecnicosResponse;
import br.com.sistemas.chamados.service.TecnicoService;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService service;

    public TecnicoController(TecnicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TecnicosResponse> criar(@Valid @RequestBody TecnicosRequest dto) {
        TecnicosResponse criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/tecnicos/" + criado.id())).body(criado);
    }

    @GetMapping
    public List<TecnicosResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TecnicosResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public TecnicosResponse atualizar(@PathVariable Long id, @Valid @RequestBody TecnicosRequest dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}