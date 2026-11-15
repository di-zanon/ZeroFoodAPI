package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.AnuncioDAO;
import br.com.fiap.zerofoodapi.dao.LojistaDAO;
import br.com.fiap.zerofoodapi.model.Anuncio;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    private final AnuncioDAO anuncioDAO;
    private final LojistaDAO lojistaDAO;

    public AnuncioService(AnuncioDAO anuncioDAO, LojistaDAO lojistaDAO) {
        this.anuncioDAO = anuncioDAO;
        this.lojistaDAO = lojistaDAO;
    }

    public Anuncio salvar(Anuncio anuncio) throws SQLException {
        if (lojistaDAO.buscarPorId(anuncio.getLojista().getIdLojista()).isEmpty()) {
            throw new RuntimeException("Não é possível criar um anúncio para um lojista inexistente.");
        }

        if (anuncio.getStatus() == null) {
            anuncio.setStatus('A');
        }

        return anuncioDAO.salvar(anuncio);
    }

    public List<Anuncio> listarTodos() throws SQLException {
        return anuncioDAO.listarTodos();
    }

    public Optional<Anuncio> buscarPorId(Long id) throws SQLException {
        return anuncioDAO.buscarPorId(id);
    }

    public boolean atualizar(Anuncio anuncio) throws SQLException {
        if (anuncioDAO.buscarPorId(anuncio.getIdAnuncio()).isEmpty()) {
            throw new RuntimeException("Anúncio não encontrado para atualização.");
        }
        return anuncioDAO.atualizar(anuncio);
    }

    public boolean excluir(Long id) throws SQLException {
        if (anuncioDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Anúncio não encontrado para exclusão.");
        }
        return anuncioDAO.excluir(id);
    }
}