package com.haras.controller;

import com.haras.dto.ContratoRequest;
import com.haras.dto.ContratoResponse;
import com.haras.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<ContratoResponse> listar() {
        return contratoService.listar();
    }

    @GetMapping("/{id}")
    public ContratoResponse buscar(@PathVariable int id) {
        return contratoService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<ContratoResponse> criar(@Valid @RequestBody ContratoRequest request) {
        return ResponseEntity.status(201).body(contratoService.criar(request));
    }

    @PutMapping("/{id}")
    public ContratoResponse atualizar(@PathVariable int id, @Valid @RequestBody ContratoRequest request) {
        return contratoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        contratoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
