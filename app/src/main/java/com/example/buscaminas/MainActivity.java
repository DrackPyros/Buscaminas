package com.example.buscaminas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TableLayout tl_table;

    private Button bt_new;
    private Button bt_change;

    private static TextView tv_bombs;
    private static TextView tv_dif;

    public static int totalBombs;
    Chronometer chronometer;
    private ActivityResultLauncher<Intent> mStartForResult;
    private static ImageButton[][] matrix;
    private static int[][] findbomb;
    private static boolean[][] bandera;
    private boolean creado = false;
    private int totalis;
    private Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl_table = findViewById(R.id.tl_table);

        bt_new = findViewById(R.id.bt_new);

        tv_bombs = findViewById(R.id.tv_bombs);
        tv_dif = findViewById(R.id.tv_dif);



        bt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Functions.Explode(chronometer);
                cleanTable(tl_table);
                openDiffActivity();
                creado = false;
            }
        });

        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            StaticCosas.Dif = intent.getIntExtra(DifActivity.RATING_KEY, 0);
                            //Log.d("MainActivity", String.format("Rating: %d", StaticCosas.Dif));
                            start();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        openDiffActivity();
    }
    private void cleanTable(TableLayout table) {
        int childCount = table.getChildCount();
        table.removeViews(0, childCount);
    }
    private void createMatrix() {
        View.OnClickListener btListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create(view);
                show(view);
            }
        } ;
        int aux = 0;
        for (int i = 0; i < matrix.length; i++){
            TableRow tr = new TableRow(this);
            for (int j = 0; j < matrix[0].length; j++){
                findbomb[i][j] = 0;
                ImageButton btn = new ImageButton(this);
                btn.setOnClickListener(btListener);
                btn.setMinimumWidth(90);
                btn.setMinimumHeight(90);
                btn.setId(aux);
                tr.addView(btn);
                matrix[i][j] = btn;
                if (aux <= totalis){
                    aux ++;
                }
            }
            tl_table.addView(tr);
        }
        //matrix[0][0].setImageResource(R.drawable.minita);
    }

    private void show(View view) {
        if (creado){
            System.out.println("-----------------"+view.getId());
            for (int j = 0; j< findbomb.length; j++){
                for (int x = 0; x<findbomb[0].length; x++){
                    System.out.print(findbomb[j][x]+" ");
                    if (findbomb[j][x] == -1){
                        //Log.d("<<<<>>>>>", "alto antes: "+matrix[j][x].getHeight());
                        //Log.d("<<<<<<<>>>>>", "ancho antes: "+ matrix[j][x].getWidth());
                        matrix[j][x].setImageResource(R.drawable.minita);
                        android.view.ViewGroup.LayoutParams params = matrix[j][x].getLayoutParams();
                        params.height = (int)(matrix[j][x].getHeight()* 0.8);
                        params.width = (int) (matrix[j][x].getWidth()* 0.8);
                        matrix[j][x].setScaleX(0.5f);
                        matrix[j][x].setScaleY(0.5f);
                        matrix[j][x].setCropToPadding(false);
                        matrix[j][x].setLayoutParams(params);
                        //Log.d("<<<<>>>>>", "alto despues: "+matrix[j][x].getHeight());
                        //Log.d("<<<<<<<>>>>>", "ancho despues: "+ matrix[j][x].getWidth());
                    }
                    /*int m = j+x;
                    if (m == view.getId()){
                        System.out.println("-----------------"+findbomb[j][x]);
                    }*/
                }
                System.out.println();
            }
        }
    }

    private void create(View view) {
        if (!creado){
            creado = true;
            int contador = 0;
            int a = view.getId();
            int[] b = new int[totalBombs];

            for (int i = 0; i < totalBombs; i++){
                int aux = rnd.nextInt(totalis);
                for (int z = 0; z < b.length; z++){
                    if (aux == a || aux == b[z]){
                        aux = rnd.nextInt(totalis);
                    }
                }
                int resultado = aux / findbomb.length;
                int resto = aux % findbomb.length;
                findbomb[resultado][resto] = -1;
                b[i] = aux;
            }
            for(int i = 0; i < findbomb.length; i++) { //Horizontal
                for (int j = 0; j < findbomb[0].length; j++) {  //Vertical

                    if (findbomb[i][j] != -1) {

                        for (int y = (j-1 >= 0 ? j-1 : j); y <= (j+1 < findbomb[0].length ? j+1 : j); y++){ //Vertical
                            for (int x = (i-1 >= 0 ? i-1 : i); x <= (i+1 < findbomb.length ? i+1 : i); x++){ //Horizontal

                                if ( y != j || x != i) {
                                    if (findbomb[x][y] == -1) {
                                        findbomb[i][j]++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Chronometer Cronometro() {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("Time Running - %s");

        return chronometer;
    }

    private void openDiffActivity() {
        Intent intent = new Intent(this, DifActivity.class);
        mStartForResult.launch(intent);
    }

    private void start() {
        //Log.d("MainActivity", String.format("Esto va: %d", StaticCosas.Dif));
        Cdifficulty();
        chronometer = Cronometro();
    }
    public void Cdifficulty(){
        switch (StaticCosas.Dif){
            case 1:
                tv_dif.setText(R.string.dif1);
                totalBombs = 10;
                totalis = 8*8;
                matrix = new ImageButton[8][8];
                findbomb = new int[8][8];
                bandera = new boolean[8][8];
                createMatrix();
                Functions.UpdateUI();
                break;
            case 2:
                tv_dif.setText(R.string.dif2);
                totalBombs = 40;
                totalis = 16*16;
                matrix = new ImageButton[16][16];
                findbomb = new int[16][16];
                bandera = new boolean[16][16];
                createMatrix();
                Functions.UpdateUI();
                break;
            case 3:
                tv_dif.setText(R.string.dif3);
                totalBombs = 99;
                totalis = 16*30;
                matrix = new ImageButton[30][16];
                findbomb = new int[30][16];
                bandera = new boolean[30][16];
                createMatrix();
                Functions.UpdateUI();

        }
    }

    public static class Functions {

        public static void UpdateUI(){
            tv_bombs.setText(Integer.toString(totalBombs));

        }

        public static void Explode(Chronometer chronometer){
            chronometer.stop();
        }

    }
}