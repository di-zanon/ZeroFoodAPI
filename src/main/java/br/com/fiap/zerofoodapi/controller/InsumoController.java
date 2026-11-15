package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.InsumoRequestDTO;
import br.com.fiap.zerofoodapi.dto.InsumoResponseDTO;
import br.com.fiap.zerofoodapi.model.Categoria;
import br.com.fiap.zerofoodapi.model.Insumo;
import br.com.fiap.zerofoodapi.service.InsumoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    private final InsumoService service;

    public InsumoController(InsumoService service) {
        this.service = service;
    }

    @GetMapping
    public List<InsumoResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(InsumoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(insumo -> ResponseEntity.ok(new InsumoResponseDTO(insumo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsumoResponseDTO criar(@Valid @RequestBody InsumoRequestDTO dto) throws SQLException {
        Insumo insumo = new Insumo();

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.idCategoria());
        insumo.setCategoria(categoria);

        insumo.setNome(dto.nome());
        insumo.setUnidadeMedida(dto.unidadeMedida());

        Insumo salvo = service.salvar(insumo);

        // Busca do banco novamente para trazer o nome da categoria preenchido
        Insumo insumoCompleto = service.buscarPorId(salvo.getIdInsumo())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar o insumo salvo"));

        return new InsumoResponseDTO(insumoCompleto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody InsumoRequestDTO dto) throws SQLException {
        return service.buscarPorId(id)
                .map(insumoExistente -> {
                    try {
                        insumoExistente.getCategoria().setIdCategoria(dto.idCategoria());
                        insumoExistente.setNome(dto.nome());
                        insumoExistente.setUnidadeMedida(dto.unidadeMedida());

                        service.atualizar(insumoExistente);

                        Insumo insumoAtualizado = service.buscarPorId(id)
                                .orElseThrow(() -> new RuntimeException("Erro ao recuperar o insumo atualizado"));

                        return ResponseEntity.ok(new InsumoResponseDTO(insumoAtualizado));
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao atualizar insumo", e);
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