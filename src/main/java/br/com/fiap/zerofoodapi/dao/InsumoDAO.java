package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.Categoria;
import br.com.fiap.zerofoodapi.model.Insumo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InsumoDAO implements DAO<Insumo> {

    private final ConnectionFactory connectionFactory;

    public InsumoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Insumo salvar(Insumo insumo) throws SQLException {
        String sql = "INSERT INTO TB_ZF_INSUMO (id_insumo, id_categoria, nome, unidade_medida) VALUES (SQ_ZF_INSUMO.NEXTVAL, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_insumo"})) {
            stmt.setLong(1, insumo.getCategoria().getIdCategoria());
            stmt.setString(2, insumo.getNome());
            stmt.setString(3, insumo.getUnidadeMedida());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) insumo.setIdInsumo(rs.getLong(1));
            }
            return insumo;
        }
    }

    @Override
    public Optional<Insumo> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT i.*, c.categoria FROM TB_ZF_INSUMO i " +
                "INNER JOIN TB_ZF_CATEGORIA c ON i.id_categoria = c.id_categoria " +
                "WHERE i.id_insumo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearInsumoCompleto(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Insumo> listarTodos() throws SQLException {
        String sql = "SELECT i.*, c.categoria FROM TB_ZF_INSUMO i " +
                "INNER JOIN TB_ZF_CATEGORIA c ON i.id_categoria = c.id_categoria " +
                "ORDER BY i.nome";
        List<Insumo> insumos = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) insumos.add(mapearInsumoCompleto(rs));
        }
        return insumos;
    }

    @Override
    public boolean atualizar(Insumo insumo) throws SQLException {
        String sql = "UPDATE TB_ZF_INSUMO SET id_categoria = ?, nome = ?, unidade_medida = ? WHERE id_insumo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, insumo.getCategoria().getIdCategoria());
            stmt.setString(2, insumo.getNome());
            stmt.setString(3, insumo.getUnidadeMedida());
            stmt.setLong(4, insumo.getIdInsumo());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_INSUMO WHERE id_insumo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Insumo mapearInsumoCompleto(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setIdInsumo(rs.getLong("id_insumo"));
        insumo.setNome(rs.getString("nome"));
        insumo.setUnidadeMedida(rs.getString("unidade_medida"));

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getLong("id_categoria"));
        categoria.setNomeCategoria(rs.getString("categoria"));
        insumo.setCategoria(categoria);

        return insumo;
    }
}