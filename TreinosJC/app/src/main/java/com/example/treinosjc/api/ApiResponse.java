package com.example.treinosjc.api;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("INSERT")
    private String insert;

    @SerializedName("UPDATE")
    private String update;

    @SerializedName("DELETE")
    private String delete;

    @SerializedName("LOGIN")
    private String login;

    @SerializedName("MSG")
    private String msg;

    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;


    @SerializedName("duplicado")
    private boolean duplicado;

    @SerializedName("treinoNome")
    private String treinoNome;



    public boolean insertOk() { return "OK".equals(insert); }
    public boolean updateOk() { return "OK".equals(update); }
    public boolean deleteOk() { return "OK".equals(delete); }
    public boolean loginOk() { return "OK".equals(login);  }
    public boolean isAdmin() { return "admin".equals(role); }
    public boolean isDuplicado()   { return duplicado;   }
    public String  getTreinoNome() { return treinoNome;  }

    public String getMsg() { return msg; }

    public int getId() { return id; }

    public String getNome() { return nome; }

    public String getEmail() { return email; }
    public String getRole()  { return role; }
}
