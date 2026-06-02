package com.example.treinosjc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.ProgressoTreinoAdapter;
import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.dao.ExercicioDAO;
import com.example.treinosjc.modelos.Exercicio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressoTreinoActivity extends AppCompatActivity {

    private TextView txtUsuarioAtual;
    private TextView txtTreinoNome;
    private ListView lsvExerciciosChecklist;
    private Button btnFinalizarTreino;

    private ExercicioDAO eDAO = new ExercicioDAO();
    private int usuarioId;
    private String usuarioNome;
    private int treinoId;
    private String treinoNome;

    private List<Exercicio> exercicios = new ArrayList<>();
    private Set<Integer> concluidos = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresso_treino);

        txtUsuarioAtual = findViewById(R.id.txtUsuarioAtual);
        txtTreinoNome = findViewById(R.id.txtTreinoNome);
        lsvExerciciosChecklist = findViewById(R.id.lsvExerciciosChecklist);
        btnFinalizarTreino = findViewById(R.id.btnFinalizarTreino);

        usuarioId = getIntent().getIntExtra("usuarioId",  0);
        usuarioNome = getIntent().getStringExtra("usuarioNome");
        treinoId = getIntent().getIntExtra("treinoId",   0);
        treinoNome = getIntent().getStringExtra("treinoNome");

        if (usuarioId <= 0 || treinoId <= 0) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        txtUsuarioAtual.setText("Usuário: " + usuarioNome);
        txtTreinoNome.setText(treinoNome);
        btnFinalizarTreino.setVisibility(View.GONE);

        carregarExercicios();
    }

    private void carregarExercicios() {
        eDAO.ListarPorTreino(treinoId, new ExercicioDAO.ExercicioCallback() {
            @Override
            public void onSucesso(List<Exercicio> resultado) {
                exercicios = resultado;
                if (exercicios.isEmpty()) {
                    Toast.makeText(ProgressoTreinoActivity.this, "Este treino não possui exercícios", Toast.LENGTH_LONG).show();
                    return;
                }

                ProgressoTreinoAdapter adapter = new ProgressoTreinoAdapter(
                        ProgressoTreinoActivity.this, exercicios, concluidos, () -> atualizarBotaoFinalizar()
                );
                lsvExerciciosChecklist.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(ProgressoTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void atualizarBotaoFinalizar() {
        if (exercicios.isEmpty()) {
            btnFinalizarTreino.setVisibility(View.GONE);
            return;
        }
        btnFinalizarTreino.setVisibility(
                concluidos.size() == exercicios.size() ? View.VISIBLE : View.GONE
        );
    }

    public void FinalizarTreinoClique(View view) {
        if (concluidos.size() < exercicios.size()) {
            Toast.makeText(this, "Marque todos os exercícios para finalizar", Toast.LENGTH_LONG).show();
            return;
        }

        RetrofitClient.getApiService()
                .finalizarTreino(usuarioId, treinoId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().insertOk()) {
                            Toast.makeText(ProgressoTreinoActivity.this, "Treino \"" + treinoNome + "\" finalizado!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProgressoTreinoActivity.this, "Treino concluído, mas não foi possível salvar o histórico", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(ProgressoTreinoActivity.this, "Treino concluído, mas sem conexão para salvar", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
