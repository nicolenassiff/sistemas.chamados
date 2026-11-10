package br.com.sistemas.chamados.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.service.CategoriasService;

@RestController
@RequestMapping("/categorias")
public class CategoriasController {

    private final CategoriasService service;

    public CategoriasController(CategoriasService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriasResponse> criar(@Valid @RequestBody CategoriasRequest dto) {
        CategoriasResponse criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/categorias/" + criada.id())).body(criada);
    }

    @GetMapping
    public List<CategoriasResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CategoriasResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public CategoriasResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriasRequest dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
