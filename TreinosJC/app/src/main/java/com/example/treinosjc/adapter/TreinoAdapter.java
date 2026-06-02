package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.treinosjc.R;
import com.example.treinosjc.modelos.Treino;

import java.util.List;

public class TreinoAdapter extends ArrayAdapter<Treino> {

    public interface OnTreinoActionListener {
        void onEditar(Treino treino);
        void onExcluir(Treino treino);
    }

    private LayoutInflater mInflater;
    private OnTreinoActionListener actionListener;

    public TreinoAdapter(Context context, int resource, List<Treino> dados){
        this(context, resource, dados, null);
    }

    public TreinoAdapter(Context context, int resource, List<Treino> dados, OnTreinoActionListener actionListener){
        super(context, resource, dados);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actionListener = actionListener;
    }

    public View getView(int posicao, View linha, ViewGroup parent){
        ViewHolder holder;

        if (linha == null){
            linha = mInflater.inflate(R.layout.treino_item, null);
            holder = new ViewHolder();
            holder.nome = linha.findViewById(R.id.txtNome);
            holder.descricao = linha.findViewById(R.id.txtDescricao);
            holder.btnEditar = linha.findViewById(R.id.btnEditar);
            holder.btnExcluir = linha.findViewById(R.id.btnExcluir);

            linha.setTag(holder);
        } else {
            holder = (ViewHolder) linha.getTag();
        }

        Treino t = getItem(posicao);

        holder.nome.setText(t.getNome());
        holder.descricao.setText(t.getDescricao());

        if (actionListener == null) {
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnExcluir.setVisibility(View.GONE);
        } else {
            holder.btnEditar.setVisibility(View.VISIBLE);
            holder.btnExcluir.setVisibility(View.VISIBLE);

            holder.btnEditar.setOnClickListener(v -> actionListener.onEditar(t));
            holder.btnExcluir.setOnClickListener(v -> actionListener.onExcluir(t));
        }

        return linha;
    }

    static class ViewHolder{
        public TextView nome;
        public TextView descricao;
        public Button btnEditar;
        public Button btnExcluir;
    }
}