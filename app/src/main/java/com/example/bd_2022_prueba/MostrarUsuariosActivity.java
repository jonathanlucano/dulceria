package com.example.bd_2022_prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bd_2022_prueba.mostrarBD.AdaptadorProductos;
import com.example.bd_2022_prueba.mostrarBD.AdaptadorUsuarios;
import com.example.bd_2022_prueba.mostrarBD.Productos;
import com.example.bd_2022_prueba.mostrarBD.Usuarios;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MostrarUsuariosActivity extends AppCompatActivity {
    ArrayList<Usuarios> usuarios;
    RecyclerView recyclerView;
    String nombreusuario;
    BaseDatos conn;
    int ide;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_usuarios);

        Bundle parametros = this.getIntent().getExtras();
        nombreusuario = parametros.getString("nombre");

        recyclerView = findViewById(R.id.recyclerUsuarios);
        login = findViewById(R.id.UsuarioLogeado);
        login.setText(nombreusuario);

        conn=new BaseDatos(this,"Base Datos",null,1);
        usuarios = new ArrayList<Usuarios>();

        llenarusuarios();
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        AdaptadorUsuarios adapter=new AdaptadorUsuarios(usuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ide = usuarios.get(recyclerView.getChildAdapterPosition(view)).getId();
                String nombre = usuarios.get(recyclerView.getChildAdapterPosition(view)).getNombre();
               /* Snackbar snackbar = Snackbar
                        .make(view, "Nombre "+nombre+ " ID: "+ide, Snackbar.LENGTH_LONG);
                snackbar.show();*/
                //Menu
                final CharSequence []opciones={"Modificar","Eliminar"};
                final AlertDialog.Builder alertaOpciones = new AlertDialog.Builder(MostrarUsuariosActivity.this);
                alertaOpciones.setTitle("Operaciones de usuarios");
                alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (opciones[id].equals("Modificar")){
                            showSuccessDialog(nombre,ide);
                        }else
                        if(opciones[id].equals("Eliminar")){
                            Eliminar();
                        }
                        else{
                            dialog.dismiss();
                        }

                    }
                });
                alertaOpciones.show();
                //fin de menu
            }
        });


    }

    private void showSuccessDialog(String nombre, int ide){
        AlertDialog.Builder builder = new AlertDialog.Builder(MostrarUsuariosActivity.this, R.style.AlertDialogTheme);
        builder.setTitle("Ventnana de cambios");
        View view = LayoutInflater.from(MostrarUsuariosActivity.this).inflate(
                R.layout.layout_modificar,
                findViewById(R.id.layoutDialogRelative)
        );
        builder.setView(view);
        EditText nnuevo = view.findViewById(R.id.txtNombrePM);
        EditText nprecio = view.findViewById(R.id.txtPrecioPM);
        EditText ndescripcion = view.findViewById(R.id.txtDescripcionPM);
        nnuevo.setText(nombre);
        nprecio.setText(toString());
        ndescripcion.setText(toString());
        ((Button) view.findViewById(R.id.btnActualizarP)).setText("OK");


        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnActualizarP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db=conn.getWritableDatabase();
                String instruccion="update productos set nombre='"+nnuevo.getText().toString()+"',precio='"+nprecio.getText()+"',descripcion='"+ndescripcion.getText().toString()+"' where id='"+ide+"'";
                db.execSQL(instruccion);
                db.close();
                alertDialog.dismiss();
                finish();
                startActivity(getIntent());

            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }


    void Eliminar(){
        SQLiteDatabase db=conn.getWritableDatabase();
        String instruccion="delete from productos where id='"+ide+"'";
        db.execSQL(instruccion);
        db.close();
        finish();
        startActivity(getIntent());
    }

    void llenarusuarios(){
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from usuarios",null);
        Usuarios usuario = null;
        while(cursor.moveToNext()){
            usuario = new Usuarios();
            if (!cursor.getString(1).equals(nombreusuario)) {
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuarios.add(usuario);
            }
        }

    }
}