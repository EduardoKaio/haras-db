package com.haras.service;

import com.haras.dto.ColaboradorRequest;
import com.haras.dto.ColaboradorResponse;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.repository.ColaboradorRepository;
import com.haras.repository.ColaboradorRepository.ColaboradorComPessoa;
import com.haras.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final PessoaRepository pessoaRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository, PessoaRepository pessoaRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<ColaboradorResponse> listar() {
        return colaboradorRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ColaboradorResponse buscar(int idPessoa) {
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public ColaboradorResponse criar(ColaboradorRequest request) {
        int idPessoa = request.idPessoa();
        if (pessoaRepository.findById(idPessoa).isEmpty()) {
            throw new NotFoundException("Pessoa não encontrada: " + idPessoa);
        }
        if (colaboradorRepository.existsById(idPessoa)) {
            throw new ConflictException("Esta pessoa já é colaboradora");
        }
        colaboradorRepository.insert(idPessoa);
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public void excluir(int idPessoa) {
        buscarOuFalhar(idPessoa);
        colaboradorRepository.delete(idPessoa);
    }

    private ColaboradorComPessoa buscarOuFalhar(int idPessoa) {
        return colaboradorRepository.findById(idPessoa)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado: " + idPessoa));
    }

    private ColaboradorResponse toResponse(ColaboradorComPessoa colaborador) {
        return new ColaboradorResponse(colaborador.idPessoa(), colaborador.nomePessoa(), colaborador.emailPessoa());
    }
}
