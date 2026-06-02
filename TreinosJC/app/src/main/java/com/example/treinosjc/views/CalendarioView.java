package com.example.treinosjc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioView extends View {

    private Paint paintCirculoTreinado;
    private Paint paintCirculoNormal;
    private Paint paintTextoDia;
    private Paint paintTextoDiaSemana;
    private Paint paintTextoTreinado;
    private Paint paintTextoCabecalho;

    private List<Integer> diasTreinados = new ArrayList<>();
    private int ano;
    private int mes;

    private static final String[] DIAS_SEMANA = {"D", "S", "T", "Q", "Q", "S", "S"};
    private static final String[] NOMES_MES   = {
            "Janeiro","Fevereiro","Marco","Abril","Maio","Junho",
            "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"
    };

    public CalendarioView(Context context) {
        super(context);
        init();
    }

    public CalendarioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintCirculoTreinado = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCirculoTreinado.setColor(Color.parseColor("#4CAF50"));
        paintCirculoTreinado.setStyle(Paint.Style.FILL);

        paintCirculoNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCirculoNormal.setColor(Color.parseColor("#F0F0F0"));
        paintCirculoNormal.setStyle(Paint.Style.FILL);

        paintTextoDia = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoDia.setColor(Color.parseColor("#333333"));
        paintTextoDia.setTextSize(36f);
        paintTextoDia.setTextAlign(Paint.Align.CENTER);

        paintTextoTreinado = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoTreinado.setColor(Color.WHITE);
        paintTextoTreinado.setTextSize(36f);
        paintTextoTreinado.setTextAlign(Paint.Align.CENTER);

        paintTextoDiaSemana = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoDiaSemana.setColor(Color.parseColor("#888888"));
        paintTextoDiaSemana.setTextSize(32f);
        paintTextoDiaSemana.setTextAlign(Paint.Align.CENTER);

        paintTextoCabecalho = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextoCabecalho.setColor(Color.parseColor("#222222"));
        paintTextoCabecalho.setTextSize(42f);
        paintTextoCabecalho.setTextAlign(Paint.Align.CENTER);
        paintTextoCabecalho.setFakeBoldText(true);

        Calendar cal = Calendar.getInstance();
        ano = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
    }

    public void setDadosMes(int ano, int mes, List<Integer> diasTreinados) {
        this.ano = ano;
        this.mes = mes;
        this.diasTreinados = diasTreinados != null ? diasTreinados : new ArrayList<>();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = MeasureSpec.getSize(widthMeasureSpec);
        int height = 80 + 50 + (6 * 60) + 40;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float largura     = getWidth();
        float celula      = largura / 7f;
        float raioCirculo = celula * 0.38f;

        String cabecalho = NOMES_MES[mes - 1] + " " + ano;
        canvas.drawText(cabecalho, largura / 2f, 60f, paintTextoCabecalho);

        for (int i = 0; i < 7; i++) {
            float cx = celula * i + celula / 2f;
            canvas.drawText(DIAS_SEMANA[i], cx, 120f, paintTextoDiaSemana);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes - 1, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);

        int primeiroDiaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int totalDias         = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar hoje = Calendar.getInstance();
        int diaHoje = (hoje.get(Calendar.YEAR)  == ano &&
                hoje.get(Calendar.MONTH) == mes - 1)
                ? hoje.get(Calendar.DAY_OF_MONTH) : -1;

        int coluna = primeiroDiaSemana;
        int linha  = 0;

        for (int dia = 1; dia <= totalDias; dia++) {
            float cx = celula * coluna + celula / 2f;
            float cy = 150f + linha * 60f + 25f;

            boolean treinado = diasTreinados.contains(dia);
            boolean ehHoje = (dia == diaHoje);

            if (treinado) {
                canvas.drawCircle(cx, cy, raioCirculo, paintCirculoTreinado);
                canvas.drawText(String.valueOf(dia), cx, cy + 13f, paintTextoTreinado);
            } else if (ehHoje) {
                canvas.drawCircle(cx, cy, raioCirculo, paintCirculoNormal);
                canvas.drawText(String.valueOf(dia), cx, cy + 13f, paintTextoDia);
            } else {
                canvas.drawText(String.valueOf(dia), cx, cy + 13f, paintTextoDia);
            }
            coluna++;
            if (coluna == 7) {
                coluna = 0;
                linha++;
            }
        }
    }
}