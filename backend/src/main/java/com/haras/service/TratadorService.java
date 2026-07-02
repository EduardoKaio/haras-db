package com.haras.service;

import com.haras.dto.TratadorRequest;
import com.haras.dto.TratadorResponse;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.repository.ColaboradorRepository;
import com.haras.repository.TratadorRepository;
import com.haras.repository.TratadorRepository.TratadorComPessoa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TratadorService {

    private final TratadorRepository tratadorRepository;
    private final ColaboradorRepository colaboradorRepository;

    public TratadorService(TratadorRepository tratadorRepository, ColaboradorRepository colaboradorRepository) {
        this.tratadorRepository = tratadorRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<TratadorResponse> listar() {
        return tratadorRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TratadorResponse buscar(int idPessoa) {
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public TratadorResponse criar(TratadorRequest request) {
        int idPessoa = request.idPessoa();
        // Tratador é especialização de Colaborador: exige o colaborador previamente cadastrado.
        if (!colaboradorRepository.existsById(idPessoa)) {
            throw new NotFoundException("Colaborador não encontrado: " + idPessoa
                    + " (cadastre a pessoa como colaborador antes de torná-la tratadora)");
        }
        if (tratadorRepository.existsById(idPessoa)) {
            throw new ConflictException("Esta pessoa já é tratadora");
        }
        tratadorRepository.insert(idPessoa);
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public void excluir(int idPessoa) {
        buscarOuFalhar(idPessoa);
        tratadorRepository.delete(idPessoa);
    }

    private TratadorComPessoa buscarOuFalhar(int idPessoa) {
        return tratadorRepository.findById(idPessoa)
                .orElseThrow(() -> new NotFoundException("Tratador não encontrado: " + idPessoa));
    }

    private TratadorResponse toResponse(TratadorComPessoa tratador) {
        return new TratadorResponse(tratador.idPessoa(), tratador.nomePessoa(), tratador.emailPessoa());
    }
}
