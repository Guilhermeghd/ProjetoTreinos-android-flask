package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.UsuarioSelecaoAdapter;
import com.example.treinosjc.dao.UsuarioDAO;
import com.example.treinosjc.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class SelecionarUsuarioAtualActivity extends AppCompatActivity {

    private ListView lsvUsuariosAtual;
    private UsuarioDAO uDAO = new UsuarioDAO();
    private List<Usuario> usuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsvUsuariosAtual.setOnItemClickListener((parent, view, position, id) -> {
            if (position < 0 || position >= usuarios.size()) return;
            Usuario selecionado = usuarios.get(position);
            Intent intent = new Intent(this, ProgressoTreinoActivity.class);
            intent.putExtra("usuarioId", selecionado.getId());
            intent.putExtra("usuarioNome", selecionado.getNome());
            startActivity(intent);
        });
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        uDAO.ListarTudo(new UsuarioDAO.UsuarioCallback() {
            @Override
            public void onSucesso(List<Usuario> resultado) {
                usuarios = resultado;

                if (usuarios.isEmpty()) {
                    Toast.makeText(SelecionarUsuarioAtualActivity.this, "Nenhum usuário cadastrado", Toast.LENGTH_LONG).show();
                }
                UsuarioSelecaoAdapter adapter = new UsuarioSelecaoAdapter(SelecionarUsuarioAtualActivity.this, usuarios);
                lsvUsuariosAtual.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(SelecionarUsuarioAtualActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
