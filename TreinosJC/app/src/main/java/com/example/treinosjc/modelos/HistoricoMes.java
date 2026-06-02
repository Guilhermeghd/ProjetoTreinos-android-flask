package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HistoricoMes {

    @SerializedName("ano")
    private int ano;

    @SerializedName("mes")
    private int mes;

    @SerializedName("diasTreinados")
    private List<Integer> diasTreinados;

    public int getAno() { return ano; }
    public int getMes() { return mes; }
    public List<Integer> getDiasTreinados() { return diasTreinados; }

    public boolean treinouNoDia(int dia) {
        return diasTreinados != null && diasTreinados.contains(dia);
    }
}
