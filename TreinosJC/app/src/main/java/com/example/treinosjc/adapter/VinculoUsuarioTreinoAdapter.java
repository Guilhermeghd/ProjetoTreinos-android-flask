package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.treinosjc.R;
import com.example.treinosjc.modelos.VinculoUsuarioTreino;

import java.util.List;

public class VinculoUsuarioTreinoAdapter extends ArrayAdapter<VinculoUsuarioTreino> {

    public interface OnVinculoActionListener {
        void onTrocarTreino(VinculoUsuarioTreino vinculo);
        void onRemoverVinculo(VinculoUsuarioTreino vinculo);
    }

    private final LayoutInflater inflater;
    private final OnVinculoActionListener actionListener;

    public VinculoUsuarioTreinoAdapter(Context context, List<VinculoUsuarioTreino> dados, OnVinculoActionListener actionListener) {
        super(context, R.layout.vinculo_usuario_treino_item, dados);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actionListener = actionListener;
    }
    @Override
    public View getView(int posicao, View linha, ViewGroup parent) {
        ViewHolder holder;
        if (linha == null) {
            linha = inflater.inflate(R.layout.vinculo_usuario_treino_item, parent, false);
            holder = new ViewHolder();
            holder.txtUsuario = linha.findViewById(R.id.txtUsuario);
            holder.txtTreino = linha.findViewById(R.id.txtTreino);
            holder.btnTrocar = linha.findViewById(R.id.btnTrocarTreino);
            holder.btnRemover = linha.findViewById(R.id.btnRemoverVinculo);
            linha.setTag(holder);
        } else {
            holder = (ViewHolder) linha.getTag();
        }

        VinculoUsuarioTreino vinculo = getItem(posicao);
        if (vinculo == null) {
            return linha;
        }
        holder.txtUsuario.setText("Usuário: " + vinculo.getUsuarioNome());
        holder.txtTreino.setText("Treino: " + vinculo.getTreinoNome());
        holder.btnTrocar.setOnClickListener(v -> actionListener.onTrocarTreino(vinculo));
        holder.btnRemover.setOnClickListener(v -> actionListener.onRemoverVinculo(vinculo));

        return linha;
    }

    static class ViewHolder {
        TextView txtUsuario;
        TextView txtTreino;
        Button btnTrocar;
        Button btnRemover;
    }
}
