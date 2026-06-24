package com.haras.controller;

import com.haras.dto.ColaboradorRequest;
import com.haras.dto.ColaboradorResponse;
import com.haras.service.ColaboradorService;
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
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping
    public List<ColaboradorResponse> listar() {
        return colaboradorService.listar();
    }

    @GetMapping("/{idPessoa}")
    public ColaboradorResponse buscar(@PathVariable int idPessoa) {
        return colaboradorService.buscar(idPessoa);
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(201).body(colaboradorService.criar(request));
    }

    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Void> excluir(@PathVariable int idPessoa) {
        colaboradorService.excluir(idPessoa);
        return ResponseEntity.noContent().build();
    }
}
