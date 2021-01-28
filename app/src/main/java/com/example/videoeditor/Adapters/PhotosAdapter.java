package com.example.videoeditor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private Context context;
    private List<Image> list;
    private OnItemClickListener onItemClickListener;
    private int selected=0;
    private int prev_selected=-1;

    public PhotosAdapter(Context context, List<Image> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelected(int selected) {
        prev_selected = this.selected;
        this.selected = selected;
        if(prev_selected!=-1){
            notifyItemChanged(prev_selected);
        }
        notifyItemChanged(this.selected);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_image_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.simple_img_view_size), (int) context.getResources().getDimension(R.dimen.simple_img_view_size));
        if (position == selected) {
            int marg = (int) context.getResources().getDimension(R.dimen.selected_margin);
            params.setMargins(marg, marg, marg, marg);

        } else {
            params.setMargins(0, 0, 0, 0);
        }
        holder.imageView.setLayoutParams(params);
        Glide.with(context)
                .load(list.get(position).getUri())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        int d=(list==null)?0:list.size();
        Log.e("test00",d+"");
        return (list==null)?0:list.size();
    }

    public void swapData(List<Image> images) {
        list=images;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
                imageView = itemView.findViewById(R.id.simple_img);
            itemView.setOnClickListener(view -> {onItemClickListener.onItemClick(getAdapterPosition());
                setSelected(getAdapterPosition());
            });
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}
