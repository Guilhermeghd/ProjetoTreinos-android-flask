package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.treinosjc.R;
import com.example.treinosjc.modelos.Treino;

import java.util.List;

public class TreinoUsuarioAdapter extends ArrayAdapter<Treino> {

    private final LayoutInflater inflater;

    public TreinoUsuarioAdapter(Context context, List<Treino> dados) {
        super(context, R.layout.treino_usuario_item, dados);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.treino_usuario_item, parent, false);
            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.txtNome);
            holder.descricao = convertView.findViewById(R.id.txtDescricao);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Treino treino = getItem(position);
        if (treino != null) {
            holder.nome.setText(treino.getNome());
            holder.descricao.setText(treino.getDescricao());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nome;
        TextView descricao;
    }
}
