package com.example.treinosjc.dao;

import com.example.treinosjc.api.ApiResponse;
import com.example.treinosjc.api.ApiService;
import com.example.treinosjc.api.RetrofitClient;
import com.example.treinosjc.modelos.Exercicio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExercicioDAO {

    public interface ExercicioCallback {
        void onSucesso(List<Exercicio> lista);
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

    public void ListarTudo(ExercicioCallback callback) {
        api.listarExercicios().enqueue(new Callback<List<Exercicio>>() {
            @Override
            public void onResponse(Call<List<Exercicio>> call, Response<List<Exercicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar exercicios: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Exercicio>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void ListarPorTreino(int treinoId, ExercicioCallback callback) {
        api.listarExerciciosPorTreino(treinoId).enqueue(new Callback<List<Exercicio>>() {
            @Override
            public void onResponse(Call<List<Exercicio>> call, Response<List<Exercicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Exercicio e : response.body()) e.setTreinoId(treinoId);
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro ao listar exercicios do treino: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Exercicio>> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void Inserir(Exercicio e, AcaoCallback callback) {
        api.inserirExercicio(e.getNome(), e.getSeries(), e.getRepeticoes(), e.getTreinoId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().insertOk()) {
                            callback.onSucesso();
                        } else {
                            callback.onErro(extrairMsgErro(response, "Erro ao inserir exercicio"));
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        callback.onErro("Falha de conexao: " + t.getMessage());
                    }
                });
    }

    public void Atualizar(Exercicio e, AcaoCallback callback) {
        api.alterarExercicio(e.getId(), e.getNome(), e.getSeries(), e.getRepeticoes())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().updateOk()) {
                            callback.onSucesso();
                        } else {
                            callback.onErro(extrairMsgErro(response, "Erro ao atualizar exercicio"));
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        callback.onErro("Falha de conexao: " + t.getMessage());
                    }
                });
    }

    public void Excluir(Exercicio e, AcaoCallback callback) {
        api.excluirExercicio(e.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().deleteOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Nao foi possivel excluir o exercicio"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }

    public void AtualizarVinculosDoTreino(int treinoId, List<Integer> exerciciosSelecionados,
                                          AcaoCallback callback) {
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < exerciciosSelecionados.size(); i++) {
            ids.append(exerciciosSelecionados.get(i));
            if (i < exerciciosSelecionados.size() - 1) ids.append(",");
        }
        api.atualizarVinculosTreino(treinoId, ids.toString()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().updateOk()) {
                    callback.onSucesso();
                } else {
                    callback.onErro(extrairMsgErro(response, "Erro ao vincular exercicios"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onErro("Falha de conexao: " + t.getMessage());
            }
        });
    }
}
