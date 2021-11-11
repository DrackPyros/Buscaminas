package com.example.buscaminas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    private final Random rnd = new Random();
    private ActivityResultLauncher<Intent> mStartForResult;

    private TableLayout tl_table;

    private TextView tv_bombs;
    private TextView tv_dif;
    private TextView tv_end;

    public int totalBombs;
    private int totalis;
    private int visible;
    private boolean creado = false;
    private boolean fin = false;

    private static ImageButton[][] matrix;
    private static int[][] findbomb;
    private static boolean[][] flag;
    private static boolean[][] shown;
    private int Dif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl_table = findViewById(R.id.tl_table);

        Button bt_new = findViewById(R.id.bt_new);

        tv_bombs = findViewById(R.id.tv_bombs);
        tv_dif = findViewById(R.id.tv_dif);
        tv_end = findViewById(R.id.tv_end);

        tl_table.setPadding(10, 10, 10, 10);
        tl_table.setBackgroundColor(Color.rgb(138,43,226));

        bt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Dif = intent.getIntExtra(DifActivity.RATING_KEY, 0);
                            Cdifficulty();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        openDiffActivity();
    }

    private void createMatrix() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTable(view);
                updateUI(view);
                explode(chronometer, view);
                wincon(chronometer, view);
            }
        };
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!fin) {
                    wincon(chronometer, view);
                    int resultado = view.getId() / findbomb.length;
                    int resto = view.getId() % findbomb.length;
                    if (!flag[resultado][resto]) {
                        flag[resultado][resto] = true;

                        ImageButton aux = matrix[resultado][resto];
                        aux.setImageResource(R.drawable.bandera);
                        aux.setPadding(0, 0, 0, 0);
                        android.view.ViewGroup.LayoutParams params = aux.getLayoutParams();
                        params.height = aux.getHeight();
                        params.width = aux.getWidth();
                        aux.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        aux.setLayoutParams(params);
                    } else {
                        flag[resultado][resto] = false;
                        matrix[resultado][resto].setImageResource(0);
                    }
                }
                return true;

            }
        };
        int id = 0;
        for (int i = 0; i < matrix.length; i++){
            TableRow tr = new TableRow(this);
            for (int j = 0; j < matrix[0].length; j++){
                findbomb[i][j] = 0;
                ImageButton btn = new ImageButton(this);
                btn.setOnClickListener(clickListener);
                btn.setOnLongClickListener(longClickListener);
                btn.setMinimumWidth(90);
                btn.setMinimumHeight(90);
                btn.setId(id);
                tr.addView(btn);
                matrix[i][j] = btn;
                if (id <= totalis){
                    id ++;
                }
            }
            tl_table.addView(tr);
        }
    }

    private void end(View view) {
        if (creado && !fin){
            fin = true;
            int resultado = view.getId() / findbomb.length;
            int resto = view.getId() % findbomb.length;

            for (int x = 0; x < findbomb.length; x++){
                for (int y = 0; y < findbomb[0].length; y++) {
                    if (!flag[x][y]) {
                        setimage(x, y, resultado, resto);
                    }
                }
            }
        }
    }

    private void setimage(int x, int y, int resultado, int resto) {
        ImageButton aux = matrix[x][y];
        switch (findbomb[x][y]) {
            case -1:
                if (x == resultado && y == resto){
                    aux.setImageResource(R.drawable.explosion);
                }
                else
                    aux.setImageResource(R.drawable.minita);
                break;
            case 0:
                aux.setImageResource(R.drawable.un_punto);
                break;
            case 1:
                aux.setImageResource(R.drawable.primer);
                break;
            case 2:
                aux.setImageResource(R.drawable.segond);
                break;
            case 3:
                aux.setImageResource(R.drawable.tercer);
                break;
            case 4:
                aux.setImageResource(R.drawable.quart);
                break;
            case 5:
                aux.setImageResource(R.drawable.cinque);
                break;
            case 6:
                aux.setImageResource(R.drawable.sise);
                break;
            case 7:
                aux.setImageResource(R.drawable.sete);
                break;
            case 8:
                aux.setImageResource(R.drawable.huite);

        }
        aux.setPadding(0, 0, 0, 0);
        android.view.ViewGroup.LayoutParams params = aux.getLayoutParams();
        params.height = aux.getHeight();
        params.width = aux.getWidth();
        aux.setScaleType(ImageView.ScaleType.FIT_CENTER);
        aux.setLayoutParams(params);
    }

    private void createTable(View view) {
        if (!creado){
            creado = true;
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

    private void cleanTable(TableLayout table) {
        int childCount = table.getChildCount();
        table.removeViews(0, childCount);
    }

    private void openDiffActivity() {
        tv_end.setVisibility(View.INVISIBLE);
        fin = false;
        Intent intent = new Intent(this, DifActivity.class);
        mStartForResult.launch(intent);
    }

    @SuppressLint("SetTextI18n")
    public void Cdifficulty() {
        chronometer = Cronometro();
        switch (Dif) {
            case 1:
                tv_dif.setText(R.string.dif1);
                totalBombs = 4;
                matrix = new ImageButton[5][5];
                break;
            case 2:
                tv_dif.setText(R.string.dif2);
                totalBombs = 10;
                matrix = new ImageButton[8][8];
                break;
            case 3:
                tv_dif.setText(R.string.dif3);
                totalBombs = 30;
                matrix = new ImageButton[12][12];
        }
        totalis = matrix.length * matrix[0].length;
        findbomb = new int[matrix.length][matrix[0].length];
        flag = new boolean[matrix.length][matrix[0].length];
        shown = flag;
        createMatrix();
        tv_bombs.setText(Integer.toString(totalBombs));
    }

    public void explode(Chronometer chronometer, View view){
        int id = view.getId();
        int resultado = id / findbomb.length;
        int resto = id % findbomb.length;
        if (creado && findbomb[resultado][resto] == -1){
            chronometer.stop();
            end(view);
            tv_end.setVisibility(View.VISIBLE);
            tv_end.setText(R.string.lose);
            tv_end.setTextColor(Color.RED);
        }
    }

    private void wincon(Chronometer chronometer, View view) {
        if (creado && visible+totalBombs == totalis){
            chronometer.stop();
            tv_end.setVisibility(View.VISIBLE);
            tv_end.setText(R.string.win);
            tv_end.setTextColor(Color.GREEN);
            end(view);
        }
    }

    private Chronometer Cronometro() {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("Time Running - %s");

        return chronometer;
    }

    public void updateUI(View view){
        if (creado){
            int horizontal = view.getId() / findbomb.length;
            int vertical = view.getId() % findbomb.length;
            mostrar(horizontal, vertical);
        }

    }

    private void mostrar(int horizontal, int vertical) {
        if(shown[horizontal][vertical]) return;
        if (findbomb[horizontal][vertical] >= 0){
            shown[horizontal][vertical] = true;
            visible++;
            setimage(horizontal, vertical, horizontal, vertical);
        }
        if (findbomb[horizontal][vertical] == 0){
            for (int y = (vertical -1 >= 0 ? vertical -1 : vertical); y <= (vertical +1 < findbomb[0].length ? vertical +1 : vertical); y++) { //Vertical
                for (int x = (horizontal - 1 >= 0 ? horizontal - 1 : horizontal); x <= (horizontal + 1 < findbomb.length ? horizontal + 1 : horizontal); x++) { //Horizontal
                    if (y != vertical || x != horizontal) {
                        mostrar(x, y);
                    }
                }
            }
        }
    }
}