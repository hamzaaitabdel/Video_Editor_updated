package com.example.videoeditor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;

import java.io.File;
import java.util.List;

public class ImagesRVAdapter extends RecyclerView.Adapter<ImagesRVAdapter.ViewHolder> {
    private OnItemClickListener itemClickListener;
    private List<Image> images;
    private List<Image> selected;
    private Context context;

    public ImagesRVAdapter(Context context, List<Image> images, OnItemClickListener itemClickListener, List<Image> selected) {
        this.itemClickListener = itemClickListener;
        this.images = images;
        this.context = context;
        this.selected = selected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.images_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context)
                    .load(new File(images.get(position).getUri()))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.img);
            if (selected.contains(images.get(position))) {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(countSelected(images.get(position)));
            } else {
                holder.count.setVisibility(View.GONE);
            }
    }

    private String countSelected(Image image) {
        int res = 0;
        for (Image im : selected) {
            if (im == image) {
                res++;
            }
        }
        return res + "";
    }

    @Override
    public int getItemCount() {
        return (images == null) ? 0 : images.size();
    }

    public void swapData(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void swapSelected(List<Image> selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void swapSelectedAt(List<Image> selected, int index) {
        this.selected = selected;
        notifyItemChanged(index);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView count;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.imgs_rv_item_count);
            img = itemView.findViewById(R.id.imgs_rv_item_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int i);
    }
}
