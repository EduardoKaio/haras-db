package com.haras.service;

import com.haras.dto.ContratoRequest;
import com.haras.dto.ContratoResponse;
import com.haras.exception.NotFoundException;
import com.haras.model.Contrato;
import com.haras.repository.ColaboradorRepository;
import com.haras.repository.ContratoRepository;
import com.haras.repository.ContratoRepository.ContratoComColaborador;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public ContratoService(ContratoRepository contratoRepository, ColaboradorRepository colaboradorRepository) {
        this.contratoRepository = contratoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<ContratoResponse> listar() {
        return contratoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ContratoResponse buscar(int id) {
        return toResponse(buscarOuFalhar(id));
    }

    public ContratoResponse criar(ContratoRequest request) {
        validarColaborador(request.idPessoa());
        Contrato contrato = new Contrato(null, request.idPessoa(), request.dataInicio(), request.dataFim(), request.salario());
        int id = contratoRepository.insert(contrato);
        return toResponse(buscarOuFalhar(id));
    }

    public ContratoResponse atualizar(int id, ContratoRequest request) {
        buscarOuFalhar(id);
        validarColaborador(request.idPessoa());
        Contrato contrato = new Contrato(id, request.idPessoa(), request.dataInicio(), request.dataFim(), request.salario());
        contratoRepository.update(id, contrato);
        return toResponse(buscarOuFalhar(id));
    }

    public void excluir(int id) {
        buscarOuFalhar(id);
        contratoRepository.delete(id);
    }

    private void validarColaborador(int idPessoa) {
        if (!colaboradorRepository.existsById(idPessoa)) {
            throw new NotFoundException("Colaborador não encontrado: " + idPessoa);
        }
    }

    private ContratoComColaborador buscarOuFalhar(int id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contrato não encontrado: " + id));
    }

    private ContratoResponse toResponse(ContratoComColaborador contrato) {
        return new ContratoResponse(
                contrato.idContrato(), contrato.idPessoa(), contrato.nomePessoa(),
                contrato.dataInicio(), contrato.dataFim(), contrato.salario());
    }
}
