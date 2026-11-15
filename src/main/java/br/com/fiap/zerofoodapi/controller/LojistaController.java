package br.com.fiap.zerofoodapi.controller;

import br.com.fiap.zerofoodapi.dto.LojistaRequestDTO;
import br.com.fiap.zerofoodapi.dto.LojistaResponseDTO;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.service.LojistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lojistas")
public class LojistaController {

    private final LojistaService service;

    public LojistaController(LojistaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LojistaResponseDTO> listar() throws SQLException {
        return service.listarTodos().stream()
                .map(LojistaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LojistaResponseDTO> buscarPorId(@PathVariable Long id) throws SQLException {
        return service.buscarPorId(id)
                .map(lojista -> ResponseEntity.ok(new LojistaResponseDTO(lojista)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LojistaResponseDTO criar(@Valid @RequestBody LojistaRequestDTO dto) throws SQLException {
        // Converte o DTO de Request para a Entidade
        Lojista lojista = new Lojista();
        lojista.setRazaoSocial(dto.razaoSocial());
        lojista.setNomeFantasia(dto.nomeFantasia());
        lojista.setCnpj(dto.cnpj());
        lojista.setEmail(dto.email());
        lojista.setTelefone(dto.telefone());
        lojista.setEndereco(dto.endereco());

        Lojista salvo = service.salvar(lojista);
        return new LojistaResponseDTO(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LojistaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LojistaRequestDTO dto) throws SQLException {
        return service.buscarPorId(id)
                .map(lojistaExistente -> {
                    try {
                        lojistaExistente.setRazaoSocial(dto.razaoSocial());
                        lojistaExistente.setNomeFantasia(dto.nomeFantasia());
                        lojistaExistente.setCnpj(dto.cnpj());
                        lojistaExistente.setEmail(dto.email());
                        lojistaExistente.setTelefone(dto.telefone());
                        lojistaExistente.setEndereco(dto.endereco());

                        service.atualizar(lojistaExistente);
                        return ResponseEntity.ok(new LojistaResponseDTO(lojistaExistente));
                    } catch (SQLException e) {
                        throw new RuntimeException("Erro ao atualizar lojista", e);
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