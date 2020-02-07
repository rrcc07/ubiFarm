package com.rrcc.ubifarm01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rrcc.ubifarm01.ClasesDeObjetos.Farmacia;
import com.rrcc.ubifarm01.ClasesDeObjetos.Sucursal;

import java.util.ArrayList;
import java.util.List;

public class adapterItemFarmacia extends
        RecyclerView.Adapter<adapterItemFarmacia.ViewHolderFarmacia>
        implements View.OnClickListener{

    Context mContext;
    List<Sucursal> listaSucursales;

    private View.OnClickListener listener;

    public adapterItemFarmacia(Context mContext, List<Sucursal> listaSucursales) {
        this.mContext = mContext;
        this.listaSucursales = listaSucursales;
    }

    //Enlace el adapter con el item_Farmacia
    @NonNull
    @Override
    public ViewHolderFarmacia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmacia,parent,false);
        view.setOnClickListener(this);
        return new ViewHolderFarmacia(view);
    }
    //enlace adaptador y viewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolderFarmacia holder, final int position) {
        holder.nombreDeFarmacia.setText(listaSucursales.get(position).getNombreFarmacia());
        holder.direccionFarmacia.setText(listaSucursales.get(position).getDireccionSucursal());
        holder.nombreDeSucursal.setText(listaSucursales.get(position).getNombreSucursal());
        holder.telefonoFarmacia.setText(listaSucursales.get(position).getTelefonoSucursal());
        // ahora usando Glide cargaremos la foto de farmacia
        Glide.with(mContext).load(listaSucursales.get(position).getFotoFarmacia()).into(holder.imagenFarmacia);
    }

    @Override
    public int getItemCount() {
        return listaSucursales.size();
    }

    public void setOnClickListener(View.OnClickListener listener){ this.listener=listener; }
    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
    }

    public class ViewHolderFarmacia extends RecyclerView.ViewHolder {
        TextView nombreDeFarmacia, direccionFarmacia, nombreDeSucursal, telefonoFarmacia;
        ImageView imagenFarmacia;
        public ViewHolderFarmacia(View itemView) {
            super(itemView);
            nombreDeFarmacia = itemView.findViewById(R.id.ItemNombredeFarmacia);
            nombreDeSucursal = itemView.findViewById(R.id.ItemSucursal);
            direccionFarmacia = itemView.findViewById(R.id.ItemDireccionFarmacia);
            telefonoFarmacia = itemView.findViewById(R.id.ItemTelefonoSucursal);
            imagenFarmacia = itemView.findViewById(R.id.ItemImagenFarmacia);
        }
    }
}
