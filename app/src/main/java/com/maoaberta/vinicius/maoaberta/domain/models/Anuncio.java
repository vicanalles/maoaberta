package com.maoaberta.vinicius.maoaberta.domain.models;

/**
 * Created by Vinicius on 24/10/2017.
 */

public class Anuncio {

    private String id;
    private String titulo;
    private String tipo;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private String idProprietario;

    public String getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(String idProprietario) {
        this.idProprietario = idProprietario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
