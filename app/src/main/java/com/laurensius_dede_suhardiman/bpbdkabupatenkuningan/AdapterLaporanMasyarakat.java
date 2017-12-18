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

public class AdapterLaporanMasyarakat extends RecyclerView.Adapter<AdapterLaporanMasyarakat.HolderLaporanMasyarakat> {
    List<LaporanMasyarakat>laporanMasyarakat;
    AdapterLaporanMasyarakat(List<LaporanMasyarakat>laporanMasyarakat){
        this.laporanMasyarakat =laporanMasyarakat;
    }

    @Override
    public HolderLaporanMasyarakat onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_laporan_masyarakat,viewGroup,false);
        HolderLaporanMasyarakat holderLaporanMasyarakat = new HolderLaporanMasyarakat(v);
        return holderLaporanMasyarakat;
    }

    @Override
    public void onBindViewHolder(HolderLaporanMasyarakat holderLaporanMasyarakat,int i){
        holderLaporanMasyarakat.ivLaporanMasyarakat.setImageResource(laporanMasyarakat.get(i).icon);
        holderLaporanMasyarakat.tvId.setText(laporanMasyarakat.get(i).id);
        holderLaporanMasyarakat.tvJudul.setText(laporanMasyarakat.get(i).judul);
        holderLaporanMasyarakat.tvTanggalKirim.setText(laporanMasyarakat.get(i).tanggal_kirim);
        holderLaporanMasyarakat.tvStatus.setText(laporanMasyarakat.get(i).status);
    }

    @Override
    public int getItemCount(){
        return laporanMasyarakat.size();
    }

    public LaporanMasyarakat getItem(int position){
        return laporanMasyarakat.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderLaporanMasyarakat extends  RecyclerView.ViewHolder{
        CardView cvLaporanMasyarakat;
        ImageView ivLaporanMasyarakat;
        TextView tvId;
        TextView tvJudul;
        TextView tvTanggalKirim;
        TextView tvStatus;

        HolderLaporanMasyarakat(View itemView){
            super(itemView);
            cvLaporanMasyarakat = (CardView) itemView.findViewById(R.id.cvLaporanMasyarakat);
            ivLaporanMasyarakat = (ImageView)itemView.findViewById(R.id.ivLaporanMasyarakat);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvJudul = (TextView)itemView.findViewById(R.id.tvJudul);
            tvTanggalKirim = (TextView)itemView.findViewById(R.id.tvTanggalKirim);
            tvStatus = (TextView)itemView.findViewById(R.id.tvStatus);
        }

    }
}
