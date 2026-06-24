package com.haras.controller;

import com.haras.dto.PessoaRequest;
import com.haras.dto.PessoaResponse;
import com.haras.dto.TelefoneRequest;
import com.haras.model.Telefone;
import com.haras.service.PessoaService;
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
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public List<PessoaResponse> listar() {
        return pessoaService.listar();
    }

    @GetMapping("/{id}")
    public PessoaResponse buscar(@PathVariable int id) {
        return pessoaService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> criar(@Valid @RequestBody PessoaRequest request) {
        return ResponseEntity.status(201).body(pessoaService.criar(request));
    }

    @PutMapping("/{id}")
    public PessoaResponse atualizar(@PathVariable int id, @Valid @RequestBody PessoaRequest request) {
        return pessoaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        pessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/telefones")
    public List<Telefone> listarTelefones(@PathVariable int id) {
        return pessoaService.listarTelefones(id);
    }

    @PostMapping("/{id}/telefones")
    public ResponseEntity<Void> adicionarTelefone(@PathVariable int id, @Valid @RequestBody TelefoneRequest request) {
        pessoaService.adicionarTelefone(id, request);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}/telefones/{telefone}")
    public ResponseEntity<Void> removerTelefone(@PathVariable int id, @PathVariable String telefone) {
        pessoaService.removerTelefone(id, telefone);
        return ResponseEntity.noContent().build();
    }
}
