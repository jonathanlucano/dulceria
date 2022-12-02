package com.example.bd_2022_prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UsuarioActivity extends AppCompatActivity {
Button agregar, modificar, eliminar,mostrar, mostrarusuarios;
String nombreusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);



        Bundle parametros = this.getIntent().getExtras();
        nombreusuario = parametros.getString("nombre");



        agregar = findViewById(R.id.btnRegistro);
        modificar = findViewById(R.id.btnModificar);
        eliminar = findViewById(R.id.btnEliminar);
        mostrar = findViewById(R.id.btnConsultar);
        mostrarusuarios = findViewById(R.id.btnMostrarUsuarios);

        mostrarusuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(UsuarioActivity.this, MostrarUsuariosActivity.class);
                i.putExtra("nombre",nombreusuario);
               startActivity(i);
            }
        });



        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsuarioActivity.this, MostrarProductosActivity.class);
                startActivity(i);
            }
        });


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsuarioActivity.this, RegistroProductosActivity.class);
                startActivity(i);
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsuarioActivity.this, ModificarActivity.class);
                startActivity(i);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsuarioActivity.this, EliminarActivity.class);
                startActivity(i);
            }
        });

    }
}