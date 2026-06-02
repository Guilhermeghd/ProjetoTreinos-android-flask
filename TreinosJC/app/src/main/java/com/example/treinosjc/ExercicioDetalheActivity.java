package com.example.treinosjc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.treinosjc.dao.ExercicioDAO;
import com.example.treinosjc.modelos.Exercicio;

public class ExercicioDetalheActivity extends AppCompatActivity {

    EditText edtNome, edtSeries, edtRepeticoes;
    Button btnInserir, btnAtualizar, btnExcluir;

    Exercicio exercicio;
    ExercicioDAO eDAO = new ExercicioDAO();  // sem Context!
    int treinoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_detalhe);

        edtNome = findViewById(R.id.edtNome);
        edtSeries = findViewById(R.id.edtSeries);
        edtRepeticoes = findViewById(R.id.edtRepeticoes);
        btnInserir = findViewById(R.id.btnInserir);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);

        exercicio = (Exercicio) getIntent().getSerializableExtra("exercicio");
        if (exercicio == null) exercicio = new Exercicio();

        treinoId = getIntent().getIntExtra("treinoId", 0);

        boolean hasId = exercicio.getId() > 0;
        if (hasId) {
            edtNome.setText(exercicio.getNome());
            edtSeries.setText(String.valueOf(exercicio.getSeries()));
            edtRepeticoes.setText(String.valueOf(exercicio.getRepeticoes()));
        }
        edtNome.requestFocus();

        btnInserir.setVisibility(hasId ? View.GONE : View.VISIBLE);
        btnAtualizar.setVisibility(hasId ? View.VISIBLE : View.GONE);
        btnExcluir.setVisibility(hasId ? View.VISIBLE : View.GONE);
    }

    public void InserirClique(View view) {
        Integer series = parseCampoInteiro(edtSeries);
        Integer repeticoes = parseCampoInteiro(edtRepeticoes);

        if (series == null || repeticoes == null) {
            Toast.makeText(this, "Preencha séries e repetições", Toast.LENGTH_LONG).show();
            return;
        }

        Exercicio e = new Exercicio(edtNome.getText().toString(), series, repeticoes, treinoId > 0 ? treinoId : 0
        );

        eDAO.Inserir(e, new ExercicioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(ExercicioDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AtualizarClique(View view) {
        Integer series = parseCampoInteiro(edtSeries);
        Integer repeticoes = parseCampoInteiro(edtRepeticoes);

        if (series == null || repeticoes == null) {
            Toast.makeText(this, "Preencha séries e repetições", Toast.LENGTH_LONG).show();
            return;
        }

        exercicio.setNome(edtNome.getText().toString());
        exercicio.setSeries(series);
        exercicio.setRepeticoes(repeticoes);

        eDAO.Atualizar(exercicio, new ExercicioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(ExercicioDetalheActivity.this, "Exercício atualizado", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(ExercicioDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ExcluirClique(View view) {
        eDAO.Excluir(exercicio, new ExercicioDAO.AcaoCallback() {
            @Override
            public void onSucesso() {
                Toast.makeText(ExercicioDetalheActivity.this, "Exercício excluído", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onErro(String msg) {
                Toast.makeText(ExercicioDetalheActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Integer parseCampoInteiro(EditText campo) {
        String valor = campo.getText().toString().trim();
        if (valor.isEmpty()) return null;
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
