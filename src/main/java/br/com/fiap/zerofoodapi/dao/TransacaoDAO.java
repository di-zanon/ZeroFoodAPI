package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransacaoDAO implements DAO<Transacao> {

    private final ConnectionFactory connectionFactory;

    public TransacaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Transacao salvar(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO TB_ZF_TRANSACAO (id_transacao, id_anuncio, id_lojista_comprador, data_hora, quantidade_vendida, valor_total, taxa_take_rate, status) " +
                "VALUES (SQ_ZF_TRANSACAO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_transacao"})) {

            stmt.setLong(1, transacao.getAnuncio().getIdAnuncio());
            stmt.setLong(2, transacao.getLojistaComprador().getIdLojista());
            stmt.setTimestamp(3, Timestamp.valueOf(transacao.getDataHora()));
            stmt.setInt(4, transacao.getQuantidadeVendida());
            stmt.setDouble(5, transacao.getValorTotal());
            stmt.setDouble(6, transacao.getTaxaTakeRate());
            stmt.setString(7, String.valueOf(transacao.getStatus()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transacao.setIdTransacao(rs.getLong(1));
                }
            }
            return transacao;
        }
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT t.*, a.id_insumo, i.nome AS nome_insumo, l.nome_fantasia AS nome_comprador " +
                "FROM TB_ZF_TRANSACAO t " +
                "INNER JOIN TB_ZF_ANUNCIO a ON t.id_anuncio = a.id_anuncio " +
                "INNER JOIN TB_ZF_INSUMO i ON a.id_insumo = i.id_insumo " +
                "INNER JOIN TB_ZF_LOJISTA l ON t.id_lojista_comprador = l.id_lojista " +
                "WHERE t.id_transacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearTransacaoCompleta(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Transacao> listarTodos() throws SQLException {
        String sql = "SELECT t.*, a.id_insumo, i.nome AS nome_insumo, l.nome_fantasia AS nome_comprador " +
                "FROM TB_ZF_TRANSACAO t " +
                "INNER JOIN TB_ZF_ANUNCIO a ON t.id_anuncio = a.id_anuncio " +
                "INNER JOIN TB_ZF_INSUMO i ON a.id_insumo = i.id_insumo " +
                "INNER JOIN TB_ZF_LOJISTA l ON t.id_lojista_comprador = l.id_lojista " +
                "ORDER BY t.data_hora DESC";
        List<Transacao> transacoes = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transacoes.add(mapearTransacaoCompleta(rs));
            }
        }
        return transacoes;
    }

    @Override
    public boolean atualizar(Transacao transacao) throws SQLException {
        String sql = "UPDATE TB_ZF_TRANSACAO SET status = ? WHERE id_transacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(transacao.getStatus()));
            stmt.setLong(2, transacao.getIdTransacao());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_TRANSACAO WHERE id_transacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Transacao mapearTransacaoCompleta(ResultSet rs) throws SQLException {
        Transacao t = new Transacao();
        t.setIdTransacao(rs.getLong("id_transacao"));
        t.setQuantidadeVendida(rs.getInt("quantidade_vendida"));
        t.setValorTotal(rs.getDouble("valor_total"));
        t.setTaxaTakeRate(rs.getDouble("taxa_take_rate"));

        Timestamp ts = rs.getTimestamp("data_hora");
        if (ts != null) t.setDataHora(ts.toLocalDateTime());

        String strStatus = rs.getString("status");
        if (strStatus != null && !strStatus.isEmpty()) t.setStatus(strStatus.charAt(0));

        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(rs.getLong("id_anuncio"));

        Insumo insumo = new Insumo();
        insumo.setIdInsumo(rs.getLong("id_insumo"));
        insumo.setNome(rs.getString("nome_insumo"));
        anuncio.setInsumo(insumo);

        t.setAnuncio(anuncio);

        Lojista comprador = new Lojista();
        comprador.setIdLojista(rs.getLong("id_lojista_comprador"));
        comprador.setNomeFantasia(rs.getString("nome_comprador"));
        t.setLojistaComprador(comprador);

        return t;
    }
}