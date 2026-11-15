package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.CategoriaRequestDTO;
import br.com.fiap.zerofoodapi.dto.CategoriaResponseDTO;
import br.com.fiap.zerofoodapi.model.Categoria;
import br.com.fiap.zerofoodapi.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(CategoriaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(categoria -> ResponseEntity.ok(new CategoriaResponseDTO(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criar(@Valid @RequestBody CategoriaRequestDTO dto) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.nomeCategoria());

        Categoria salva = service.salvar(categoria);
        return new CategoriaResponseDTO(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) throws SQLException {
        return service.buscarPorId(id)
                .map(categoriaExistente -> {
                    try {
                        categoriaExistente.setNomeCategoria(dto.nomeCategoria());
                        service.atualizar(categoriaExistente);
                        return ResponseEntity.ok(new CategoriaResponseDTO(categoriaExistente));
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao atualizar categoria", e);
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