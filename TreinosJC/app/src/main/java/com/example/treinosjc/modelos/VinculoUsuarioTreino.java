package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;

public class VinculoUsuarioTreino {
    @SerializedName("usuarioId")
    private int usuarioId;

    @SerializedName("usuarioNome")
    private String usuarioNome;

    @SerializedName("treinoId")
    private int treinoId;

    @SerializedName("treinoNome")
    private String treinoNome;

    public VinculoUsuarioTreino(int usuarioId, String usuarioNome, int treinoId,  String treinoNome) {
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.treinoId = treinoId;
        this.treinoNome = treinoNome;
    }

    public int getUsuarioId() { return usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }

    public int getTreinoId() { return treinoId; }
    public String getTreinoNome() { return treinoNome; }

    public String getDescricao() {
        return usuarioNome + " com " + treinoNome;
    }
}
