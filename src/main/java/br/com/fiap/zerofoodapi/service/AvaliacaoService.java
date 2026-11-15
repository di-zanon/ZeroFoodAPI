package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.AvaliacaoDAO;
import br.com.fiap.zerofoodapi.dao.LojistaDAO;
import br.com.fiap.zerofoodapi.dao.TransacaoDAO;
import br.com.fiap.zerofoodapi.model.Avaliacao;
import br.com.fiap.zerofoodapi.model.Transacao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    private final AvaliacaoDAO avaliacaoDAO;
    private final LojistaDAO lojistaDAO;
    private final TransacaoDAO transacaoDAO;

    public AvaliacaoService(AvaliacaoDAO avaliacaoDAO, LojistaDAO lojistaDAO, TransacaoDAO transacaoDAO) {
        this.avaliacaoDAO = avaliacaoDAO;
        this.lojistaDAO = lojistaDAO;
        this.transacaoDAO = transacaoDAO;
    }

    public Avaliacao salvar(Avaliacao avaliacao) throws SQLException {
        // Regra 1: Verificar se os lojistas existem
        if (lojistaDAO.buscarPorId(avaliacao.getLojistaAvaliador().getIdLojista()).isEmpty()) {
            throw new RuntimeException("Lojista avaliador não encontrado.");
        }
        if (lojistaDAO.buscarPorId(avaliacao.getLojistaAvaliado().getIdLojista()).isEmpty()) {
            throw new RuntimeException("Lojista avaliado não encontrado.");
        }

        // Regra 2: Verificar se a transação existe e se o avaliado é o vendedor
        Optional<Transacao> transacaoOpt = transacaoDAO.buscarPorId(avaliacao.getTransacao().getIdTransacao());
        if (transacaoOpt.isEmpty()) {
            throw new RuntimeException("Transação não encontrada para avaliar.");
        }

        Transacao transacao = transacaoOpt.get();
        Long vendedorId = transacao.getAnuncio().getLojista().getIdLojista();

        if (!vendedorId.equals(avaliacao.getLojistaAvaliado().getIdLojista())) {
            throw new RuntimeException("A avaliação deve ser feita ao vendedor da transação.");
        }

        return avaliacaoDAO.salvar(avaliacao);
    }

    public List<Avaliacao> listarTodos() throws SQLException {
        return avaliacaoDAO.listarTodos();
    }

    public Optional<Avaliacao> buscarPorId(Long id) throws SQLException {
        return avaliacaoDAO.buscarPorId(id);
    }

    public boolean atualizar(Avaliacao avaliacao) throws SQLException {
        if (avaliacaoDAO.buscarPorId(avaliacao.getIdAvaliacao()).isEmpty()) {
            throw new RuntimeException("Avaliação não encontrada para atualização.");
        }
        return avaliacaoDAO.atualizar(avaliacao);
    }

    public boolean excluir(Long id) throws SQLException {
        if (avaliacaoDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Avaliação não encontrada para exclusão.");
        }
        return avaliacaoDAO.excluir(id);
    }
}