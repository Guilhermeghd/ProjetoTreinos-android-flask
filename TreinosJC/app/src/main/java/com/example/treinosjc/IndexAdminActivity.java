package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IndexAdminActivity extends AppCompatActivity {

    int usuarioId;
    String usuarioNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_admin);

        usuarioId = getIntent().getIntExtra("usuarioId", 0);
        usuarioNome = getIntent().getStringExtra("usuarioNome");

        TextView txtBemVindo = findViewById(R.id.txtBemVindo);
        txtBemVindo.setText("Ola, " + usuarioNome + " admin");
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

    public void sair(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
