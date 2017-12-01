package com.maoaberta.vinicius.maoaberta.domain.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinicius on 24/10/2017.
 */

public class Anuncio implements Serializable{

    private String id;
    private String anuncio; //vou usar na apresentação
    private String tipo;
    private String instituicao; //vou usar na apresentação
    private String dataInicio;
    private String dataFim;
    private String idProprietario;
    private String nome;
    private String telefone;
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

    public String getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(String titulo) {
        this.anuncio = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String descricao) {
        this.instituicao = descricao;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
