package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.Categoria;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaDAO implements DAO<Categoria> {

    private final ConnectionFactory connectionFactory;

    public CategoriaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Categoria salvar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO TB_ZF_CATEGORIA (id_categoria, categoria) VALUES (SQ_ZF_CATEGORIA.NEXTVAL, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_categoria"})) {

            stmt.setString(1, categoria.getNomeCategoria());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getLong(1));
                }
            }
            return categoria;
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM TB_ZF_CATEGORIA WHERE id_categoria = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearCategoria(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Categoria> listarTodos() throws SQLException {
        String sql = "SELECT * FROM TB_ZF_CATEGORIA ORDER BY categoria";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) categorias.add(mapearCategoria(rs));
        }
        return categorias;
    }

    @Override
    public boolean atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE TB_ZF_CATEGORIA SET categoria = ? WHERE id_categoria = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNomeCategoria());
            stmt.setLong(2, categoria.getIdCategoria());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_CATEGORIA WHERE id_categoria = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getLong("id_categoria"));
        c.setNomeCategoria(rs.getString("categoria"));
        return c;
    }
}