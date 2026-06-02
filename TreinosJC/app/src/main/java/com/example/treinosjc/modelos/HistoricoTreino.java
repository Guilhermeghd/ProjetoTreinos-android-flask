package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;

public class HistoricoTreino {

    @SerializedName("id")
    private int id;

    @SerializedName("usuarioId")
    private int usuarioId;

    @SerializedName("treinoId")
    private int treinoId;

    @SerializedName("treinoNome")
    private String treinoNome;

    @SerializedName("data")
    private String data;

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getTreinoId(){ return treinoId; }
    public String getTreinoNome() { return treinoNome; }
    public String getData() { return data; }

    public String getDataFormatada() {
        if (data == null || data.length() < 10) return "";
        String[] partes = data.substring(0, 10).split("-");
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }
    public String getHora() {
        if (data == null || data.length() < 16) return "";
        return data.substring(11, 16);
    }
}
