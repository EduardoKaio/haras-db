package com.haras.service;

import com.haras.dto.ProprietarioRequest;
import com.haras.dto.ProprietarioResponse;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.model.Equino;
import com.haras.repository.EquinoRepository;
import com.haras.repository.PessoaRepository;
import com.haras.repository.ProprietarioRepository;
import com.haras.repository.ProprietarioRepository.ProprietarioComPessoa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;
    private final PessoaRepository pessoaRepository;
    private final EquinoRepository equinoRepository;

    public ProprietarioService(ProprietarioRepository proprietarioRepository, PessoaRepository pessoaRepository,
                                EquinoRepository equinoRepository) {
        this.proprietarioRepository = proprietarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.equinoRepository = equinoRepository;
    }

    public List<ProprietarioResponse> listar() {
        return proprietarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProprietarioResponse buscar(int idPessoa) {
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public ProprietarioResponse criar(ProprietarioRequest request) {
        int idPessoa = request.idPessoa();
        if (pessoaRepository.findById(idPessoa).isEmpty()) {
            throw new NotFoundException("Pessoa não encontrada: " + idPessoa);
        }
        if (proprietarioRepository.existsById(idPessoa)) {
            throw new ConflictException("Esta pessoa já é proprietária");
        }
        proprietarioRepository.insert(idPessoa);
        return toResponse(buscarOuFalhar(idPessoa));
    }

    public void excluir(int idPessoa) {
        buscarOuFalhar(idPessoa);
        proprietarioRepository.delete(idPessoa);
    }

    public List<Equino> listarEquinos(int idPessoa) {
        buscarOuFalhar(idPessoa);
        return proprietarioRepository.listarEquinos(idPessoa);
    }

    public void vincularEquino(int idPessoa, int idEquino) {
        buscarOuFalhar(idPessoa);
        if (equinoRepository.findById(idEquino).isEmpty()) {
            throw new NotFoundException("Equino não encontrado: " + idEquino);
        }
        if (proprietarioRepository.existeVinculo(idPessoa, idEquino)) {
            throw new ConflictException("Este equino já está vinculado a esta proprietária");
        }
        proprietarioRepository.vincularEquino(idPessoa, idEquino);
    }

    public void desvincularEquino(int idPessoa, int idEquino) {
        buscarOuFalhar(idPessoa);
        if (!proprietarioRepository.existeVinculo(idPessoa, idEquino)) {
            throw new NotFoundException("Vínculo não encontrado entre esta proprietária e o equino " + idEquino);
        }
        proprietarioRepository.desvincularEquino(idPessoa, idEquino);
    }

    private ProprietarioComPessoa buscarOuFalhar(int idPessoa) {
        return proprietarioRepository.findById(idPessoa)
                .orElseThrow(() -> new NotFoundException("Proprietário não encontrado: " + idPessoa));
    }

    private ProprietarioResponse toResponse(ProprietarioComPessoa proprietario) {
        return new ProprietarioResponse(
                proprietario.idPessoa(), proprietario.nomePessoa(), proprietario.emailPessoa());
    }
}
