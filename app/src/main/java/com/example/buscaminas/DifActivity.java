package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DifActivity extends AppCompatActivity {

    public static final String RATING_KEY = "RATING_KEY";

    private Button bt_f1;
    private Button bt_f2;
    private Button bt_f3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difactyvity);

        bt_f1 = findViewById(R.id.bt_dif1);
        bt_f2 = findViewById(R.id.bt_dif2);
        bt_f3 = findViewById(R.id.bt_dif3);

        Button[] a = {bt_f1, bt_f2, bt_f3};

        for (int i = 0; i< a.length; i++){
            a[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bt_f1.equals(view)){
                        Intent intent = new Intent();
                        intent.putExtra(RATING_KEY, 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else if (bt_f2.equals(view)){
                        Intent intent = new Intent();
                        intent.putExtra(RATING_KEY, 2);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.putExtra(RATING_KEY, 3);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
        }
    }
}
