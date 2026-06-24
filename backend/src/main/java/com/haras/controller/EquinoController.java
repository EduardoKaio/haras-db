package com.haras.controller;

import com.haras.dto.EquinoRequest;
import com.haras.dto.EquinoResponse;
import com.haras.service.EquinoService;
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
@RequestMapping("/api/equinos")
public class EquinoController {

    private final EquinoService equinoService;

    public EquinoController(EquinoService equinoService) {
        this.equinoService = equinoService;
    }

    @GetMapping
    public List<EquinoResponse> listar() {
        return equinoService.listar();
    }

    @GetMapping("/{id}")
    public EquinoResponse buscar(@PathVariable int id) {
        return equinoService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<EquinoResponse> criar(@Valid @RequestBody EquinoRequest request) {
        return ResponseEntity.status(201).body(equinoService.criar(request));
    }

    @PutMapping("/{id}")
    public EquinoResponse atualizar(@PathVariable int id, @Valid @RequestBody EquinoRequest request) {
        return equinoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        equinoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
