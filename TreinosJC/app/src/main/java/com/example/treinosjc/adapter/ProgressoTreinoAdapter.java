package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.treinosjc.modelos.Exercicio;
import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.treinosjc.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgressoTreinoAdapter extends ArrayAdapter<Exercicio> {

    public interface OnItemCheckChangeListener {
        void onItemCheckChanged();
    }

    private final LayoutInflater inflater;
    private final Set<Integer> concluidos;
    private final OnItemCheckChangeListener listener;

    public ProgressoTreinoAdapter(@NonNull Context context, List<Exercicio> dados, Set<Integer> concluidos, OnItemCheckChangeListener listener) {
        super(context, R.layout.item_progresso_exercicio, dados);
        this.inflater = LayoutInflater.from(context);
        this.concluidos = concluidos != null ? concluidos : new HashSet<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_progresso_exercicio, parent, false);
            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.txtNome);
            holder.detalhes = convertView.findViewById(R.id.txtDetalhes);
            holder.switchConcluido = convertView.findViewById(R.id.switchConcluido);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercicio exercicio = getItem(position);
        if (exercicio == null) {
            return convertView;
        }

        holder.nome.setText(exercicio.getNome());
        holder.detalhes.setText("Séries: " + exercicio.getSeries() + " | Repetições: " + exercicio.getRepeticoes());

        boolean marcado = concluidos.contains(exercicio.getId());
        holder.switchConcluido.setOnCheckedChangeListener(null);
        holder.switchConcluido.setChecked(marcado);

        View.OnClickListener toggle = v -> {
            boolean novoEstado = !concluidos.contains(exercicio.getId());
            if (novoEstado) {
                concluidos.add(exercicio.getId());
            } else {
                concluidos.remove(exercicio.getId());
            }
            notifyDataSetChanged();
            if (listener != null) {
                listener.onItemCheckChanged();
            }
        };
        convertView.setOnClickListener(toggle);
        holder.switchConcluido.setOnClickListener(toggle);

        return convertView;
    }

    static class ViewHolder {
        TextView nome;
        TextView detalhes;
        SwitchMaterial switchConcluido;
    }
}

