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
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;

import java.util.List;

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>{
    private OnItemClickListener itemClickListener;
    private List<Image> selected;
    private Context context;

    public SelectedImagesAdapter(List<Image> selected, Context context,OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.selected = selected;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_imgs_rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(selected.get(position).getUri())
                .apply(new RequestOptions().centerCrop())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return (selected==null)?0:selected.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img,remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.remove_selected_rv_item);
            img = itemView.findViewById(R.id.img_selected_rv_item);
            remove.setOnClickListener(new View.OnClickListener() {
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
    public void swapData(List<Image> selected){
        this.selected=selected;
        notifyDataSetChanged();
    }
}
