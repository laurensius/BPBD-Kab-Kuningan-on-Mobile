package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Laurensius D.S on 12/14/2017.
 */

public class AdapterInfoBencana extends RecyclerView.Adapter<AdapterInfoBencana.HolderInfoBencana> {
    List<InfoBencana>infoBencana;
    AdapterInfoBencana(List<InfoBencana>infoBencana){
        this.infoBencana =infoBencana;
    }

    @Override
    public HolderInfoBencana onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_info_bencana,viewGroup,false);
        HolderInfoBencana holderInfoBencana = new HolderInfoBencana(v);
        return holderInfoBencana;
    }

    @Override
    public void onBindViewHolder(HolderInfoBencana holderInfoBencana,int i){
        holderInfoBencana.ivInfoBencana.setImageResource(infoBencana.get(i).icon);
        holderInfoBencana.tvId.setText(infoBencana.get(i).id);
        holderInfoBencana.tvJudul.setText(infoBencana.get(i).judul);
        holderInfoBencana.tvTanggalBuat.setText(infoBencana.get(i).tanggal_publish);
    }

    @Override
    public int getItemCount(){
        return infoBencana.size();
    }

    public InfoBencana getItem(int position){
        return infoBencana.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderInfoBencana extends  RecyclerView.ViewHolder{
        CardView cvInfoBencana;
        ImageView ivInfoBencana;
        TextView tvId;
        TextView tvJudul;
        TextView tvTanggalBuat;

        HolderInfoBencana(View itemView){
            super(itemView);
            cvInfoBencana = (CardView) itemView.findViewById(R.id.cvInfoBencana);
            ivInfoBencana = (ImageView)itemView.findViewById(R.id.ivInfoBencana);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvJudul = (TextView)itemView.findViewById(R.id.tvJudul);
            tvTanggalBuat = (TextView)itemView.findViewById(R.id.tvTanggalBuat);
        }

    }
}
