package com.example.treinosjc.modelos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Exercicio implements Serializable {

    @SerializedName("id")
    private int Id;

    @SerializedName("nome")
    private String Nome;

    @SerializedName("series")
    private int Series;

    @SerializedName("repeticoes")
    private int Repeticoes;

    private int TreinoId;

    public Exercicio() {}

    public Exercicio(int id, String nome, int series, int repeticoes, int treinoId) {
        Id = id;
        Nome = nome;
        Series = series;
        Repeticoes = repeticoes;
        TreinoId = treinoId;
    }

    public Exercicio(String nome, int series, int repeticoes, int treinoId) {
        Nome = nome;
        Series = series;
        Repeticoes = repeticoes;
        TreinoId = treinoId;
    }

    public int getId(){ return Id;}

    public void setId(int id) { Id = id; }

    public String getNome() { return Nome; }

    public void setNome(String n) { Nome = n; }
    public int getSeries(){ return Series; }
    public void setSeries(int s) { Series = s; }
    public int getRepeticoes() { return Repeticoes; }
    public void setRepeticoes(int r) { Repeticoes = r; }

    public int getTreinoId() { return TreinoId; }

    public void setTreinoId(int tid) { TreinoId = tid; }

}
