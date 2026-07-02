package com.haras.service;

import com.haras.dto.LimpezaRequest;
import com.haras.dto.LimpezaResponse;
import com.haras.exception.NotFoundException;
import com.haras.repository.EquinoRepository;
import com.haras.repository.LimpezaRepository;
import com.haras.repository.LimpezaRepository.LimpezaRow;
import com.haras.repository.TratadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LimpezaService {

    private final LimpezaRepository limpezaRepository;
    private final EquinoRepository equinoRepository;
    private final TratadorRepository tratadorRepository;

    public LimpezaService(LimpezaRepository limpezaRepository, EquinoRepository equinoRepository,
                          TratadorRepository tratadorRepository) {
        this.limpezaRepository = limpezaRepository;
        this.equinoRepository = equinoRepository;
        this.tratadorRepository = tratadorRepository;
    }

    public List<LimpezaResponse> listar() {
        return limpezaRepository.findAll().stream().map(row -> montar(row.idLimpeza(), row)).toList();
    }

    public LimpezaResponse buscar(int idLimpeza) {
        LimpezaRow row = buscarOuFalhar(idLimpeza);
        return montar(idLimpeza, row);
    }

    @Transactional
    public LimpezaResponse criar(LimpezaRequest request) {
        List<Integer> equinos = request.idsEquinos().stream().distinct().toList();
        List<Integer> tratadores = request.idsTratadores().stream().distinct().toList();

        for (Integer idEquino : equinos) {
            if (equinoRepository.findById(idEquino).isEmpty()) {
                throw new NotFoundException("Equino não encontrado: " + idEquino);
            }
        }
        for (Integer idPessoa : tratadores) {
            if (!tratadorRepository.existsById(idPessoa)) {
                throw new NotFoundException("Tratador não encontrado: " + idPessoa);
            }
        }

        int idLimpeza = limpezaRepository.insert(request.dataHora());
        equinos.forEach(idEquino -> limpezaRepository.vincularEquino(idLimpeza, idEquino));
        tratadores.forEach(idPessoa -> limpezaRepository.vincularTratador(idLimpeza, idPessoa));

        return buscar(idLimpeza);
    }

    @Transactional
    public void excluir(int idLimpeza) {
        buscarOuFalhar(idLimpeza);
        // Remove os vínculos N:N antes da limpeza (FKs são ON DELETE NO ACTION).
        limpezaRepository.desvincularEquinos(idLimpeza);
        limpezaRepository.desvincularTratadores(idLimpeza);
        limpezaRepository.delete(idLimpeza);
    }

    private LimpezaRow buscarOuFalhar(int idLimpeza) {
        return limpezaRepository.findById(idLimpeza)
                .orElseThrow(() -> new NotFoundException("Limpeza não encontrada: " + idLimpeza));
    }

    private LimpezaResponse montar(int idLimpeza, LimpezaRow row) {
        return new LimpezaResponse(
                row.idLimpeza(),
                row.dataHora(),
                limpezaRepository.listarEquinos(idLimpeza),
                limpezaRepository.listarTratadores(idLimpeza));
    }
}
