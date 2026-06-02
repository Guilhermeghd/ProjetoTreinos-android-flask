package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HistoricoSemana {

    @SerializedName("diasTreinados")
    private List<Integer> diasTreinados;

    @SerializedName("total")
    private int total;

    public List<Integer> getDiasTreinados() { return diasTreinados; }
    public int getTotal() { return total; }

    public boolean treinouNoDia(int dia) {
        return diasTreinados != null && diasTreinados.contains(dia);
    }
}
