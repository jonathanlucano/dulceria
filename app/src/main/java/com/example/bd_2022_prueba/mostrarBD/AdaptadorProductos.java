package com.example.bd_2022_prueba.mostrarBD;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bd_2022_prueba.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductosViewHolder> implements View.OnClickListener{
    ArrayList<Productos> listaProductos;
    private Context mContext;
    private View.OnClickListener listener;


    public AdaptadorProductos(ArrayList<Productos> listaProductos ) {
        this.listaProductos = listaProductos;


    }
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos, null,false);
        view.setOnClickListener(this);
        return new ProductosViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {

       holder.id.setText(Integer.toString(listaProductos.get(position).getId()));
       holder.codigo.setText(listaProductos.get(position).getCodigo().toString());
       holder.nombre.setText(listaProductos.get(position).getNombre().toString());
       holder.precio.setText(Float.toString(listaProductos.get(position).getPrecio()));
       holder.descripcion.setText(listaProductos.get(position).getDescripcion().toString());

        Random random= new Random();
        int color = Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
        holder.img.setColorFilter(color);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder {
        TextView id, codigo, nombre,precio,descripcion;
        ImageView img;
        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.lblIDP);
            codigo = itemView.findViewById(R.id.lblCodigoP);
            nombre = (TextView) itemView.findViewById(R.id.lblNombreP);
            precio = (TextView) itemView.findViewById(R.id.lblPrecioP);
            descripcion = (TextView) itemView.findViewById(R.id.lblDescripcionP);
            img = (ImageView)itemView.findViewById(R.id.img);
        }



    }
}
