package com.example.treinosjc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.dao.UsuarioDAO;
import com.example.treinosjc.modelos.Usuario;

public class UsuarioDetalheActivity extends AppCompatActivity {

    EditText edtNome, edtEmail;
    Button btnAtualizar, btnExcluir;

    Usuario usuario;
    UsuarioDAO uDAO = new UsuarioDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalhe);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) usuario = new Usuario();

        edtNome.setText(usuario.getNome());
        edtEmail.setText(usuario.getEmail());
        edtNome.requestFocus();

        boolean hasId = usuario.getId() > 0;
        btnAtualizar.setVisibility(hasId ? View.VISIBLE : View.GONE);
        btnExcluir.setVisibility(hasId ? View.VISIBLE : View.GONE);
    }

    public void AtualizarClique(View view) {
        usuario.setNome(edtNome.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());

        uDAO.Atualizar(usuario, new UsuarioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(UsuarioDetalheActivity.this, "Usuário atualizado", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onErro(String msg) {
                Toast.makeText(UsuarioDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ExcluirClique(View view) {
        uDAO.Excluir(usuario, new UsuarioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(UsuarioDetalheActivity.this, "Usuário excluído", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onErro(String msg) {
                Toast.makeText(UsuarioDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
