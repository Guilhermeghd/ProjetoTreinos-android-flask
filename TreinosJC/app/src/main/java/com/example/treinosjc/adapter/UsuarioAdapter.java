package com.example.treinosjc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treinosjc.R;
import com.example.treinosjc.UsuarioDetalheActivity;
import com.example.treinosjc.dao.UsuarioDAO;
import com.example.treinosjc.modelos.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private LayoutInflater mInflater;
    private UsuarioDAO uDAO = new UsuarioDAO(); // sem Context!

    public UsuarioAdapter(Context context, int resource, List<Usuario> dados) {
        super(context, resource, dados);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int posicao, View linha, ViewGroup parent) {
        ViewHolder holder;
        if (linha == null) {
            linha = mInflater.inflate(R.layout.usuario_item, null);
            holder = new ViewHolder();
            holder.nome = linha.findViewById(R.id.txtNome);
            holder.email = linha.findViewById(R.id.txtEmail);
            holder.btnEditar = linha.findViewById(R.id.btnEditar);
            holder.btnExcluir = linha.findViewById(R.id.btnExcluir);
            linha.setTag(holder);
        } else {
            holder = (ViewHolder) linha.getTag();
        }

        Usuario u = getItem(posicao);

        holder.nome.setText(u.getNome());
        holder.email.setText(u.getEmail());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UsuarioDetalheActivity.class);
            intent.putExtra("usuario", u);
            getContext().startActivity(intent);
        });

        holder.btnExcluir.setOnClickListener(v -> {
            uDAO.Excluir(u, new UsuarioDAO.AcaoCallback() {
                @Override
                public void onSucesso() {
                    remove(u);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Usuário excluído", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onErro(String msg) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        });
        return linha;
    }

    static class ViewHolder {
        public TextView nome;
        public TextView email;
        public Button btnEditar;
        public Button btnExcluir;
    }
}