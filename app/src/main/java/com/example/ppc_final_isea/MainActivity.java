package com.example.ppc_final_isea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarMenuInf();
    }

    private void iniciarMenuInf() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.page_inicio);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.page_inicio) {
                // Do nothing, already on "inicio" page
            } else if (itemId == R.id.page_riesgo) {
                goScreen2();
            } else if (itemId == R.id.page_info) {
                goScreen3();
            } else if (itemId == R.id.page_carga) {
                goScreen4();
            } else {
                // Handle unexpected icon
                Toast.makeText(getApplicationContext(),
                        "Icono no encontrado", Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }


    //Metodo para que al volver se seleccione el icono correspondiente en el menu
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bottomNavigationView.setSelectedItemId(R.id.page_inicio);
    }

    public void goScreen2(View view) {
        Intent act2 = new Intent(this, Screen2Activity.class);
        startActivity(act2);
    }

    public void goScreen3(View view) {
        Intent act3 = new Intent(this, Screen3Activity.class);
        startActivity(act3);
    }

    public void goScreen4(View view) {
        Intent act4 = new Intent(this, Screen4Activity.class);
        startActivity(act4);
    }

    public void goScreen5(View view) {
        Intent act5 = new Intent(this, Screen5Activity.class);
        startActivity(act5);
    }

    public void goScreen2() {
        Intent act2 = new Intent(this, Screen2Activity.class);
        startActivity(act2);
    }

    public void goScreen3() {
        Intent act3 = new Intent(this, Screen3Activity.class);
        startActivity(act3);
    }

    public void goScreen4() {
        Intent act4 = new Intent(this, Screen4Activity.class);
        startActivity(act4);
    }

    public void goScreen5() {
        Intent act5 = new Intent(this, Screen5Activity.class);
        startActivity(act5);
    }

}