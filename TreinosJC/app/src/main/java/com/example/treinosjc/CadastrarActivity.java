package com.example.treinosjc;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtSenha, edtConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha);
    }

    public void CadastrarClique(View view) {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String confirmarSenha = edtConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty()) {
            edtNome.setError("Nome obrigatorio");
            edtNome.requestFocus();
            return;
        }
        if (nome.length() < 3) {
            edtNome.setError("Nome deve ter pelo menos 3 caracteres");
            edtNome.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Email obrigatorio");
            edtEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email invalido");
            edtEmail.requestFocus();
            return;
        }
        if (senha.isEmpty()) {
            edtSenha.setError("Senha obrigatoria");
            edtSenha.requestFocus();
            return;
        }
        if (senha.length() < 6) {
            edtSenha.setError("Senha deve ter pelo menos 6 caracteres");
            edtSenha.requestFocus();
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            edtConfirmarSenha.setError("Senhas nao conferem");
            edtConfirmarSenha.requestFocus();
            return;
        }

        RetrofitClient.getApiService().cadastrar(nome, email, senha)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().insertOk()) {
                            Toast.makeText(CadastrarActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String msg = response.body() != null ? response.body().getMsg() : "Erro ao cadastrar";
                            Toast.makeText(CadastrarActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(CadastrarActivity.this,
                                "Falha de conexao: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}