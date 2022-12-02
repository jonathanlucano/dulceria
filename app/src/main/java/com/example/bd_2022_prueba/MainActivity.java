package com.example.bd_2022_prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText u,p;
Button acp;
private Cursor reg;
TextView r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        u = findViewById(R.id.txtUsuario);
        p = findViewById(R.id.txtPassword);
        acp = findViewById(R.id.btnEntrar);
        r = findViewById(R.id.lblRegistrar);
        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegistroActivity.class);
                startActivity(i);
            }
        });

    }
    void validar(){
        String us,pas;
        us = u.getText().toString();
        pas = p.getText().toString();
        BaseDatos db1 = new BaseDatos(this,"Base Datos",null,1);
        SQLiteDatabase conexion=db1.getWritableDatabase();
        reg = conexion.rawQuery("select * from usuarios where usuario='"+us+"' and password='"+pas+"'",null);
        if (reg.moveToFirst()){
            String ubd = reg.getString(1);
            String pbd = reg.getString(2);
            if (us.equals(ubd) && pas.equals(pbd)){
                Intent i = new Intent(MainActivity.this, UsuarioActivity.class);
                i.putExtra("nombre",us);
                startActivity(i);
            }else{
                Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
        }
        conexion.close();

    }

}