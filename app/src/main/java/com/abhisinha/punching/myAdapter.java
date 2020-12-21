package com.abhisinha.punching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder>  {


   LayoutInflater inflater;
   List<Datum> data;

    public myAdapter(Context context, List<Datum> data) {
        this.inflater=LayoutInflater.from(context);
        this.data=data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       // LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sample_layout,parent,false);
        return new myviewholder(view);
    }

    public myAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

       // Datum datum=data[position];
        holder.name.setText(data.get(position).getName());
        holder.time.setText(data.get(position).getTime());
        holder.date.setText(data.get(position).getDate());
        holder.id.setText(data.get(position).getId());
        holder.address.setText(data.get(position).getAddress());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public  class  myviewholder extends RecyclerView.ViewHolder  {


        TextView name,id,date,time,address;

        public myviewholder(@NonNull View itemView) {
            super(itemView);


            name= (TextView) itemView.findViewById(R.id.name_id);
            id= (TextView) itemView.findViewById(R.id.ID_id);
            date= (TextView) itemView.findViewById(R.id.date_id);
            time= (TextView) itemView.findViewById(R.id.time_id);
            address= (TextView) itemView.findViewById(R.id.address_id);


        }


    }

}
