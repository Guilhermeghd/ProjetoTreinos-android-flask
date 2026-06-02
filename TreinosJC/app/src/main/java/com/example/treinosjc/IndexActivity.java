package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_admin);
    }

    public void abrirUsuarios(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void abrirExercicios(View view) {
        startActivity(new Intent(this, ExercicioActivity.class));
    }

    public void abrirTreinos(View view) {
        startActivity(new Intent(this, GerenciarTreinosActivity.class));
    }

    public void abrirVinculoUsuarioTreino(View view) {
        startActivity(new Intent(this, VincularUsuarioTreinoActivity.class));
    }

    public void abrirSelecionarUsuarioAtual(View view) {
        startActivity(new Intent(this, SelecionarUsuarioAtualActivity.class));
    }
}