package com.example.treinosjc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.TreinoUsuarioAdapter;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.dao.TreinoDAO;
import com.example.treinosjc.modelos.HistoricoMes;
import com.example.treinosjc.modelos.HistoricoSemana;
import com.example.treinosjc.modelos.Treino;
import com.example.treinosjc.views.CalendarioView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexUsuarioActivity extends AppCompatActivity {

    private TextView txtBemVindo;
    private ListView lsvTreinos;

    private TextView diaDom, diaSeg, diaTer, diaQua, diaQui, diaSex, diaSab ;
    private TreinoDAO tDAO = new TreinoDAO();

    private int    usuarioId;
    private String usuarioNome;
    private List<Treino> treinos = new ArrayList<>();

    private HistoricoMes historicoMesAtual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_usuario);

        txtBemVindo = findViewById(R.id.txtBemVindo);
        lsvTreinos  = findViewById(R.id.lsvTreinos);

        diaSeg = findViewById(R.id.diaSeg);
        diaTer = findViewById(R.id.diaTer);
        diaQua = findViewById(R.id.diaQua);
        diaQui = findViewById(R.id.diaQui);
        diaSex = findViewById(R.id.diaSex);
        diaSab = findViewById(R.id.diaSab);
        diaDom = findViewById(R.id.diaDom);

        usuarioId   = getIntent().getIntExtra("usuarioId", 0);
        usuarioNome = getIntent().getStringExtra("usuarioNome");

        txtBemVindo.setText("Olá, " + usuarioNome + "!");

        lsvTreinos.setOnItemClickListener((parent, view, position, id) -> {
            Treino t = treinos.get(position);
            Intent intent = new Intent(this, ProgressoTreinoActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            intent.putExtra("usuarioNome", usuarioNome);
            intent.putExtra("treinoId", t.getId());
            intent.putExtra("treinoNome", t.getNome());
            startActivity(intent);
        });
        findViewById(R.id.layoutSemana).setOnClickListener(v -> mostrarCalendario());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTreinos();
        carregarHistoricoSemana();
        carregarHistoricoMes();
    }

    private void carregarTreinos() {
        tDAO.ListarPorUsuario(usuarioId, new TreinoDAO.TreinoCallback() {
            @Override
            public void onSucesso(List<Treino> resultado) {
                treinos = resultado;
                if (treinos.isEmpty()) {
                    Toast.makeText(IndexUsuarioActivity.this, "Você ainda não possui treinos vinculados", Toast.LENGTH_LONG).show();
                    return;
                }
                TreinoUsuarioAdapter adapter = new TreinoUsuarioAdapter(
                        IndexUsuarioActivity.this, treinos);
                lsvTreinos.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(IndexUsuarioActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarHistoricoSemana() {
        RetrofitClient.getApiService().historicoSemana(usuarioId).enqueue(new Callback<HistoricoSemana>() {
                    @Override
                    public void onResponse(Call<HistoricoSemana> call, Response<HistoricoSemana> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            atualizarDiasSemana(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<HistoricoSemana> call, Throwable t) {}
                });
    }

    private void carregarHistoricoMes() {
        RetrofitClient.getApiService().historicoMes(usuarioId).enqueue(new Callback<HistoricoMes>() {
                    @Override
                    public void onResponse(Call<HistoricoMes> call, Response<HistoricoMes> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            historicoMesAtual = response.body();
                        }
                    }
                    @Override
                    public void onFailure(Call<HistoricoMes> call, Throwable t) {}
                });
    }

    private void atualizarDiasSemana(HistoricoSemana semana) {
        TextView[] dias = {diaDom, diaSeg, diaTer, diaQua, diaQui, diaSex, diaSab};
        for (int i = 0; i < dias.length; i++) {
            if (semana.treinouNoDia(i)) {
                dias[i].setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
                dias[i].setTextColor(android.graphics.Color.WHITE);
            } else {
                dias[i].setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
                dias[i].setTextColor(android.graphics.Color.parseColor("#888888"));
            }
        }
    }

    private void mostrarCalendario() {
        View modalView = LayoutInflater.from(this)
                .inflate(R.layout.modal_calendario, null);

        CalendarioView calendarioView = modalView.findViewById(R.id.calendarioView);

        if (historicoMesAtual != null) {
            calendarioView.setDadosMes(historicoMesAtual.getAno(), historicoMesAtual.getMes(), historicoMesAtual.getDiasTreinados()
            );
        } else {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            calendarioView.setDadosMes(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, new ArrayList<>()
            );
        }

        AlertDialog dialog = new AlertDialog.Builder(this).setView(modalView).create();

        modalView.findViewById(R.id.btnFecharCalendario).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void sair(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
