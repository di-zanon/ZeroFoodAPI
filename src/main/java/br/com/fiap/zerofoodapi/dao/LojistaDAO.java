package br.com.fiap.zerofoodapi.dao;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.model.Lojista;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LojistaDAO implements DAO<Lojista> {

    private final ConnectionFactory connectionFactory;

    // Injeção de dependência via construtor
    public LojistaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Lojista salvar(Lojista lojista) throws SQLException {
        String sql = "INSERT INTO TB_ZF_LOJISTA (id_lojista, razao_social, nome_fantasia, cnpj, email, telefone, endereco) " +
                "VALUES (SQ_ZF_LOJISTA.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_lojista"})) {

            stmt.setString(1, lojista.getRazaoSocial());
            stmt.setString(2, lojista.getNomeFantasia());
            stmt.setString(3, lojista.getCnpj());
            stmt.setString(4, lojista.getEmail());
            stmt.setString(5, lojista.getTelefone());
            stmt.setString(6, lojista.getEndereco());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lojista.setIdLojista(rs.getLong(1));
                }
            }
            return lojista;
        }
    }

    @Override
    public Optional<Lojista> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM TB_ZF_LOJISTA WHERE id_lojista = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLojista(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Lojista> listarTodos() throws SQLException {
        String sql = "SELECT * FROM TB_ZF_LOJISTA ORDER BY razao_social";
        List<Lojista> lojistas = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lojistas.add(mapearLojista(rs));
            }
        }
        return lojistas;
    }

    @Override
    public boolean atualizar(Lojista lojista) throws SQLException {
        String sql = "UPDATE TB_ZF_LOJISTA SET razao_social = ?, nome_fantasia = ?, cnpj = ?, email = ?, telefone = ?, endereco = ? WHERE id_lojista = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lojista.getRazaoSocial());
            stmt.setString(2, lojista.getNomeFantasia());
            stmt.setString(3, lojista.getCnpj());
            stmt.setString(4, lojista.getEmail());
            stmt.setString(5, lojista.getTelefone());
            stmt.setString(6, lojista.getEndereco());
            stmt.setLong(7, lojista.getIdLojista());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM TB_ZF_LOJISTA WHERE id_lojista = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Optional<Lojista> buscarPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM TB_ZF_LOJISTA WHERE cnpj = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLojista(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Lojista mapearLojista(ResultSet rs) throws SQLException {
        Lojista lojista = new Lojista();
        lojista.setIdLojista(rs.getLong("id_lojista"));
        lojista.setRazaoSocial(rs.getString("razao_social"));
        lojista.setNomeFantasia(rs.getString("nome_fantasia"));
        lojista.setCnpj(rs.getString("cnpj"));
        lojista.setEmail(rs.getString("email"));
        lojista.setTelefone(rs.getString("telefone"));
        lojista.setEndereco(rs.getString("endereco"));
        return lojista;
    }
}