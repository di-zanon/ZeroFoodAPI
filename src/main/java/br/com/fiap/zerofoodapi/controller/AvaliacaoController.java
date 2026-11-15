package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.AvaliacaoRequestDTO;
import br.com.fiap.zerofoodapi.dto.AvaliacaoResponseDTO;
import br.com.fiap.zerofoodapi.model.Avaliacao;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.model.Transacao;
import br.com.fiap.zerofoodapi.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService service;

    public AvaliacaoController(AvaliacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AvaliacaoResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(avaliacao -> ResponseEntity.ok(new AvaliacaoResponseDTO(avaliacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponseDTO criar(@Valid @RequestBody AvaliacaoRequestDTO dto) throws SQLException {
        Avaliacao avaliacao = new Avaliacao();

        Lojista avaliador = new Lojista();
        avaliador.setIdLojista(dto.idLojistaAvaliador());
        avaliacao.setLojistaAvaliador(avaliador);

        Lojista avaliado = new Lojista();
        avaliado.setIdLojista(dto.idLojistaAvaliado());
        avaliacao.setLojistaAvaliado(avaliado);

        Transacao transacao = new Transacao();
        transacao.setIdTransacao(dto.idTransacao());
        avaliacao.setTransacao(transacao);

        avaliacao.setQuantidadeEstrelas(dto.quantidadeEstrelas());
        avaliacao.setComentario(dto.comentario());

        Avaliacao salva = service.salvar(avaliacao);

        Avaliacao completa = service.buscarPorId(salva.getIdAvaliacao())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar avaliação salva"));

        return new AvaliacaoResponseDTO(completa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequestDTO dto) throws SQLException {
        return service.buscarPorId(id)
                .map(avaliacaoExistente -> {
                    try {
                        avaliacaoExistente.setQuantidadeEstrelas(dto.quantidadeEstrelas());
                        avaliacaoExistente.setComentario(dto.comentario());
                        service.atualizar(avaliacaoExistente);

                        Avaliacao atualizada = service.buscarPorId(id)
                                .orElseThrow(() -> new RuntimeException("Erro ao recuperar avaliação atualizada"));

                        return ResponseEntity.ok(new AvaliacaoResponseDTO(atualizada));
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao atualizar avaliação", e);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) throws SQLException {
        service.excluir(id);
    }
}