package com.maoaberta.vinicius.maoaberta.domain.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinicius on 24/10/2017.
 */

public class Anuncio implements Serializable{

    private String id;
    private String titulo;
    private String tipo;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private String idProprietario;
    private HashMap<String, Boolean> interessados;

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

    public HashMap<String, Boolean> getInteressados() {
        return interessados;
    }

    public void setInteressados(HashMap<String, Boolean> interessados) {
        this.interessados = interessados;
    }
}
