package com.haras.service;

import com.haras.dto.MedicoVeterinarioRequest;
import com.haras.dto.MedicoVeterinarioResponse;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.model.MedicoVeterinario;
import com.haras.repository.MedicoVeterinarioRepository;
import com.haras.repository.MedicoVeterinarioRepository.MedicoVeterinarioComPessoa;
import com.haras.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoVeterinarioService {

    private final MedicoVeterinarioRepository medicoVeterinarioRepository;
    private final PessoaRepository pessoaRepository;

    public MedicoVeterinarioService(MedicoVeterinarioRepository medicoVeterinarioRepository,
                                     PessoaRepository pessoaRepository) {
        this.medicoVeterinarioRepository = medicoVeterinarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<MedicoVeterinarioResponse> listar() {
        return medicoVeterinarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MedicoVeterinarioResponse buscar(int idPessoa) {
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public MedicoVeterinarioResponse criar(MedicoVeterinarioRequest request) {
        int idPessoa = request.idPessoa();
        if (pessoaRepository.findById(idPessoa).isEmpty()) {
            throw new NotFoundException("Pessoa não encontrada: " + idPessoa);
        }
        if (medicoVeterinarioRepository.existsById(idPessoa)) {
            throw new ConflictException("Esta pessoa já é médica veterinária");
        }
        if (medicoVeterinarioRepository.existsByCrmv(request.numCrmv(), request.ufCrmv())) {
            throw new ConflictException("CRMV já cadastrado: " + request.numCrmv() + "/" + request.ufCrmv());
        }
        medicoVeterinarioRepository.insert(new MedicoVeterinario(idPessoa, request.numCrmv(), request.ufCrmv()));
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public MedicoVeterinarioResponse atualizar(int idPessoa, MedicoVeterinarioRequest request) {
        buscarOuFalhar(idPessoa);
        if (medicoVeterinarioRepository.existsByCrmvExcludingId(request.numCrmv(), request.ufCrmv(), idPessoa)) {
            throw new ConflictException("CRMV já cadastrado: " + request.numCrmv() + "/" + request.ufCrmv());
        }
        medicoVeterinarioRepository.update(idPessoa, new MedicoVeterinario(idPessoa, request.numCrmv(), request.ufCrmv()));
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public void excluir(int idPessoa) {
        buscarOuFalhar(idPessoa);
        medicoVeterinarioRepository.delete(idPessoa);
    }

    private MedicoVeterinarioComPessoa buscarOuFalhar(int idPessoa) {
        return medicoVeterinarioRepository.findById(idPessoa)
                .orElseThrow(() -> new NotFoundException("Médico veterinário não encontrado: " + idPessoa));
    }

    private MedicoVeterinarioResponse toResponse(MedicoVeterinarioComPessoa medico) {
        return new MedicoVeterinarioResponse(
                medico.idPessoa(), medico.nomePessoa(), medico.emailPessoa(), medico.numCrmv(), medico.ufCrmv());
    }
}
