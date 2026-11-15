package br.com.fiap.zerofoodapi.model;

import java.time.LocalDate;

public class Anuncio {

    private Long idAnuncio;
    private Lojista lojista;
    private Insumo insumo; // Relacionamento com o Insumo
    private Integer quantidadeEstoque;
    private Double valorUnitario;
    private LocalDate dataValidade;
    private Character status;

    public Anuncio() {}

    public Anuncio(Long idAnuncio, Lojista lojista, Insumo insumo, Integer quantidadeEstoque, Double valorUnitario, LocalDate dataValidade, Character status) {
        this.idAnuncio = idAnuncio;
        this.lojista = lojista;
        this.insumo = insumo;
        this.quantidadeEstoque = quantidadeEstoque;
        this.valorUnitario = valorUnitario;
        this.dataValidade = dataValidade;
        this.status = status;
    }

    // Getters e Setters
    public Long getIdAnuncio() { return idAnuncio; }
    public void setIdAnuncio(Long idAnuncio) { this.idAnuncio = idAnuncio; }

    public Lojista getLojista() { return lojista; }
    public void setLojista(Lojista lojista) { this.lojista = lojista; }

    public Insumo getInsumo() { return insumo; }
    public void setInsumo(Insumo insumo) { this.insumo = insumo; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(Double valorUnitario) { this.valorUnitario = valorUnitario; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public Character getStatus() { return status; }
    public void setStatus(Character status) { this.status = status; }
}