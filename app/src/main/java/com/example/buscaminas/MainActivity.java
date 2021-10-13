package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TableLayout tl_table;

    private Button bt_new;
    private Button bt_change;

    private TextView tv_time;
    private TextView tv_bombs;
    private TextView tv_dif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl_table = findViewById(R.id.tl_table);

        bt_new = findViewById(R.id.bt_new);
        bt_change = findViewById(R.id.bt_dif);

        tv_time = findViewById(R.id.tv_time);
        tv_bombs = findViewById(R.id.tv_bombs);
        tv_dif = findViewById(R.id.tv_dif);

    }

    public void UpdateUI(){}

    public static class Functions {

        public void Cdifficulty(){}

        public void Reset(){}

        public void Explode(){}
    }
}