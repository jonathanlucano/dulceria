package com.example.bd_2022_prueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ModificarActivity extends AppCompatActivity {
     TextView codigo;
    Button buscar, leer, mod;
    Cursor reg;
    EditText n,p,d;
    int id;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        leer = findViewById(R.id.btnBLeer);
        codigo = findViewById(R.id.LBLBProducto);
        n = findViewById(R.id.txtNProducto);
        p = findViewById(R.id.txtPProducto);
        d = findViewById(R.id.txtDProducto);

        mod = findViewById(R.id.btnMProducto);

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(v);
            }
        });

        buscar = findViewById(R.id.btnBProducto);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
        leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permisoCamaraConcedido) {
                    //Toast.makeText(RegistroProductosActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                tomarlectura();
            }
        });

    }

    void modificar(View v){
        float precion = Float.parseFloat(p.getText().toString());
        BaseDatos db1=new BaseDatos(this,"Base Datos",null,1);
        SQLiteDatabase db=db1.getWritableDatabase();
        String instruccion="update productos set nombre='"+n.getText().toString()+"',precio='"+precion+"',Descripcion='"+d.getText().toString()+"' where id='"+id+"'";
        db.execSQL(instruccion);
        Snackbar snackbar = Snackbar
                .make(v, "la modificacion se ha Ralizado", Snackbar.LENGTH_LONG);
        snackbar.show();
        Intent i = new Intent(ModificarActivity.this, UsuarioActivity.class);
        startActivity(i);
    }


    void validar(){
        String cb;
        cb = codigo.getText().toString();

        BaseDatos db1 = new BaseDatos(this,"Base Datos",null,1);
        SQLiteDatabase conexion=db1.getWritableDatabase();
        reg = conexion.rawQuery("select * from productos where codigo='"+cb+"'",null);
        if (reg.moveToFirst()){
             id = reg.getInt(0);
            String nom = reg.getString(2);
            String pre = reg.getString(3);
            String des = reg.getString(4);
            n.setText(nom);
            p.setText(pre);
            d.setText(des);

        }else{
            Toast.makeText(this, "El producto no existe", Toast.LENGTH_SHORT).show();
        }
        conexion.close();

    }





    void tomarlectura(){
        Intent i = new Intent(ModificarActivity.this, CamaraActivity.class);
        startActivityForResult(i, CODIGO_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigoL = data.getStringExtra("codigo");
                    codigo.setText(codigoL);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        tomarlectura();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(ModificarActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(ModificarActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }


    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(ModificarActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }
}