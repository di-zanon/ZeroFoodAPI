package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.CategoriaDAO;
import br.com.fiap.zerofoodapi.model.Categoria;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public Categoria salvar(Categoria categoria) throws SQLException {
        // REGRA DE NEGÓCIO: Evitar nomes de categorias duplicados
        boolean categoriaJaExiste = categoriaDAO.listarTodos().stream()
                .anyMatch(c -> c.getNomeCategoria().equalsIgnoreCase(categoria.getNomeCategoria()));

        if (categoriaJaExiste) {
            throw new RuntimeException("Já existe uma categoria cadastrada com este nome.");
        }

        return categoriaDAO.salvar(categoria);
    }

    public List<Categoria> listarTodos() throws SQLException {
        return categoriaDAO.listarTodos();
    }

    public Optional<Categoria> buscarPorId(Long id) throws SQLException {
        return categoriaDAO.buscarPorId(id);
    }

    public boolean atualizar(Categoria categoria) throws SQLException {
        if (categoriaDAO.buscarPorId(categoria.getIdCategoria()).isEmpty()) {
            throw new RuntimeException("Categoria não encontrada para atualização.");
        }
        return categoriaDAO.atualizar(categoria);
    }

    public boolean excluir(Long id) throws SQLException {
        if (categoriaDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Categoria não encontrada para exclusão.");
        }
        return categoriaDAO.excluir(id);
    }
}