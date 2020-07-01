package id.yongki.layananqta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.yongki.layananqta.Model.UsersModel;
import id.yongki.layananqta.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<UsersModel> albumList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nama,nohp,alamat, email;
        public MyViewHolder(View v) {
            super(v);

            nama = (TextView)v.findViewById(R.id.rv_tvNama);
            nohp = (TextView)v.findViewById(R.id.rv_tvnohp);
            alamat = (TextView)v.findViewById(R.id.rv_tv_alamat);
            email = (TextView)v.findViewById(R.id.rv_tv_Email);
        }
    }
    public RecyclerAdapter(Context mContext, List<UsersModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UsersModel album = albumList.get(position);
        holder.nama.setText(album.name);
        holder.nohp.setText(album.noHp);
        holder.alamat.setText(album.alamat);
        holder.email.setText(album.email);
    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
