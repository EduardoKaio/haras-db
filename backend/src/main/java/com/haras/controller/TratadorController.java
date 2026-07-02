package com.haras.controller;

import com.haras.dto.TratadorRequest;
import com.haras.dto.TratadorResponse;
import com.haras.service.TratadorService;
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
@RequestMapping("/api/tratadores")
public class TratadorController {

    private final TratadorService tratadorService;

    public TratadorController(TratadorService tratadorService) {
        this.tratadorService = tratadorService;
    }

    @GetMapping
    public List<TratadorResponse> listar() {
        return tratadorService.listar();
    }

    @GetMapping("/{idPessoa}")
    public TratadorResponse buscar(@PathVariable int idPessoa) {
        return tratadorService.buscar(idPessoa);
    }

    @PostMapping
    public ResponseEntity<TratadorResponse> criar(@Valid @RequestBody TratadorRequest request) {
        return ResponseEntity.status(201).body(tratadorService.criar(request));
    }

    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Void> excluir(@PathVariable int idPessoa) {
        tratadorService.excluir(idPessoa);
        return ResponseEntity.noContent().build();
    }
}
