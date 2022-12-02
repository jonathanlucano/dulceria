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

public class EliminarActivity extends AppCompatActivity {
    EditText n,p,d;
    TextView c;
    Button buscar, eliminar;
    Cursor reg;
    int id;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        buscar = findViewById(R.id.btnBLeerE);
        c = findViewById(R.id.LBLBProductoE);
        n = findViewById(R.id.txtNProductoE);
        p = findViewById(R.id.txtPProductoE);
        d = findViewById(R.id.txtDProductoE);

        eliminar = findViewById(R.id.btnMProductoE);

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(v);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
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

    void eliminar(View v){
        BaseDatos  conn=new BaseDatos(this,"Base Datos",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String instruccion="delete from productos where id='"+id+"'";
        db.execSQL(instruccion);
        db.close();
        Intent i = new Intent(EliminarActivity.this, UsuarioActivity.class);
        startActivity(i);
    }


    void tomarlectura(){
        Intent i = new Intent(EliminarActivity.this, CamaraActivity.class);
        startActivityForResult(i, CODIGO_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigoL = data.getStringExtra("codigo");
                    id = Integer.parseInt(data.getStringExtra("id"));
                    String nombre = data.getStringExtra("nombre");
                    String precio = data.getStringExtra("precio");
                    String descripcion = data.getStringExtra("descripcion");
                    c.setText(codigoL);
                    n.setText(nombre);
                    p.setText(precio);
                    d.setText(descripcion);
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
        int estadoDePermiso = ContextCompat.checkSelfPermission(EliminarActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(EliminarActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }


    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(EliminarActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }
}