package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Laurensius D.S on 12/14/2017.
 */

public class AdapterPeringatanDini extends RecyclerView.Adapter<AdapterPeringatanDini.HolderPeringatanDini> {
    List<PeringatanDini> peringatanDini;
    AdapterPeringatanDini(List<PeringatanDini> peringatanDini){
        this.peringatanDini = peringatanDini;
    }

    @Override
    public HolderPeringatanDini onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_peringatan_dini,viewGroup,false);
        HolderPeringatanDini holderPeringatanDini = new HolderPeringatanDini(v);
        return holderPeringatanDini;
    }

    @Override
    public void onBindViewHolder(HolderPeringatanDini holderPeringatanDini,int i){
        holderPeringatanDini.tvId.setText(peringatanDini.get(i).id);
        holderPeringatanDini.tvJudul.setText(peringatanDini.get(i).judul);
        holderPeringatanDini.tvTanggalBuat.setText(peringatanDini.get(i).tanggal_publish);
    }

    @Override
    public int getItemCount(){
        return peringatanDini.size();
    }

    public PeringatanDini getItem(int position){
        return peringatanDini.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderPeringatanDini extends  RecyclerView.ViewHolder{
        CardView cvPeringatanDini;
        ImageView ivPeringatanDini;
        TextView tvId;
        TextView tvJudul;
        TextView tvTanggalBuat;

        HolderPeringatanDini(View itemView){
            super(itemView);
            cvPeringatanDini = (CardView) itemView.findViewById(R.id.cvPeringatanDini);
            ivPeringatanDini = (ImageView)itemView.findViewById(R.id.ivPeringatanDini);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvJudul = (TextView)itemView.findViewById(R.id.tvJudul);
            tvTanggalBuat = (TextView)itemView.findViewById(R.id.tvTanggalBuat);
        }

    }
}
