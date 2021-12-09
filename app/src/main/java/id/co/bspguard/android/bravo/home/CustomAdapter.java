package id.co.bspguard.android.bravo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private Context context;
    private List<MyData> myData;
    Fungsi fungsi = new Fungsi();
    public CustomAdapter(Context context, List<MyData> myData) {
        this.context = context;
        this.myData = myData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).asBitmap().load(myData.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .override(600,450)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ID :"+myData.get(position).getJudul(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ActivityAwal.class);
                Bundle extras = new Bundle();
                extras.putString("id_paket", myData.get(position).getId());
                extras.putString("image",  myData.get(position).getImage());
                extras.putString("term",  myData.get(position).getTerm());
                extras.putString("judul",  myData.get(position).getJudul());
                extras.putString("harga",  myData.get(position).getHarga());
                extras.putString("deskripsi",  myData.get(position).getDeskripsi());
                extras.putString("include",  myData.get(position).getInclude());
                extras.putString("exclude",  myData.get(position).getExclude());
                extras.putString("harga_family",  myData.get(position).getHarga_family());
                extras.putString("harga_group",  myData.get(position).getHarga_group());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        holder.judul.setText(myData.get(position).getJudul());
        holder.idr.setText(fungsi.ubahharga(myData.get(position).getHarga_group()));

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView judul, idr;


        public ViewHolder(View itemView) {
            super(itemView);
//            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            this.judul = (TextView) itemView.findViewById(R.id.judul);
//            this.idr = (TextView) itemView.findViewById(R.id.idr);
        }
    }

}
