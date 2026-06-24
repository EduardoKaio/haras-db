package com.haras.controller;

import com.haras.dto.EquinoResponse;
import com.haras.dto.EquinoVinculoRequest;
import com.haras.dto.ProprietarioRequest;
import com.haras.dto.ProprietarioResponse;
import com.haras.model.Equino;
import com.haras.service.ProprietarioService;
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
@RequestMapping("/api/proprietarios")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    @GetMapping
    public List<ProprietarioResponse> listar() {
        return proprietarioService.listar();
    }

    @GetMapping("/{idPessoa}")
    public ProprietarioResponse buscar(@PathVariable int idPessoa) {
        return proprietarioService.buscar(idPessoa);
    }

    @PostMapping
    public ResponseEntity<ProprietarioResponse> criar(@Valid @RequestBody ProprietarioRequest request) {
        return ResponseEntity.status(201).body(proprietarioService.criar(request));
    }

    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Void> excluir(@PathVariable int idPessoa) {
        proprietarioService.excluir(idPessoa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idPessoa}/equinos")
    public List<EquinoResponse> listarEquinos(@PathVariable int idPessoa) {
        return proprietarioService.listarEquinos(idPessoa).stream().map(this::toResponse).toList();
    }

    @PostMapping("/{idPessoa}/equinos")
    public ResponseEntity<Void> vincularEquino(@PathVariable int idPessoa,
                                                @Valid @RequestBody EquinoVinculoRequest request) {
        proprietarioService.vincularEquino(idPessoa, request.idEquino());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{idPessoa}/equinos/{idEquino}")
    public ResponseEntity<Void> desvincularEquino(@PathVariable int idPessoa, @PathVariable int idEquino) {
        proprietarioService.desvincularEquino(idPessoa, idEquino);
        return ResponseEntity.noContent().build();
    }

    private EquinoResponse toResponse(Equino equino) {
        return new EquinoResponse(equino.idEquino(), equino.nome(), equino.raca(), equino.peso(), equino.funcao(),
                equino.dataNascimento(), equino.status(), equino.registro(), equino.registroPai(),
                equino.registroMae(), equino.pelagem());
    }
}
