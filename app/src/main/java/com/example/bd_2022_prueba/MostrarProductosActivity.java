package com.example.bd_2022_prueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bd_2022_prueba.mostrarBD.AdaptadorProductos;
import com.example.bd_2022_prueba.mostrarBD.Productos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MostrarProductosActivity extends AppCompatActivity {
    ArrayList<Productos> listaProductos;
    RecyclerView recyclerViewProductos;

    BaseDatos conn;
    int ide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos);


        conn=new BaseDatos(this,"Base Datos",null,1);
        listaProductos=new ArrayList<>();

        recyclerViewProductos = findViewById(R.id.ProductosRecycler);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        consultarListaProductos();
        AdaptadorProductos adapter=new AdaptadorProductos(listaProductos);
        recyclerViewProductos.setHasFixedSize(true);
        recyclerViewProductos.setAdapter(adapter);
        //consultarListaProductos();

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ide = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view)).getId();
                String codigo = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view)).getCodigo();
                String nombre = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view)).getNombre();
                float precio = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view)).getPrecio();
                String descripcion = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view)).getDescripcion();
                //Menu
                final CharSequence []opciones={"Modificar","Eliminar"};
                final AlertDialog.Builder alertaOpciones = new AlertDialog.Builder(MostrarProductosActivity.this);
                alertaOpciones.setTitle("Operaciones de usuarios");
                alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (opciones[id].equals("Modificar")){
                            showSuccessDialog(nombre,precio,descripcion);
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
    private void showSuccessDialog(String nombre, float precio, String descripcion){
        AlertDialog.Builder builder = new AlertDialog.Builder(MostrarProductosActivity.this, R.style.AlertDialogTheme);
        builder.setTitle("Ventana de cambios");
        View view = LayoutInflater.from(MostrarProductosActivity.this).inflate(
                R.layout.layout_modificar,
                findViewById(R.id.layoutDialogRelative)
        );
        builder.setView(view);
        EditText nnuevo = view.findViewById(R.id.txtNombrePM);
        EditText nprecio = view.findViewById(R.id.txtPrecioPM);
        EditText ndescripcion = view.findViewById(R.id.txtDescripcionPM);
        nnuevo.setText(nombre);
        nprecio.setText(Float.toString(precio));
        ndescripcion.setText(descripcion);
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
    private void consultarListaProductos() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Productos producto=null;
        Cursor cursor = db.rawQuery("select * from productos",null);
        while (cursor.moveToNext()) {
            producto = new Productos();
            producto.setId(cursor.getInt(0));
            producto.setCodigo(cursor.getString(1));
            producto.setNombre(cursor.getString(2));
            producto.setPrecio(cursor.getFloat(3));
            producto.setDescripcion(cursor.getString(4));
            listaProductos.add(producto);
        }
        cursor.close();
    }


}