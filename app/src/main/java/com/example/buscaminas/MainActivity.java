package com.example.buscaminas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TableLayout tl_table;

    private Button bt_new;
    private Button bt_change;

    private static TextView tv_bombs;
    private static TextView tv_dif;

    public static int totalBombs;
    Chronometer chronometer;
    private ActivityResultLauncher<Intent> mStartForResult;
    private static Button[][] matrix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl_table = findViewById(R.id.tl_table);

        bt_new = findViewById(R.id.bt_new);
        bt_change = findViewById(R.id.bt_dif);

        tv_bombs = findViewById(R.id.tv_bombs);
        tv_dif = findViewById(R.id.tv_dif);

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
        chronometer = Cronometro();

        bt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.Explode(chronometer);
            }
        });
    }

    private void createMatrix() {
        for (int i = 0; i < matrix.length; i++){
            TableRow tr = new TableRow(this);
            tl_table.addView(tr);
            for (int j = 0; j < matrix[0].length; j++){
                Button btn = new Button(this);
                btn.setText(Integer.toString(j));
                btn.setId(i);
                btn.setWidth(10);
                btn.setHeight(10);
                tr.addView(btn);
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
    }
    public void Cdifficulty(){
        switch (StaticCosas.Dif){
            case 1:
                tv_dif.setText(R.string.dif1);
                totalBombs = 10;
                matrix = new Button[8][8];
                createMatrix();
                Functions.UpdateUI();
                break;
            case 2:
                tv_dif.setText(R.string.dif2);
                totalBombs = 40;
                matrix = new Button[16][16];
                createMatrix();
                Functions.UpdateUI();
                break;
            case 3:
                tv_dif.setText(R.string.dif3);
                totalBombs = 99;
                matrix = new Button[16][30];
                createMatrix();
                Functions.UpdateUI();

        }
    }

    public static class Functions {

        public static void UpdateUI(){
            tv_bombs.setText(Integer.toString(totalBombs));

        }

        public void Reset(){}

        public static void Explode(Chronometer chronometer){
            chronometer.stop();
        }

    }
}