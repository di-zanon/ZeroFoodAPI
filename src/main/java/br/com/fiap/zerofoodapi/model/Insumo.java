package br.com.fiap.zerofoodapi.model;

public class Insumo {
    private Long idInsumo;
    private Categoria categoria; // Relacionamento 1:N
    private String nome;
    private String unidadeMedida;

    public Insumo() {}

    public Insumo(Long idInsumo, Categoria categoria, String nome, String unidadeMedida) {
        this.idInsumo = idInsumo;
        this.categoria = categoria;
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
    }

    public Long getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Long idInsumo) { this.idInsumo = idInsumo; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
}