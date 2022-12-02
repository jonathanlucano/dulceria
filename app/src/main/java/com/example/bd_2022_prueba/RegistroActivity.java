package com.example.bd_2022_prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegistroActivity extends AppCompatActivity {
EditText u,p,cp;
Button acp;
private Cursor reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
         u = findViewById(R.id.txtUsuarioR);
         p = findViewById(R.id.txtPasswordR);
         cp = findViewById(R.id.txtPasswordConfirmR);
         acp = findViewById(R.id.btnGuardarR);

         acp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 guardar(v);
             }
         });
    }
    void guardar(View v){
        String us,pas,cpas;
        us = u.getText().toString();
        pas = p.getText().toString();
        cpas = cp.getText().toString();
        if (pas.equals(cpas)) {
            if (validar(us)) {
                BaseDatos db = new BaseDatos(this, "Base Datos", null, 1);
                SQLiteDatabase conexion = db.getWritableDatabase();
                conexion.execSQL("insert into usuarios values(null,'" + us + "','" + pas + "')");
                conexion.close();
                Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(v, "Usuario ya existe", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }else{
            Toast.makeText(this, "Las contreseñas no coinsiden", Toast.LENGTH_SHORT).show();

        }
    }

    boolean validar(String us){
        BaseDatos db = new BaseDatos(this, "Base Datos", null, 1);
        SQLiteDatabase conexion = db.getReadableDatabase();
        reg = conexion.rawQuery("select * from usuarios where usuario='"+us+"'",null);
        if (reg.moveToFirst()){
            reg.close();
            return false;
        }else{
            reg.close();
            return  true;
        }

    }

}