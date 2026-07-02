package com.haras.controller;

import com.haras.dto.LimpezaRequest;
import com.haras.dto.LimpezaResponse;
import com.haras.service.LimpezaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/limpezas")
public class LimpezaController {

    private final LimpezaService limpezaService;

    public LimpezaController(LimpezaService limpezaService) {
        this.limpezaService = limpezaService;
    }

    @GetMapping
    public List<LimpezaResponse> listar() {
        return limpezaService.listar();
    }

    @GetMapping("/{idLimpeza}")
    public LimpezaResponse buscar(@PathVariable int idLimpeza) {
        return limpezaService.buscar(idLimpeza);
    }

    @PostMapping
    public ResponseEntity<LimpezaResponse> criar(@Valid @RequestBody LimpezaRequest request) {
        return ResponseEntity.status(201).body(limpezaService.criar(request));
    }

    @DeleteMapping("/{idLimpeza}")
    public ResponseEntity<Void> excluir(@PathVariable int idLimpeza) {
        limpezaService.excluir(idLimpeza);
        return ResponseEntity.noContent().build();
    }
}
