package com.example.treinosjc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.treinosjc.R;
import com.example.treinosjc.modelos.Usuario;

import java.util.List;

public class UsuarioSelecaoAdapter extends ArrayAdapter<Usuario> {

    private final LayoutInflater inflater;

    public UsuarioSelecaoAdapter(@NonNull Context context, List<Usuario> dados) {
        super(context, R.layout.item_usuario_selecao, dados);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_usuario_selecao, parent, false);
            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.txtNome);
            holder.email = convertView.findViewById(R.id.txtEmail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Usuario usuario = getItem(position);
        if (usuario == null) {
            return convertView;
        }
        holder.nome.setText(usuario.getNome());
        holder.email.setText(usuario.getEmail());

        return convertView;
    }
    static class ViewHolder {
        TextView nome;
        TextView email;
    }
}

