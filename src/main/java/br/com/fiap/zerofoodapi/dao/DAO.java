package br.com.fiap.zerofoodapi.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    T salvar(T entidade) throws SQLException;
    Optional<T> buscarPorId(Long id) throws SQLException;
    List<T> listarTodos() throws SQLException;
    boolean atualizar(T entidade) throws SQLException;
    boolean excluir(Long id) throws SQLException;
}