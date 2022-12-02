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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegistroProductosActivity extends AppCompatActivity {
    EditText co, p,pr,d;
    Button acp, leer;
    private Cursor reg;



    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_productos);
        co = findViewById(R.id.txtcodigo);
        p = findViewById(R.id.txtProducto);
        pr = findViewById(R.id.txtPrecio);
        d = findViewById(R.id.txtDescripcion);
        acp = findViewById(R.id.btnGuardarP);

        leer = findViewById(R.id.btnLector);

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




        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar(v);
            }
        });
    }

    void tomarlectura(){
            Intent i = new Intent(RegistroProductosActivity.this, CamaraActivity.class);
            startActivityForResult(i, CODIGO_INTENT);
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigo = data.getStringExtra("codigo");
                    co.setText(codigo);
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
        int estadoDePermiso = ContextCompat.checkSelfPermission(RegistroProductosActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(RegistroProductosActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }


    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(RegistroProductosActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }

    void guardar(View v){
        String c,nom,pre,des;
        c = co.getText().toString();
        nom = p.getText().toString();
        pre = pr.getText().toString();
        des = d.getText().toString();
        float precio = Float.parseFloat(pre);
        BaseDatos db = new BaseDatos(this, "Base Datos", null, 1);
        SQLiteDatabase conexion = db.getWritableDatabase();
        conexion.execSQL("insert into productos values(null,'"+c+"','" + nom + "','" + precio + "','"+des+"')");
        conexion.close();
        Snackbar snackbar = Snackbar
                .make(v, "Registro Ralizado", Snackbar.LENGTH_LONG);
        snackbar.show();
        limpiardatos();
        Intent i = new Intent(RegistroProductosActivity.this,UsuarioActivity.class);
        startActivity(i);

        }

        void limpiardatos() {
            co.setText("");
            p.setText("");
            pr.setText("");
            d.setText("");

        }

}