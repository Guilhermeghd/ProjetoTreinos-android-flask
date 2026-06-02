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

import com.example.treinosjc.ExercicioDetalheActivity;
import com.example.treinosjc.R;
import com.example.treinosjc.dao.ExercicioDAO;
import com.example.treinosjc.modelos.Exercicio;

import java.util.List;

public class ExercicioAdapter extends ArrayAdapter<Exercicio> {

    private LayoutInflater mInflater;
    private ExercicioDAO eDAO = new ExercicioDAO(); // sem Context!

    public ExercicioAdapter(Context context, int resource, List<Exercicio> dados) {
        super(context, resource, dados);
        this.mInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int posicao, View linha, ViewGroup parent) {
        ViewHolder holder;
        if (linha == null) {
            linha = mInflater.inflate(R.layout.exercicio_item, null);
            holder = new ViewHolder();
            holder.nome = linha.findViewById(R.id.txtNome);
            holder.series = linha.findViewById(R.id.txtSeries);
            holder.repeticoes = linha.findViewById(R.id.txtRepeticoes);
            holder.btnEditar  = linha.findViewById(R.id.btnEditar);
            holder.btnExcluir = linha.findViewById(R.id.btnExcluir);
            linha.setTag(holder);
        } else {
            holder = (ViewHolder) linha.getTag();
        }

        Exercicio e = getItem(posicao);

        holder.nome.setText(e.getNome());
        holder.series.setText("Séries: " + e.getSeries());
        holder.repeticoes.setText("Repetições: " + e.getRepeticoes());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ExercicioDetalheActivity.class);
            intent.putExtra("exercicio", e);
            intent.putExtra("treinoId", e.getTreinoId());
            getContext().startActivity(intent);
        });

        holder.btnExcluir.setOnClickListener(v -> {
            eDAO.Excluir(e, new ExercicioDAO.AcaoCallback() {
                @Override
                public void onSucesso() {
                    remove(e);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Exercício excluído", Toast.LENGTH_LONG).show();
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
        public TextView series;
        public TextView repeticoes;
        public Button btnEditar;
        public Button btnExcluir;
    }
}