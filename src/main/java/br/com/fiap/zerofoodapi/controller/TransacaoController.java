package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.TransacaoRequestDTO;
import br.com.fiap.zerofoodapi.dto.TransacaoResponseDTO;
import br.com.fiap.zerofoodapi.model.Anuncio;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.model.Transacao;
import br.com.fiap.zerofoodapi.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransacaoResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(TransacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(transacao -> ResponseEntity.ok(new TransacaoResponseDTO(transacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponseDTO criar(@Valid @RequestBody TransacaoRequestDTO dto) throws SQLException {
        Transacao transacao = new Transacao();

        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(dto.idAnuncio());
        transacao.setAnuncio(anuncio);

        Lojista comprador = new Lojista();
        comprador.setIdLojista(dto.idLojistaComprador());
        transacao.setLojistaComprador(comprador);

        transacao.setQuantidadeVendida(dto.quantidadeVendida());

        // Salva (o Service calcula os valores e taxa)
        Transacao salva = service.salvar(transacao);

        // Recupera do banco com os JOINs
        Transacao completa = service.buscarPorId(salva.getIdTransacao())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar a transação salva"));

        return new TransacaoResponseDTO(completa);
    }

    // No caso de transações, o PUT é usado apenas para atualizar o Status (ex: Pagamento confirmado)
    @PatchMapping("/{id}/status")
    public ResponseEntity<TransacaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam Character status) throws SQLException {

        service.atualizarStatus(id, status);

        Transacao atualizada = service.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar a transação atualizada"));

        return ResponseEntity.ok(new TransacaoResponseDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) throws SQLException {
        service.excluir(id);
    }
}