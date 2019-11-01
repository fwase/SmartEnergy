package com.smartenergy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smartenergy.database.BancoDeDados;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ConsumoActivity extends AppCompatActivity {
    float teste[] = {215f, 185f};
    String label[] = {"Consumo total", "Consumo utilizado"};
    Usuario usuario;

    protected static  final int TIMER_RUNTIME = 10000;
    protected  boolean mActive;
    protected ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        setTitle("Consumo");

        Intent intent = getIntent();
        // object usuario
        usuario = (Usuario) intent.getExtras().getSerializable("usuario");
        TextView editTextBemVindo = (TextView) findViewById(R.id.textBemVindo);
        editTextBemVindo.setText("Olá " + usuario.getName() + ", aqui está listado graficamente seus gastos desse mês");

        /*
        progressBar = (ProgressBar) findViewById(R.id.progressBarTeste);
        progressBar.setMax(1000);
        progressBar.setProgress(900);
        progressBar.setScaleY(2.5f);

        progressBar.getProgressDrawable().setColorFilter(ColorTemplate.rgb("#ff0006"), PorterDuff.Mode.MULTIPLY);
        //progressBar.setProgressTintList(ColorStateList.valueOf(ColorTemplate.rgb("#ff0006")));
        //progressBar.getIndeterminateDrawable().setColorFilter(ColorTemplate.rgb("#ff0006"), PorterDuff.Mode.MULTIPLY);
        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(ColorTemplate.rgb("#33cc5a")));



        progressBar.postInvalidate();

         */


        setupPieChart();
    }

    private void setupPieChart(){

        float valorUtilizado = 102.18f;
        float valores[] = {(float)usuario.getValorLimite()-valorUtilizado, valorUtilizado};
        String labels[] = {"R$ " + String.format("%.2f",(float) usuario.getValorLimite()-valorUtilizado)
                .replace(".",","),
        "R$ " + String.format("%.2f",valorUtilizado).replace(".",",")};

        // Populating a list of PieEntries

        List<PieEntry> pieEntries = new ArrayList<>();
        PieDataSet dataSet;
        if(usuario.getValorLimite()-valorUtilizado >= 0.0f) {
            for (int i = 0; i < valores.length; i++) {
                pieEntries.add(new PieEntry(valores[i], labels[i]));
            }
            dataSet = new PieDataSet(pieEntries, "");
            dataSet.setColors(ColorTemplate.rgb("#33cc5a"),ColorTemplate.rgb("#ff0006"));
        }
        else{
            pieEntries.add(new PieEntry((float)usuario.getValorLimite(), "ABC"));
            dataSet = new PieDataSet(pieEntries, "");
            dataSet.setColors(ColorTemplate.rgb("#ff0006"));
        }
        /*
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(156,254,230));
        colors.add(Color.rgb(159,185,235));
         */

        PieData data = new PieData(dataSet);

        // Get the chart
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setData(data);
        // set between 0% and 100%
        pieChart.setUsePercentValues(true);
        // disabled "Description Label" text
        pieChart.getDescription().setEnabled(false);
        // set color of legend
        //pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.animateY(1000);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);

        /*
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(15f);

         */



        /*
        // location of Pie Chart and labels in screen
        Legend l = pieChart.getLegend(); // get legend of pie
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER); // set vertical alignment for legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        l.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        l.setDrawInside(false);
        */





        /*
        Legend legend = pieChart.getLegend();
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.WHITE);
         */


        List<LegendEntry> entries = new ArrayList<>();
        LegendEntry entry1 = new LegendEntry();
        entry1.formColor = ColorTemplate.rgb("#33cc5a");
        entry1.label = "Valor restante";
        entries.add(entry1);

        LegendEntry entry2 = new LegendEntry();
        entry2.formColor = ColorTemplate.rgb("#ff0006");
        entry2.label = "Valor consumido";
        entries.add(entry2);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setCustom(entries);
        legend.setTextSize(15f);





        pieChart.invalidate();
    }
}
