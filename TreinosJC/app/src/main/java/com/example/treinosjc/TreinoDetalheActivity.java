package com.example.treinosjc;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.dao.ExercicioDAO;
import com.example.treinosjc.dao.TreinoDAO;
import com.example.treinosjc.modelos.Exercicio;
import com.example.treinosjc.modelos.Treino;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreinoDetalheActivity extends AppCompatActivity {

    EditText edtNome, edtDescricao;
    ListView lsvExerciciosVinculo;
    Button btnInserir, btnAtualizar, btnExcluir;

    Treino treino;
    TreinoDAO tDAO = new TreinoDAO();
    ExercicioDAO eDAO = new ExercicioDAO();

    List<Exercicio> exercicios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_detalhe);

        edtNome = findViewById(R.id.edtNome);
        edtDescricao = findViewById(R.id.edtDescricao);
        lsvExerciciosVinculo = findViewById(R.id.lsvExerciciosVinculo);
        btnInserir = findViewById(R.id.btnInserir);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);

        treino = (Treino) getIntent().getSerializableExtra("treino");
        if (treino == null) treino = new Treino();

        edtNome.setText(treino.getNome());
        edtDescricao.setText(treino.getDescricao());

        boolean hasId = treino.getId() > 0;
        btnInserir.setVisibility(hasId ? View.GONE : View.VISIBLE);
        btnAtualizar.setVisibility(hasId ? View.VISIBLE : View.GONE);
        btnExcluir.setVisibility(hasId ? View.VISIBLE : View.GONE);

        carregarExercicios(treino.getId());
    }

    private void carregarExercicios(int treinoIdAtual) {
        eDAO.ListarTudo(new ExercicioDAO.ExercicioCallback() {
            @Override
            public void onSucesso(List<Exercicio> todos) {
                exercicios = todos;

                List<String> nomes = new ArrayList<>();
                for (Exercicio e : exercicios) nomes.add(e.getNome());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        TreinoDetalheActivity.this, android.R.layout.simple_list_item_multiple_choice, nomes
                );
                lsvExerciciosVinculo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lsvExerciciosVinculo.setAdapter(adapter);

                if (treinoIdAtual > 0) {
                    eDAO.ListarPorTreino(treinoIdAtual, new ExercicioDAO.ExercicioCallback() {
                        @Override
                        public void onSucesso(List<Exercicio> vinculados) {
                            for (int i = 0; i < exercicios.size(); i++) {
                                for (Exercicio v : vinculados) {
                                    if (exercicios.get(i).getId() == v.getId()) {
                                        lsvExerciciosVinculo.setItemChecked(i, true);
                                        break;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onErro(String msg) {}
                    });
                }
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(TreinoDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getExerciciosSelecionadosIds() {
        SparseBooleanArray marcados = lsvExerciciosVinculo.getCheckedItemPositions();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < exercicios.size(); i++) {
            if (marcados.get(i, false)) {
                if (ids.length() > 0) ids.append(",");
                ids.append(exercicios.get(i).getId());
            }
        }
        return ids.toString();
    }

    public void InserirClique(View view) {
        String nome = edtNome.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();

        if (nome.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha nome e descricao", Toast.LENGTH_SHORT).show();
            return;
        }

        String exercicioIds = getExerciciosSelecionadosIds();

        verificarDuplicadoESalvar(exercicioIds, 0, () -> {
            Treino t = new Treino(nome, descricao, 0);
            tDAO.Inserir(t, new TreinoDAO.InserirCallback() {
                @Override
                public void onSucesso(int novoId) {
                    vincularExerciciosSelecionados(novoId);
                }
                @Override
                public void onErro(String msg) {
                    Toast.makeText(TreinoDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        });
    }


    public void AtualizarClique(View view) {
        treino.setNome(edtNome.getText().toString().trim());
        treino.setDescricao(edtDescricao.getText().toString().trim());

        String exercicioIds = getExerciciosSelecionadosIds();

        verificarDuplicadoESalvar(exercicioIds, treino.getId(), () -> {
            tDAO.Atualizar(treino, new TreinoDAO.AcaoCallback() {
                @Override
                public void onSucesso() {
                    vincularExerciciosSelecionados(treino.getId());
                }
                @Override
                public void onErro(String msg) {
                    Toast.makeText(TreinoDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    public void ExcluirClique(View view) {
        tDAO.Excluir(treino, new TreinoDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(TreinoDetalheActivity.this, "Treino excluido", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(TreinoDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vincularExerciciosSelecionados(int treinoId) {
        if (treinoId <= 0) return;

        SparseBooleanArray marcados = lsvExerciciosVinculo.getCheckedItemPositions();
        List<Integer> selecionados = new ArrayList<>();
        for (int i = 0; i < exercicios.size(); i++) {
            if (marcados.get(i, false)) {
                selecionados.add(exercicios.get(i).getId());
            }
        }
        eDAO.AtualizarVinculosDoTreino(treinoId, selecionados, new ExercicioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(TreinoDetalheActivity.this, "Treino salvo com sucesso!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(TreinoDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void verificarDuplicadoESalvar(String exercicioIds, int treinoIdAtual, Runnable aoConfirmar) {
        if (exercicioIds.isEmpty()) {
            aoConfirmar.run();
            return;
        }
        RetrofitClient.getApiService()
                .verificarTreinosDuplicados(exercicioIds, treinoIdAtual).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isDuplicado()) {
                            String nomeExistente = response.body().getTreinoNome();
                            new AlertDialog.Builder(TreinoDetalheActivity.this)
                                    .setTitle("Treino duplicado")
                                    .setMessage(response.body().getMsg())  // ← vem do Flask
                                    .setPositiveButton("Salvar mesmo assim", (d, w) -> aoConfirmar.run())
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        } else {
                            aoConfirmar.run();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        aoConfirmar.run();
                    }
                });
    }

}
