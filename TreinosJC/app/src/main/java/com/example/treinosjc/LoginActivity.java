package com.example.treinosjc;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
    }

    public void LoginClique(View view) {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha email e senha", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().login(email, senha)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().loginOk()) {
                            int id = response.body().getId();
                            String nome = response.body().getNome();
                            String role = response.body().getRole();

                            Intent intent;
                            if ("admin".equals(role)) {
                                intent = new Intent(LoginActivity.this, IndexAdminActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, IndexUsuarioActivity.class);
                            }

                            intent.putExtra("usuarioId", id);
                            intent.putExtra("usuarioNome", nome);
                            intent.putExtra("role", role);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email ou senha inválidos", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Falha de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void CadastrarClique(View view) {
        startActivity(new Intent(this, CadastrarActivity.class));
    }
}
