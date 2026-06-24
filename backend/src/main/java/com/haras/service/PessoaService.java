package com.haras.service;

import com.haras.dto.PessoaRequest;
import com.haras.dto.PessoaResponse;
import com.haras.dto.TelefoneRequest;
import com.haras.exception.ConflictException;
import com.haras.exception.NotFoundException;
import com.haras.exception.ValidationException;
import com.haras.model.Pessoa;
import com.haras.model.Telefone;
import com.haras.repository.PessoaRepository;
import com.haras.repository.TelefoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final TelefoneRepository telefoneRepository;

    public PessoaService(PessoaRepository pessoaRepository, TelefoneRepository telefoneRepository) {
        this.pessoaRepository = pessoaRepository;
        this.telefoneRepository = telefoneRepository;
    }

    public List<PessoaResponse> listar() {
        return pessoaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PessoaResponse buscar(int id) {
        return toResponse(buscarOuFalhar(id));
    }

    public PessoaResponse criar(PessoaRequest request) {
        validarSenha(request.senha(), true);
        validarUnicidade(request, null);
        Pessoa pessoa = new Pessoa(null, request.nome(), request.dataNascimento(), request.cpf(),
                request.gerente(), request.email(), request.senha());
        int id = pessoaRepository.insert(pessoa);
        return toResponse(buscarOuFalhar(id));
    }

    public PessoaResponse atualizar(int id, PessoaRequest request) {
        Pessoa atual = buscarOuFalhar(id);
        validarSenha(request.senha(), false);
        validarUnicidade(request, id);
        String senha = (request.senha() == null || request.senha().isBlank()) ? atual.senha() : request.senha();
        Pessoa pessoa = new Pessoa(id, request.nome(), request.dataNascimento(), request.cpf(),
                request.gerente(), request.email(), senha);
        pessoaRepository.update(id, pessoa);
        return toResponse(buscarOuFalhar(id));
    }

    public void excluir(int id) {
        buscarOuFalhar(id);
        pessoaRepository.delete(id);
    }

    public List<Telefone> listarTelefones(int idPessoa) {
        buscarOuFalhar(idPessoa);
        return telefoneRepository.findByPessoaId(idPessoa);
    }

    public void adicionarTelefone(int idPessoa, TelefoneRequest request) {
        buscarOuFalhar(idPessoa);
        telefoneRepository.insert(idPessoa, request.telefone());
    }

    public void removerTelefone(int idPessoa, String telefone) {
        int affected = telefoneRepository.delete(idPessoa, telefone);
        if (affected == 0) {
            throw new NotFoundException("Telefone não encontrado para esta pessoa");
        }
    }

    private Pessoa buscarOuFalhar(int id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada: " + id));
    }

    private void validarSenha(String senha, boolean obrigatoria) {
        if (senha == null || senha.isBlank()) {
            if (obrigatoria) {
                throw new ValidationException(Map.of("senha", "Senha é obrigatória"));
            }
            return;
        }
        if (senha.length() < 4) {
            throw new ValidationException(Map.of("senha", "Senha deve ter entre 4 e 45 caracteres"));
        }
    }

    private void validarUnicidade(PessoaRequest request, Integer idAtual) {
        boolean cpfDuplicado = idAtual == null
                ? pessoaRepository.existsByCpf(request.cpf())
                : pessoaRepository.existsByCpfExcludingId(request.cpf(), idAtual);
        if (cpfDuplicado) {
            throw new ConflictException("CPF já cadastrado: " + request.cpf());
        }
        boolean emailDuplicado = idAtual == null
                ? pessoaRepository.existsByEmail(request.email())
                : pessoaRepository.existsByEmailExcludingId(request.email(), idAtual);
        if (emailDuplicado) {
            throw new ConflictException("Email já cadastrado: " + request.email());
        }
    }

    private PessoaResponse toResponse(Pessoa pessoa) {
        return new PessoaResponse(pessoa.idPessoa(), pessoa.nome(), pessoa.dataNascimento(),
                pessoa.cpf(), pessoa.gerente(), pessoa.email());
    }
}
