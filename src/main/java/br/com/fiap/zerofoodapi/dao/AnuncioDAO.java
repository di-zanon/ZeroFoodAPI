package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.Anuncio;
import br.com.fiap.zerofoodapi.model.Categoria;
import br.com.fiap.zerofoodapi.model.Insumo;
import br.com.fiap.zerofoodapi.model.Lojista;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnuncioDAO implements DAO<Anuncio> {

    private final ConnectionFactory connectionFactory;

    public AnuncioDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Anuncio salvar(Anuncio anuncio) throws SQLException {
        String sql = "INSERT INTO TB_ZF_ANUNCIO (id_anuncio, id_lojista, id_insumo, quantidade_estoque, valor_unitario, data_validade, status) " +
                "VALUES (SQ_ZF_ANUNCIO.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_anuncio"})) {

            stmt.setLong(1, anuncio.getLojista().getIdLojista());
            stmt.setLong(2, anuncio.getInsumo().getIdInsumo());
            stmt.setInt(3, anuncio.getQuantidadeEstoque());
            stmt.setDouble(4, anuncio.getValorUnitario());
            stmt.setDate(5, Date.valueOf(anuncio.getDataValidade()));
            stmt.setString(6, String.valueOf(anuncio.getStatus() != null ? anuncio.getStatus() : 'A'));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    anuncio.setIdAnuncio(rs.getLong(1));
                }
            }
            return anuncio;
        }
    }

    @Override
    public Optional<Anuncio> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT a.*, l.nome_fantasia, i.id_insumo, i.nome AS nome_insumo, i.unidade_medida, i.id_categoria, c.categoria " +
                "FROM TB_ZF_ANUNCIO a " +
                "INNER JOIN TB_ZF_LOJISTA l ON a.id_lojista = l.id_lojista " +
                "INNER JOIN TB_ZF_INSUMO i ON a.id_insumo = i.id_insumo " +
                "INNER JOIN TB_ZF_CATEGORIA c ON i.id_categoria = c.id_categoria " +
                "WHERE a.id_anuncio = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearAnuncioCompleto(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Anuncio> listarTodos() throws SQLException {
        String sql = "SELECT a.*, l.nome_fantasia, i.id_insumo, i.nome AS nome_insumo, i.unidade_medida, i.id_categoria, c.categoria " +
                "FROM TB_ZF_ANUNCIO a " +
                "INNER JOIN TB_ZF_LOJISTA l ON a.id_lojista = l.id_lojista " +
                "INNER JOIN TB_ZF_INSUMO i ON a.id_insumo = i.id_insumo " +
                "INNER JOIN TB_ZF_CATEGORIA c ON i.id_categoria = c.id_categoria " +
                "ORDER BY a.id_anuncio DESC";
        List<Anuncio> anuncios = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                anuncios.add(mapearAnuncioCompleto(rs));
            }
        }
        return anuncios;
    }

    @Override
    public boolean atualizar(Anuncio anuncio) throws SQLException {
        String sql = "UPDATE TB_ZF_ANUNCIO SET id_lojista = ?, id_insumo = ?, quantidade_estoque = ?, valor_unitario = ?, data_validade = ?, status = ? WHERE id_anuncio = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, anuncio.getLojista().getIdLojista());
            stmt.setLong(2, anuncio.getInsumo().getIdInsumo());
            stmt.setInt(3, anuncio.getQuantidadeEstoque());
            stmt.setDouble(4, anuncio.getValorUnitario());
            stmt.setDate(5, Date.valueOf(anuncio.getDataValidade()));
            stmt.setString(6, String.valueOf(anuncio.getStatus()));
            stmt.setLong(7, anuncio.getIdAnuncio());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_ANUNCIO WHERE id_anuncio = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Anuncio mapearAnuncioCompleto(ResultSet rs) throws SQLException {
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(rs.getLong("id_anuncio"));
        anuncio.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        anuncio.setValorUnitario(rs.getDouble("valor_unitario"));

        Date sqlDate = rs.getDate("data_validade");
        if (sqlDate != null) {
            anuncio.setDataValidade(sqlDate.toLocalDate());
        }

        String strStatus = rs.getString("status");
        if (strStatus != null && !strStatus.isEmpty()) {
            anuncio.setStatus(strStatus.charAt(0));
        }

        Lojista lojista = new Lojista();
        lojista.setIdLojista(rs.getLong("id_lojista"));
        lojista.setNomeFantasia(rs.getString("nome_fantasia"));
        anuncio.setLojista(lojista);

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getLong("id_categoria"));
        categoria.setNomeCategoria(rs.getString("categoria"));

        Insumo insumo = new Insumo();
        insumo.setIdInsumo(rs.getLong("id_insumo"));
        insumo.setNome(rs.getString("nome_insumo"));
        insumo.setUnidadeMedida(rs.getString("unidade_medida"));
        insumo.setCategoria(categoria);
        anuncio.setInsumo(insumo);

        return anuncio;
    }
}