package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Treino implements Serializable {

    @SerializedName("id")
    private int Id;
    @SerializedName("nome")
    private String Nome;

    @SerializedName("descricao")
    private String Descricao;
    private int UsuarioId;

    public Treino() {}

    public Treino(int id, String nome, String descricao, int usuarioId) {
        Id = id;
        Nome = nome;
        Descricao = descricao;
        UsuarioId = usuarioId;
    }

    public Treino(String nome, String descricao, int usuarioId) {
        Nome = nome;
        Descricao = descricao;
        UsuarioId = usuarioId;
    }

    public int getId() { return Id; }

    public void setId(int id) { Id = id; }

    public String getNome() { return Nome; }

    public void setNome(String n) { Nome = n; }
    public String getDescricao() { return Descricao; }
    public void setDescricao(String d){ Descricao = d;}

    public int getUsuarioId() { return UsuarioId; }

    public void setUsuarioId(int uid) { UsuarioId = uid; }
}
