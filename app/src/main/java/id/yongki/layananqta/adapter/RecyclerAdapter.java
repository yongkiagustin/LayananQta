package id.yongki.layananqta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.yongki.layananqta.model.UsersModel;
import id.yongki.layananqta.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private OnItemListener mOnItemListener;
    private List<UsersModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nama, kota, alamat, profesi, lamaKerja,no;
        CardView cardView;
        OnItemListener onItemListener;

        public MyViewHolder(View v, OnItemListener onItemListener) {
            super(v);

            nama = (TextView) v.findViewById(R.id.rv_labelnama);
            kota = (TextView) v.findViewById(R.id.rv_labelkota);
            alamat = (TextView) v.findViewById(R.id.rv_labelalamat);
            profesi = (TextView) v.findViewById(R.id.rv_labelprofesi);
            lamaKerja = (TextView) v.findViewById(R.id.rv_labellamakerja);
            cardView = (CardView) v.findViewById(R.id.rc_cardview);
            no = (TextView)v.findViewById(R.id.rc_label_no);
            this.onItemListener = onItemListener;

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public RecyclerAdapter(Context mContext, List<UsersModel> albumList, OnItemListener onItemListener) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.mOnItemListener = onItemListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_layout, parent, false);

        return new MyViewHolder(itemView, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UsersModel album = albumList.get(position);
        holder.nama.setText(album.nama);
        holder.kota.setText(album.kota);
        holder.alamat.setText(album.alamat);
        holder.profesi.setText(album.profesi);
        holder.lamaKerja.setText(album.lamaKerja.concat(" Tahun"));
        holder.no.setText(String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
