package com.example.treinosjc;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.VinculoUsuarioTreinoAdapter;
import com.example.treinosjc.dao.TreinoDAO;
import com.example.treinosjc.dao.UsuarioDAO;
import com.example.treinosjc.modelos.Treino;
import com.example.treinosjc.modelos.Usuario;
import com.example.treinosjc.modelos.VinculoUsuarioTreino;

import java.util.ArrayList;
import java.util.List;

public class VincularUsuarioTreinoActivity extends AppCompatActivity {

    private Spinner spnUsuario, spnTreino;
    private ListView lsvVinculos;

    private UsuarioDAO uDAO = new UsuarioDAO();
    private TreinoDAO tDAO = new TreinoDAO();

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Treino> treinos = new ArrayList<>();
    private List<VinculoUsuarioTreino> vinculos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular_usuario_treino);

        spnUsuario = findViewById(R.id.spnUsuario);
        spnTreino = findViewById(R.id.spnTreino);
        lsvVinculos = findViewById(R.id.lsvVinculos);

        carregarUsuarios();
        carregarTreinos();
        carregarVinculos();
    }

    private void carregarUsuarios() {
        uDAO.ListarTudo(new UsuarioDAO.UsuarioCallback() {
            @Override
            public void onSucesso(List<Usuario> resultado) {
                usuarios = resultado;

                List<String> nomes = new ArrayList<>();
                nomes.add("Selecione um usuario");
                for (Usuario u : usuarios) nomes.add(u.getNome());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        VincularUsuarioTreinoActivity.this, R.layout.spiner_item, nomes);
                adapter.setDropDownViewResource(R.layout.spiner_item);
                spnUsuario.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarTreinos() {
        tDAO.ListarTudo(new TreinoDAO.TreinoCallback() {
            @Override
            public void onSucesso(List<Treino> resultado) {
                treinos = resultado;

                List<String> nomes = new ArrayList<>();
                nomes.add("Selecione um treino");
                for (Treino t : treinos) nomes.add(t.getNome());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        VincularUsuarioTreinoActivity.this, R.layout.spiner_item, nomes);
                adapter.setDropDownViewResource(R.layout.spiner_item);
                spnTreino.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarVinculos() {
        tDAO.ListarVinculosUsuarioTreino(new TreinoDAO.VinculoCallback() {
            @Override
            public void onSucesso(List<VinculoUsuarioTreino> resultado) {
                vinculos = resultado;

                VinculoUsuarioTreinoAdapter adapter = new VinculoUsuarioTreinoAdapter(
                        VincularUsuarioTreinoActivity.this, vinculos, new VinculoUsuarioTreinoAdapter.OnVinculoActionListener() {
                            @Override
                            public void onTrocarTreino(VinculoUsuarioTreino vinculo) {
                                mostrarDialogTrocarTreino(vinculo);
                            }
                            @Override
                            public void onRemoverVinculo(VinculoUsuarioTreino vinculo) {
                                removerVinculo(vinculo);
                            }
                        }
                );
                lsvVinculos.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void VincularClique(View view) {
        int usuarioPosicao = spnUsuario.getSelectedItemPosition();
        int treinoPosicao = spnTreino.getSelectedItemPosition();

        if (usuarioPosicao <= 0 || treinoPosicao <= 0) {
            Toast.makeText(this, "Selecione usuário e treino", Toast.LENGTH_LONG).show();
            return;
        }

        int usuarioId = usuarios.get(usuarioPosicao - 1).getId();
        int treinoId = treinos.get(treinoPosicao - 1).getId();
        String uNome = usuarios.get(usuarioPosicao - 1).getNome();
        String tNome = treinos.get(treinoPosicao - 1).getNome();

        tDAO.VincularUsuarioAoTreino(treinoId, usuarioId, new TreinoDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(VincularUsuarioTreinoActivity.this, uNome + " vinculado a " + tNome, Toast.LENGTH_LONG).show();
                carregarVinculos();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogTrocarTreino(VinculoUsuarioTreino vinculo) {
        List<Treino> opcoes = new ArrayList<>();
        List<String> nomes = new ArrayList<>();

        for (Treino t : treinos) {
            if (t.getId() != vinculo.getTreinoId()) {
                opcoes.add(t);
                nomes.add(t.getNome());
            }
        }
        if (opcoes.isEmpty()) {
            Toast.makeText(this, "Não há outro treino disponível", Toast.LENGTH_LONG).show();
            return;
        }

        CharSequence[] itens = nomes.toArray(new CharSequence[0]);
        new AlertDialog.Builder(this)
                .setTitle("Escolha o novo treino")
                .setItems(itens, (dialog, which) -> {
                    Treino novoTreino = opcoes.get(which);
                    tDAO.RemoverVinculoUsuarioTreino(
                            vinculo.getUsuarioId(), vinculo.getTreinoId(), new TreinoDAO.AcaoCallback() {
                                @Override
                                public void onSucesso() {
                                    tDAO.VincularUsuarioAoTreino(
                                            novoTreino.getId(), vinculo.getUsuarioId(), new TreinoDAO.AcaoCallback() {
                                                @Override
                                                public void onSucesso() {
                                                    Toast.makeText(VincularUsuarioTreinoActivity.this, "Treino alterado com sucesso", Toast.LENGTH_LONG).show();
                                                    carregarVinculos();
                                                }
                                                @Override
                                                public void onErro(String msg) {
                                                    Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                                @Override
                                public void onErro(String msg) {
                                    Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                            });
                })
                .show();
    }

    private void removerVinculo(VinculoUsuarioTreino vinculo) {
        new AlertDialog.Builder(this)
                .setTitle("Remover vínculo")
                .setMessage("Deseja remover o vínculo de " + vinculo.getDescricao() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    tDAO.RemoverVinculoUsuarioTreino(
                            vinculo.getUsuarioId(), vinculo.getTreinoId(),
                            new TreinoDAO.AcaoCallback() {
                                @Override
                                public void onSucesso() {
                                    Toast.makeText(VincularUsuarioTreinoActivity.this, "Vínculo removido", Toast.LENGTH_LONG).show();
                                    carregarVinculos();
                                }
                                @Override
                                public void onErro(String msg) {
                                    Toast.makeText(VincularUsuarioTreinoActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                            });
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarUsuarios();
        carregarTreinos();
        carregarVinculos();
    }
}
