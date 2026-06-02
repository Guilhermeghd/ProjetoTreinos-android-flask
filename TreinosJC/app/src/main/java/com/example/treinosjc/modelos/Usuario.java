package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Usuario implements Serializable {
    @SerializedName("id")
    private int Id;

    @SerializedName("nome")
    private String Nome;
    @SerializedName("email")
    private String Email;
    @SerializedName("senha")
    private String Senha;

    public Usuario() {}

    public Usuario(int id, String nome, String email) {
        Id = id;
        Nome = nome;
        Email = email;
    }

    public Usuario(String senha, int id, String nome, String email) {
        Senha = senha;
        Id = id;
        Nome = nome;
        Email = email;
    }

    public Usuario(String nome, String email) {
        Nome = nome;
        Email = email;
        Senha = "";
    }

    public int getId() { return Id; }

    public void setId(int id) { Id = id; }

    public String getNome() { return Nome;  }

    public void setNome(String n) { Nome = n; }

    public String getEmail() { return Email; }

    public void setEmail(String e) { Email = e; }


    public String getSenha() { return Senha != null ? Senha : ""; }
    public void setSenha(String s) { Senha = s; }
}
