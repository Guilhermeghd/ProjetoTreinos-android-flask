package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.TreinoAdapter;
import com.example.treinosjc.dao.TreinoDAO;
import com.example.treinosjc.modelos.Treino;

import java.util.ArrayList;
import java.util.List;

public class GerenciarTreinosActivity extends AppCompatActivity {

    private ListView lsvTreinos;
    private List<Treino> lista = new ArrayList<>();
    private TreinoDAO tDAO = new TreinoDAO();

    ActivityResultLauncher<Intent> detalheLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_treinos);

        lsvTreinos = findViewById(R.id.lsvTreinos);

        detalheLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Listar();
                    }
                }
        );
        Listar();
    }

    private void Listar() {
        tDAO.ListarTudo(new TreinoDAO.TreinoCallback() {
            @Override
            public void onSucesso(List<Treino> resultado) {
                lista = resultado;

                TreinoAdapter adapter = new TreinoAdapter(GerenciarTreinosActivity.this, R.layout.treino_item, lista,
                        new TreinoAdapter.OnTreinoActionListener() {
                            @Override
                            public void onEditar(Treino treino) {
                                Intent intent = new Intent(GerenciarTreinosActivity.this, TreinoDetalheActivity.class);
                                intent.putExtra("treino", treino);
                                detalheLauncher.launch(intent);
                            }

                            @Override
                            public void onExcluir(Treino treino) {
                                tDAO.Excluir(treino, new TreinoDAO.AcaoCallback() {
                                    @Override
                                    public void onSucesso() {
                                        lista.remove(treino);
                                        ((TreinoAdapter) lsvTreinos.getAdapter()).notifyDataSetChanged();
                                        Toast.makeText(GerenciarTreinosActivity.this, "Treino excluído", Toast.LENGTH_LONG).show();
                                    }
                                    @Override
                                    public void onErro(String msg) {
                                        Toast.makeText(GerenciarTreinosActivity.this, msg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                );
                lsvTreinos.setAdapter(adapter);
            }

            @Override
            public void onErro(String msg) {
                Toast.makeText(GerenciarTreinosActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void NovoClique(View view) {
        Intent intent = new Intent(this, TreinoDetalheActivity.class);
        intent.putExtra("treino", new Treino());
        detalheLauncher.launch(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Listar();
    }
}
