package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.CategoriaDAO;
import br.com.fiap.zerofoodapi.dao.InsumoDAO;
import br.com.fiap.zerofoodapi.model.Insumo;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class InsumoService {
    private final InsumoDAO insumoDAO;
    private final CategoriaDAO categoriaDAO;

    public InsumoService(InsumoDAO insumoDAO, CategoriaDAO categoriaDAO) {
        this.insumoDAO = insumoDAO;
        this.categoriaDAO = categoriaDAO;
    }

    public Insumo salvar(Insumo insumo) throws SQLException {
        if (categoriaDAO.buscarPorId(insumo.getCategoria().getIdCategoria()).isEmpty()) {
            throw new RuntimeException("Categoria não encontrada.");
        }
        return insumoDAO.salvar(insumo);
    }

    public List<Insumo> listarTodos() throws SQLException {
        return insumoDAO.listarTodos();
    }

    public Optional<Insumo> buscarPorId(Long id) throws SQLException {
        return insumoDAO.buscarPorId(id);
    }

    public boolean atualizar(Insumo insumo) throws SQLException {
        if (insumoDAO.buscarPorId(insumo.getIdInsumo()).isEmpty()) {
            throw new RuntimeException("Insumo não encontrado para atualização.");
        }
        if (categoriaDAO.buscarPorId(insumo.getCategoria().getIdCategoria()).isEmpty()) {
            throw new RuntimeException("Categoria não encontrada.");
        }
        return insumoDAO.atualizar(insumo);
    }

    public boolean excluir(Long id) throws SQLException {
        if (insumoDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Insumo não encontrado para exclusão.");
        }
        return insumoDAO.excluir(id);
    }
}