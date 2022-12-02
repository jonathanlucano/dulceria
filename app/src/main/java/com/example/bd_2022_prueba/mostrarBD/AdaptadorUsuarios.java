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
import java.util.Random;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.UsuariosViewHolder> implements View.OnClickListener{
    ArrayList<Usuarios> usuario;
    private Context mContext;
    private View.OnClickListener listener;

    public AdaptadorUsuarios(ArrayList<Usuarios> usuario) {
        this.usuario = usuario;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =   LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuarios, null,false);
        view.setOnClickListener(this);
        return new AdaptadorUsuarios.UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.UsuariosViewHolder holder, int position) {

        holder.id.setText(Integer.toString(usuario.get(position).getId()));
        holder.nom.setText(usuario.get(position).getNombre().toString());
        Random random= new Random();
        int color = Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
        holder.imgUsuario.setColorFilter(color);
    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {
        TextView nom,id;
        ImageView imgUsuario;
        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);

            nom = itemView.findViewById(R.id.lblUsuarioMostrar);
            id = itemView.findViewById(R.id.lblIDMostrar);
            imgUsuario = itemView.findViewById(R.id.imgUsuario);
        }
    }
}
