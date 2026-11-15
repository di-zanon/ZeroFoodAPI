package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.AnuncioRequestDTO;
import br.com.fiap.zerofoodapi.dto.AnuncioResponseDTO;
import br.com.fiap.zerofoodapi.model.Anuncio;
import br.com.fiap.zerofoodapi.model.Insumo;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.service.AnuncioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/anuncios")
public class AnuncioController {

    private final AnuncioService service;

    public AnuncioController(AnuncioService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnuncioResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(AnuncioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(anuncio -> ResponseEntity.ok(new AnuncioResponseDTO(anuncio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnuncioResponseDTO criar(@Valid @RequestBody AnuncioRequestDTO dto) throws SQLException {
        Anuncio anuncio = new Anuncio();

        Lojista lojista = new Lojista();
        lojista.setIdLojista(dto.idLojista());
        anuncio.setLojista(lojista);

        Insumo insumo = new Insumo();
        insumo.setIdInsumo(dto.idInsumo());
        anuncio.setInsumo(insumo);

        anuncio.setQuantidadeEstoque(dto.quantidadeEstoque());
        anuncio.setValorUnitario(dto.valorUnitario());
        anuncio.setDataValidade(dto.dataValidade());

        // 1. Salva o anúncio no banco (gera o ID)
        Anuncio salvo = service.salvar(anuncio);

        // 2. Busca o anúncio salvo no banco novamente (para trazer os JOINs completos)
        Anuncio anuncioCompleto = service.buscarPorId(salvo.getIdAnuncio())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar o anúncio salvo"));

        // 3. Retorna o DTO preenchido corretamente
        return new AnuncioResponseDTO(anuncioCompleto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AnuncioRequestDTO dto) throws SQLException {
        return service.buscarPorId(id)
                .map(anuncioExistente -> {
                    try {
                        // 1. Atualiza os dados na memória
                        anuncioExistente.getLojista().setIdLojista(dto.idLojista());
                        anuncioExistente.getInsumo().setIdInsumo(dto.idInsumo());
                        anuncioExistente.setQuantidadeEstoque(dto.quantidadeEstoque());
                        anuncioExistente.setValorUnitario(dto.valorUnitario());
                        anuncioExistente.setDataValidade(dto.dataValidade());

                        // 2. Salva no banco
                        service.atualizar(anuncioExistente);

                        // 3. Busca novamente no banco para trazer os JOINs completos (nomes atualizados)
                        Anuncio anuncioAtualizado = service.buscarPorId(id)
                                .orElseThrow(() -> new RuntimeException("Erro ao recuperar o anúncio atualizado"));

                        // 4. Retorna o DTO preenchido corretamente
                        return ResponseEntity.ok(new AnuncioResponseDTO(anuncioAtualizado));
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao atualizar anúncio", e);
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