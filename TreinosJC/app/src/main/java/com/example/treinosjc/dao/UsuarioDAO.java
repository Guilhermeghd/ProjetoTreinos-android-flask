package com.example.treinosjc.dao;

import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.ApiService;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.modelos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioDAO {

    public interface UsuarioCallback {
        void onSucesso(List<Usuario> lista);
        void onErro(String msg);
    }

    public interface AcaoCallback {
        void onSucesso();
        void onErro(String msg);
    }

    private final ApiService api = RetrofitClient.getApiService();

    private String extrairMsgErro(Response<?> response, String msgPadrao) {
        try {
            if (response.errorBody() != null) {
                com.google.gson.Gson gson = new com.google.gson.Gson();
                ApiResponse erro = gson.fromJson(
                        response.errorBody().charStream(), ApiResponse.class);
                if (erro != null && erro.getMsg() != null && !erro.getMsg().isEmpty()) {
                    return erro.getMsg();
                }
            }
        } catch (Exception ignored) {}
        return msgPadrao;
    }

    public void ListarTudo(UsuarioCallback callback) {
        api.listarUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar usuarios: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Inserir(Usuario u, AcaoCallback callback) {
        api.inserirUsuario(u.getNome(), u.getEmail(), u.getSenha()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().insertOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao inserir usuario"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Atualizar(Usuario u, AcaoCallback callback) {
        api.alterarUsuario(u.getId(), u.getNome(), u.getEmail(), u.getSenha()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().updateOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao atualizar usuario"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Excluir(Usuario u, AcaoCallback callback) {
        api.excluirUsuario(u.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().deleteOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Nao foi possivel excluir o usuario"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }
}
