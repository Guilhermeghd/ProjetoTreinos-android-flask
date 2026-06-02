package com.example.treinosjc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.adapter.UsuarioAdapter;
import com.example.treinosjc.dao.UsuarioDAO;
import com.example.treinosjc.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lsvUsuarios;
    List<Usuario> lista = new ArrayList<>();
    UsuarioDAO uDAO = new UsuarioDAO();

    ActivityResultLauncher<Intent> detalheLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista);

        lsvUsuarios = findViewById(R.id.lsvUsuarios);

        detalheLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Listar();
                    }
                }
        );
        Listar();
    }

    private void Listar() {
        uDAO.ListarTudo(new UsuarioDAO.UsuarioCallback() {
            @Override
            public void onSucesso(List<Usuario> resultado) {
                lista = resultado;
                UsuarioAdapter adapter = new UsuarioAdapter(
                        MainActivity.this, R.layout.usuario_item, lista);
                lsvUsuarios.setAdapter(adapter);
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void NovoClique(View view) {
        Intent intent = new Intent(this, UsuarioDetalheActivity.class);
        intent.putExtra("usuario", new Usuario());
        detalheLauncher.launch(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Listar();
    }
}
