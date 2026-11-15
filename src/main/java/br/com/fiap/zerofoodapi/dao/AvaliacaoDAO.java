package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.Avaliacao;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.model.Transacao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AvaliacaoDAO implements DAO<Avaliacao> {

    private final ConnectionFactory connectionFactory;

    public AvaliacaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Avaliacao salvar(Avaliacao avaliacao) throws SQLException {
        String sql = "INSERT INTO TB_ZF_AVALIACAO (id_avaliacao, id_lojista_avaliador, id_lojista_avaliado, id_transacao, quantidade_estrelas, comentario) " +
                "VALUES (SQ_ZF_AVALIACAO.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_avaliacao"})) {

            stmt.setLong(1, avaliacao.getLojistaAvaliador().getIdLojista());
            stmt.setLong(2, avaliacao.getLojistaAvaliado().getIdLojista());
            stmt.setLong(3, avaliacao.getTransacao().getIdTransacao());
            stmt.setInt(4, avaliacao.getQuantidadeEstrelas());
            stmt.setString(5, avaliacao.getComentario());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    avaliacao.setIdAvaliacao(rs.getLong(1));
                }
            }
            return avaliacao;
        }
    }

    @Override
    public Optional<Avaliacao> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT a.*, " +
                "av.nome_fantasia AS nome_avaliador, " +
                "ad.nome_fantasia AS nome_avaliado " +
                "FROM TB_ZF_AVALIACAO a " +
                "INNER JOIN TB_ZF_LOJISTA av ON a.id_lojista_avaliador = av.id_lojista " +
                "INNER JOIN TB_ZF_LOJISTA ad ON a.id_lojista_avaliado = ad.id_lojista " +
                "WHERE a.id_avaliacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearAvaliacaoCompleta(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Avaliacao> listarTodos() throws SQLException {
        String sql = "SELECT a.*, " +
                "av.nome_fantasia AS nome_avaliador, " +
                "ad.nome_fantasia AS nome_avaliado " +
                "FROM TB_ZF_AVALIACAO a " +
                "INNER JOIN TB_ZF_LOJISTA av ON a.id_lojista_avaliador = av.id_lojista " +
                "INNER JOIN TB_ZF_LOJISTA ad ON a.id_lojista_avaliado = ad.id_lojista " +
                "ORDER BY a.id_avaliacao DESC";
        List<Avaliacao> avaliacoes = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                avaliacoes.add(mapearAvaliacaoCompleta(rs));
            }
        }
        return avaliacoes;
    }

    @Override
    public boolean atualizar(Avaliacao avaliacao) throws SQLException {
        String sql = "UPDATE TB_ZF_AVALIACAO SET quantidade_estrelas = ?, comentario = ? WHERE id_avaliacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, avaliacao.getQuantidadeEstrelas());
            stmt.setString(2, avaliacao.getComentario());
            stmt.setLong(3, avaliacao.getIdAvaliacao());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_AVALIACAO WHERE id_avaliacao = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Avaliacao mapearAvaliacaoCompleta(ResultSet rs) throws SQLException {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(rs.getLong("id_avaliacao"));
        avaliacao.setQuantidadeEstrelas(rs.getInt("quantidade_estrelas"));
        avaliacao.setComentario(rs.getString("comentario"));

        Lojista avaliador = new Lojista();
        avaliador.setIdLojista(rs.getLong("id_lojista_avaliador"));
        avaliador.setNomeFantasia(rs.getString("nome_avaliador"));
        avaliacao.setLojistaAvaliador(avaliador);

        Lojista avaliado = new Lojista();
        avaliado.setIdLojista(rs.getLong("id_lojista_avaliado"));
        avaliado.setNomeFantasia(rs.getString("nome_avaliado"));
        avaliacao.setLojistaAvaliado(avaliado);

        Transacao transacao = new Transacao();
        transacao.setIdTransacao(rs.getLong("id_transacao"));
        avaliacao.setTransacao(transacao);

        return avaliacao;
    }
}
