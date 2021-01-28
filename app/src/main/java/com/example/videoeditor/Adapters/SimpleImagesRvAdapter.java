package com.example.videoeditor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.R;

import java.util.List;

public class SimpleImagesRvAdapter extends RecyclerView.Adapter<SimpleImagesRvAdapter.ViewHolder> {
    private Context context;
    private List<Integer> list;
    private OnItemClickListener onItemClickListener;
    private boolean isFrame;
    private int selected=0;
    private int prev_selected=-1;

    public SimpleImagesRvAdapter(Context context, List<Integer> list, OnItemClickListener onItemClickListener, boolean isFrame) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.isFrame = isFrame;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isFrame){
            view= LayoutInflater.from(context).inflate(R.layout.frame_image_row,parent,false);
        }else{
           view = LayoutInflater.from(context).inflate(R.layout.simple_image_row,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!isFrame) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.simple_img_view_size), (int) context.getResources().getDimension(R.dimen.simple_img_view_size));
            if (position == selected) {
                int marg = (int) context.getResources().getDimension(R.dimen.selected_margin);
                params.setMargins(marg, marg, marg, marg);

            } else {
                params.setMargins(0, 0, 0, 0);
            }
            holder.imageView.setLayoutParams(params);
        }
        holder.imageView.setImageResource(list.get(position));
    }

    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }

    public void setSelected(int selected) {
        prev_selected = this.selected;
        this.selected = selected;
        if(prev_selected!=-1){
            notifyItemChanged(prev_selected);
        }
        notifyItemChanged(this.selected);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            if(isFrame) {
                imageView = itemView.findViewById(R.id.frame_img);
            }else{
                imageView = itemView.findViewById(R.id.simple_img);
            }
            itemView.setOnClickListener(view -> {onItemClickListener.onItemClick(getAdapterPosition());
            setSelected(getAdapterPosition());
            });
        }

    }


    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}

