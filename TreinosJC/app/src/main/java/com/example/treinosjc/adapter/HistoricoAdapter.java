package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.treinosjc.R;
import com.example.treinosjc.modelos.HistoricoTreino;

import java.util.List;

public class HistoricoAdapter extends ArrayAdapter<HistoricoTreino> {

    private final LayoutInflater inflater;

    public HistoricoAdapter(Context context, List<HistoricoTreino> dados) {
        super(context, R.layout.historico_item, dados);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.historico_item, parent, false);
            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.txtNomeTreino);
            holder.data = convertView.findViewById(R.id.txtData);
            holder.hora = convertView.findViewById(R.id.txtHora);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoricoTreino h = getItem(position);
        if (h != null) {
            holder.nome.setText(h.getTreinoNome());
            holder.data.setText(h.getDataFormatada());
            holder.hora.setText(h.getHora());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nome;
        TextView data;
        TextView hora;
    }
}
