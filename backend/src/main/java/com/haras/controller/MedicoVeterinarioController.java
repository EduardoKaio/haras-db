package com.haras.controller;

import com.haras.dto.MedicoVeterinarioRequest;
import com.haras.dto.MedicoVeterinarioResponse;
import com.haras.service.MedicoVeterinarioService;
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
@RequestMapping("/api/medicos-veterinarios")
public class MedicoVeterinarioController {

    private final MedicoVeterinarioService medicoVeterinarioService;

    public MedicoVeterinarioController(MedicoVeterinarioService medicoVeterinarioService) {
        this.medicoVeterinarioService = medicoVeterinarioService;
    }

    @GetMapping
    public List<MedicoVeterinarioResponse> listar() {
        return medicoVeterinarioService.listar();
    }

    @GetMapping("/{idPessoa}")
    public MedicoVeterinarioResponse buscar(@PathVariable int idPessoa) {
        return medicoVeterinarioService.buscar(idPessoa);
    }

    @PostMapping
    public ResponseEntity<MedicoVeterinarioResponse> criar(@Valid @RequestBody MedicoVeterinarioRequest request) {
        return ResponseEntity.status(201).body(medicoVeterinarioService.criar(request));
    }

    @PutMapping("/{idPessoa}")
    public MedicoVeterinarioResponse atualizar(@PathVariable int idPessoa,
                                                @Valid @RequestBody MedicoVeterinarioRequest request) {
        return medicoVeterinarioService.atualizar(idPessoa, request);
    }

    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Void> excluir(@PathVariable int idPessoa) {
        medicoVeterinarioService.excluir(idPessoa);
        return ResponseEntity.noContent().build();
    }
}
