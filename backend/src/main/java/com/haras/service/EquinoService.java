package com.haras.service;

import com.haras.dto.EquinoRequest;
import com.haras.dto.EquinoResponse;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.model.Equino;
import com.haras.repository.EquinoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquinoService {

    private final EquinoRepository equinoRepository;

    public EquinoService(EquinoRepository equinoRepository) {
        this.equinoRepository = equinoRepository;
    }

    public List<EquinoResponse> listar() {
        return equinoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public EquinoResponse buscar(int id) {
        return toResponse(buscarOuFalhar(id));
    }

    public EquinoResponse criar(EquinoRequest request) {
        if (equinoRepository.existsByRegistro(request.registro())) {
            throw new ConflictException("Registro já cadastrado: " + request.registro());
        }
        Equino equino = new Equino(null, request.nome(), request.raca(), request.peso(), request.funcao(),
                request.dataNascimento(), request.status(), request.registro(), request.registroPai(),
                request.registroMae(), request.pelagem());
        int id = equinoRepository.insert(equino);
        return toResponse(buscarOuFalhar(id));
    }

    public EquinoResponse atualizar(int id, EquinoRequest request) {
        buscarOuFalhar(id);
        if (equinoRepository.existsByRegistroExcludingId(request.registro(), id)) {
            throw new ConflictException("Registro já cadastrado: " + request.registro());
        }
        Equino equino = new Equino(id, request.nome(), request.raca(), request.peso(), request.funcao(),
                request.dataNascimento(), request.status(), request.registro(), request.registroPai(),
                request.registroMae(), request.pelagem());
        equinoRepository.update(id, equino);
        return toResponse(buscarOuFalhar(id));
    }

    public void excluir(int id) {
        buscarOuFalhar(id);
        equinoRepository.delete(id);
    }

    private Equino buscarOuFalhar(int id) {
        return equinoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equino não encontrado: " + id));
    }

    private EquinoResponse toResponse(Equino equino) {
        return new EquinoResponse(equino.idEquino(), equino.nome(), equino.raca(), equino.peso(), equino.funcao(),
                equino.dataNascimento(), equino.status(), equino.registro(), equino.registroPai(),
                equino.registroMae(), equino.pelagem());
    }
}
