package com.smartenergy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smartenergy.database.BancoDeDados;
import com.smartenergy.database.UsuarioDAO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ConsumoActivity extends AppCompatActivity {
    Button buttonLamp0, buttonLamp1, buttonLamp2, buttonLamp3;
    Button buttonLamp4, buttonLamp5, buttonLamp6, buttonLamp7;
    Button buttonDesligarTodos, buttonLigarTodos;

    String info[] = new String[9];

    EditText txtResultado;

    Handler handler = new Handler();

    String url = "http://172.20.10.4/";

    UsuarioDAO usuarioDAO = null;

    Usuario usuario;

    long start_app, finish_app;

    protected static  final int TIMER_RUNTIME = 10000;
    protected  boolean mActive;
    protected ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_app = System.currentTimeMillis();
        setContentView(R.layout.activity_consumo);
        setTitle("Consumo");

        Intent intent = getIntent();
        // object usuario
        usuario = (Usuario) intent.getExtras().getSerializable("usuario");
        TextView editTextBemVindo = (TextView) findViewById(R.id.textBemVindo);
        editTextBemVindo.setText("Olá " + usuario.getName() + ", aqui estão seus gastos:");
        Log.d("ABC","funfou");
        usuarioDAO = new UsuarioDAO(this);

        handler.postDelayed(atualizaConsumo, 0);

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

    /*
        buttonLamp0 = (Button) findViewById(R.id.buttonLamp0);
        buttonLamp1 = (Button) findViewById(R.id.buttonLamp1);
        buttonLamp2 = (Button) findViewById(R.id.buttonLamp2);
        buttonLamp3 = (Button) findViewById(R.id.buttonLamp3);
        buttonLamp4 = (Button) findViewById(R.id.buttonLamp4);
        buttonLamp5 = (Button) findViewById(R.id.buttonLamp5);
        buttonLamp6 = (Button) findViewById(R.id.buttonLamp6);
        buttonLamp7 = (Button) findViewById(R.id.buttonLamp7);
        buttonLigarTodos = (Button) findViewById(R.id.buttonLigarTodos);
        buttonDesligarTodos = (Button) findViewById(R.id.buttonDesligarTodos);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onDestroy(){
        Log.d("abc","fechou a activity de consumo");
        finish_app = System.currentTimeMillis();
        int total = (int)((finish_app - start_app) /1000.0);
        Log.d("abc",Integer.toString(total));
        usuarioDAO.atualizarTempo(usuario.getUserId(), total);
        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.atualizarConsumo:
                setupPieChart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupPieChart(){
        long finish_consulta = System.currentTimeMillis();
        int total = (int)((finish_consulta - start_app) /1000.0);
        int tempo_total = usuario.getTempo_segundos() + total;

        // Calculo de valor em reais de Kwh consumido
        double somaWatts = usuarioDAO.consultarConsumo(usuario.getUserId(), "2019","12");
        double energiaConsumida = (somaWatts/1000.0) * (tempo_total/3600.0);

        float valorUtilizado = (float) (energiaConsumida * usuario.getKw_hora());
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

    private Runnable atualizaConsumo = new Runnable() {
        @Override
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){

                new solicitaDados().execute(url);

                if(info[8] != null){
                    Double consumo = Double.valueOf(info[8]);
                    if(usuarioDAO.cadastrarConsumo(usuario.getUserId(), consumo)){
                        Log.d("ABC","funfou");
                    }
                    else{
                        Log.d("ABC","nao funfou");
                    }
                }
            }
            else{
                Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                        "foi detectada", Toast.LENGTH_LONG).show();
            }



            handler.postDelayed(this, 5000);
        }
    };

    private class solicitaDados extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... url) {
            return Conexao.getDados(url[0]);
        }

        @Override
        protected void onPostExecute(String resultado) {

            if(resultado != null){
                info = resultado.split(",");

            }
            else{
                Toast.makeText(ConsumoActivity.this, "Ocorreu um erro", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void lamp0(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp0";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp1(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp1";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp2(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp2";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp3(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp3";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp4(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp4";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp5(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp5";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp6(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp6";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lamp7(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "lamp7";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lampDesligaTodos(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "desligarTodos";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }

    public void lampLigaTodos(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            String cmd = "ligarTodos";

            new solicitaDados().execute(url+cmd);
        }
        else{
            Toast.makeText(ConsumoActivity.this, "Nenhuma conexão " +
                    "foi detectada", Toast.LENGTH_LONG).show();
        }
    }
}
