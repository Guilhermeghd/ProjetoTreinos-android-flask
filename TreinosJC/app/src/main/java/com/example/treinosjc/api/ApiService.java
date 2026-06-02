package com.example.treinosjc.api;

import com.example.treinosjc.modelos.Exercicio;
import com.example.treinosjc.modelos.Treino;
import com.example.treinosjc.modelos.Usuario;
import com.example.treinosjc.modelos.VinculoUsuarioTreino;
import com.example.treinosjc.modelos.HistoricoTreino;
import com.example.treinosjc.modelos.HistoricoSemana;
import com.example.treinosjc.modelos.HistoricoMes;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse> login(
            @Field("txtEmail") String email,
            @Field("txtSenha") String senha
    );

    @FormUrlEncoded
    @POST("cadastrar")
    Call<ApiResponse> cadastrar(
            @Field("txtNome") String nome,
            @Field("txtEmail") String email,
            @Field("txtSenha") String senha
    );

    @GET("listarUsuarios")
    Call<List<Usuario>> listarUsuarios();

    @FormUrlEncoded
    @POST("insUsuario")
    Call<ApiResponse> inserirUsuario(
            @Field("txtNome") String nome,
            @Field("txtEmail") String email,
            @Field("txtSenha") String senha
    );

    @FormUrlEncoded
    @POST("altUsuario")
    Call<ApiResponse> alterarUsuario(
            @Field("txtId") int id,
            @Field("txtNome") String nome,
            @Field("txtEmail") String email,
            @Field("txtSenha") String senha
    );

    @FormUrlEncoded
    @POST("excUsuario")
    Call<ApiResponse> excluirUsuario(
            @Field("txtId") int id
    );


    @GET("listarTreinos")
    Call<List<Treino>> listarTreinos();

    @GET("listarTreinosPorUsuario")
    Call<List<Treino>> listarTreinosPorUsuario(
            @Query("txtUsuarioId") int usuarioId
    );

    @FormUrlEncoded
    @POST("insTreino")
    Call<ApiResponse> inserirTreino(
            @Field("txtNome") String nome,
            @Field("txtDescricao") String descricao
    );

    @FormUrlEncoded
    @POST("altTreino")
    Call<ApiResponse> alterarTreino(
            @Field("txtId") int id,
            @Field("txtNome") String nome,
            @Field("txtDescricao") String descricao
    );

    @FormUrlEncoded
    @POST("excTreino")
    Call<ApiResponse> excluirTreino(
            @Field("txtId") int id
    );

    @FormUrlEncoded
    @POST("verificarTreinosDuplicados")
    Call<ApiResponse> verificarTreinosDuplicados(
            @Field("txtExercicioIds") String exercicioIds,
            @Field("txtTreinoId") int treinoId
    );


    @GET("listarExercicios")
    Call<List<Exercicio>> listarExercicios();

    @GET("listarExerciciosPorTreino")
    Call<List<Exercicio>> listarExerciciosPorTreino(
            @Query("txtTreinoId") int treinoId
    );

    @FormUrlEncoded
    @POST("insExercicio")
    Call<ApiResponse> inserirExercicio(
            @Field("txtNome") String nome,
            @Field("txtSeries") int series,
            @Field("txtRepeticoes") int repeticoes,
            @Field("txtTreinoId") int treinoId
    );

    @FormUrlEncoded
    @POST("altExercicio")
    Call<ApiResponse> alterarExercicio(
            @Field("txtId") int id,
            @Field("txtNome") String nome,
            @Field("txtSeries") int series,
            @Field("txtRepeticoes") int repeticoes
    );

    @FormUrlEncoded
    @POST("atualizarVinculosTreino")
    Call<ApiResponse> atualizarVinculosTreino(
            @Field("txtTreinoId") int treinoId,
            @Field("txtExercicioIds") String exercicioIds
    );


    @FormUrlEncoded
    @POST("excExercicio")
    Call<ApiResponse> excluirExercicio(
            @Field("txtId") int id
    );


    @GET("listarVinculos")
    Call<List<VinculoUsuarioTreino>> listarVinculos();

    @FormUrlEncoded
    @POST("vincularUsuarioTreino")
    Call<ApiResponse> vincularUsuarioTreino(
            @Field("txtUsuarioId") int usuarioId,
            @Field("txtTreinoId") int treinoId
    );

    @FormUrlEncoded
    @POST("desvincularUsuarioTreino")
    Call<ApiResponse> desvincularUsuarioTreino(
            @Field("txtUsuarioId") int usuarioId,
            @Field("txtTreinoId") int treinoId
    );


    @FormUrlEncoded
    @POST("finalizarTreino")
    Call<ApiResponse> finalizarTreino(
            @Field("txtUsuarioId") int usuarioId,
            @Field("txtTreinoId") int treinoId
    );

    @GET("listarHistorico")
    Call<List<HistoricoTreino>> listarHistorico(
            @Query("txtUsuarioId") int usuarioId
    );

    @GET("historicoSemana")
    Call<HistoricoSemana> historicoSemana(
            @Query("txtUsuarioId") int usuarioId
    );

    @GET("historicoMes")
    Call<HistoricoMes> historicoMes(
            @Query("txtUsuarioId") int usuarioId
    );

}

