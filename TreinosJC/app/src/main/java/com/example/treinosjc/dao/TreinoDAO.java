package com.example.treinosjc.dao;

import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.ApiService;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.modelos.Treino;
import com.example.treinosjc.modelos.VinculoUsuarioTreino;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreinoDAO {

    public interface TreinoCallback {
        void onSucesso(List<Treino> lista);
        void onErro(String msg);
    }

    public interface VinculoCallback {
        void onSucesso(List<VinculoUsuarioTreino> lista);
        void onErro(String msg);
    }

    public interface AcaoCallback {
        void onSucesso();
        void onErro(String msg);
    }

    public interface InserirCallback {
        void onSucesso(int novoId);
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

    public void ListarTudo(TreinoCallback callback) {
        api.listarTreinos().enqueue(new Callback<List<Treino>>() {
            @Override
            public void onResponse(Call<List<Treino>> call, Response<List<Treino>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar treinos: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Treino>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void ListarPorUsuario(int usuarioId, TreinoCallback callback) {
        api.listarTreinosPorUsuario(usuarioId).enqueue(new Callback<List<Treino>>() {
            @Override
            public void onResponse(Call<List<Treino>> call, Response<List<Treino>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar treinos do usuario: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Treino>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Inserir(Treino t, InserirCallback callback) {
        api.inserirTreino(t.getNome(), t.getDescricao()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().insertOk()) {
                    callback.onSucesso(response.body().getId());
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao inserir treino"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Atualizar(Treino t, AcaoCallback callback) {
        api.alterarTreino(t.getId(), t.getNome(), t.getDescricao()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().updateOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao atualizar treino"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Excluir(Treino t, AcaoCallback callback) {
        api.excluirTreino(t.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().deleteOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Nao foi possivel excluir o treino"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void ListarVinculosUsuarioTreino(VinculoCallback callback) {
        api.listarVinculos().enqueue(new Callback<List<VinculoUsuarioTreino>>() {
            @Override
            public void onResponse(Call<List<VinculoUsuarioTreino>> call,
                                   Response<List<VinculoUsuarioTreino>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar vinculos: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<VinculoUsuarioTreino>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void VincularUsuarioAoTreino(int treinoId, int usuarioId, AcaoCallback callback) {
        api.vincularUsuarioTreino(usuarioId, treinoId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().insertOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao vincular"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void RemoverVinculoUsuarioTreino(int usuarioId, int treinoId, AcaoCallback callback) {
        api.desvincularUsuarioTreino(usuarioId, treinoId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().deleteOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao desvincular"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }
}
