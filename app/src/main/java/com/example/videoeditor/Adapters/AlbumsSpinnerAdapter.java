package com.example.videoeditor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.videoeditor.Entities.Album;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;

import java.util.List;

public class AlbumsSpinnerAdapter extends RecyclerView.Adapter<AlbumsSpinnerAdapter.ViewHolder>{
    private OnItemClickListener itemClickListener;
    private List<Album> albums;
    private Context context;
    private String album = "All Albums";

    public AlbumsSpinnerAdapter(List<Album> albums, Context context,OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.albums = albums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(albums.get(position).getThumbnail())
                .apply(new RequestOptions().centerCrop())
                .into(holder.img);
        holder.count.setText(albums.get(position).getCount()+"");
        holder.name.setText(albums.get(position).getName());

    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public int getItemCount() {
        return (albums==null)?0:albums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView count,name;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.album_count_rv);
            name = itemView.findViewById(R.id.album_name_rv);
            img = itemView.findViewById(R.id.album_img_rv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}
