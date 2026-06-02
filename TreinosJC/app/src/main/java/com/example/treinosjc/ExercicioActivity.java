package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.ExercicioAdapter;
import com.example.treinosjc.dao.ExercicioDAO;
import com.example.treinosjc.modelos.Exercicio;

import java.util.ArrayList;
import java.util.List;

public class ExercicioActivity extends AppCompatActivity {

    private ListView lsvExercicios;
    private List<Exercicio> lista = new ArrayList<>();
    private ExercicioDAO eDAO = new ExercicioDAO();
    private int treinoId;

    ActivityResultLauncher<Intent> detalheLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_exercicios);

        lsvExercicios = findViewById(R.id.lsvExercicios);
        treinoId = getIntent().getIntExtra("treinoId", 0);

        detalheLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        carregarLista();
                    }
                }
        );
        carregarLista();
    }

    private void carregarLista() {
        if (treinoId > 0) {
            eDAO.ListarPorTreino(treinoId, new ExercicioDAO.ExercicioCallback() {
                @Override
                public void onSucesso(List<Exercicio> resultado) {
                    lista = resultado;
                    montarAdapter();
                }
                @Override
                public void onErro(String msg) {
                    Toast.makeText(ExercicioActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            eDAO.ListarTudo(new ExercicioDAO.ExercicioCallback() {
                @Override
                public void onSucesso(List<Exercicio> resultado) {
                    lista = resultado;
                    montarAdapter();
                }
                @Override
                public void onErro(String msg) {
                    Toast.makeText(ExercicioActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void montarAdapter() {
        ExercicioAdapter adapter = new ExercicioAdapter(this, R.layout.exercicio_item, lista);
        lsvExercicios.setAdapter(adapter);
        lsvExercicios.setOnItemClickListener((list, view, posicao, id) -> {
            Intent intent = new Intent(this, ExercicioDetalheActivity.class);
            Exercicio selecionado = lista.get(posicao);
            intent.putExtra("exercicio", selecionado);
            intent.putExtra("treinoId", treinoId > 0 ? treinoId : selecionado.getTreinoId());
            detalheLauncher.launch(intent);
        });
    }

    public void NovoClique(View view) {
        Intent intent = new Intent(this, ExercicioDetalheActivity.class);
        intent.putExtra("exercicio", new Exercicio());
        if (treinoId > 0) intent.putExtra("treinoId", treinoId);
        detalheLauncher.launch(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }
}
