package com.rrcc.ubifarm01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrcc.ubifarm01.ClasesDeObjetos.Notificaiones;
import com.rrcc.ubifarm01.ClasesDeObjetos.Sucursal;

import java.util.List;

public class adapterItemNotificaciones extends
        RecyclerView.Adapter<adapterItemNotificaciones.ViewHolderNotificaciones>
        implements View.OnClickListener{
    Context mContext;
    List<Notificaiones> listaNotificaiones;

    private View.OnClickListener listener;

    public adapterItemNotificaciones(Context mContext, List<Notificaiones> listaNotificaiones) {
        this.mContext = mContext;
        this.listaNotificaiones = listaNotificaiones;
    }

    @NonNull
    @Override
    public ViewHolderNotificaciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificaciones,parent,false);
        view.setOnClickListener(this);
        return new ViewHolderNotificaciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotificaciones holder, int position) {
        holder.nombreDeFarmacia.setText(listaNotificaiones.get(position).getnombreFarmaciaSugerida());
        holder.telefonoFarmacia.setText(listaNotificaiones.get(position).getTelefonoFarmaciaSugerida());
        holder.correoFarmacia.setText(listaNotificaiones.get(position).getCorreoFarmaciaSugerencia());
        holder.direccionFarmacia.setText(listaNotificaiones.get(position).getDireccionFarmaciaSugerida());
        holder.correoUsuario.setText(listaNotificaiones.get(position).getCorreoUsuario());
    }

    @Override
    public int getItemCount() { return listaNotificaiones.size(); }
    public void setOnClickListener(View.OnClickListener listener){ this.listener=listener; }
    @Override
    public void onClick(View view) { if(listener!=null){ listener.onClick(view); } }

    public class ViewHolderNotificaciones extends RecyclerView.ViewHolder {
        TextView nombreDeFarmacia, telefonoFarmacia, correoFarmacia, direccionFarmacia, correoUsuario;
        public ViewHolderNotificaciones(View itemView) {
            super(itemView);

            nombreDeFarmacia = itemView.findViewById(R.id.sugFarmacia);
            telefonoFarmacia = itemView.findViewById(R.id.sugTelefono);
            correoFarmacia = itemView.findViewById(R.id.sugCorreo);
            direccionFarmacia = itemView.findViewById(R.id.sugDireccion);
            correoUsuario = itemView.findViewById(R.id.sugUsuario);
        }
    }
}
