package com.example.videoeditor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoeditor.Entities.Draft;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.Entities.StudioItem;
import com.example.videoeditor.R;

import java.util.List;

public class StudioAdapter extends RecyclerView.Adapter<StudioAdapter.ViewHolder> {
    private Context context;
    private List<StudioItem> list;
    private OnItemClickListener onItemClickListener;
    private boolean isVideo;

    public void swapData(List<StudioItem> studioItems) {
        list = studioItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView date;
        ImageView more;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.stud_pic);
            date=itemView.findViewById(R.id.stud_date);
            more=itemView.findViewById(R.id.stud_more);
            itemView.setOnClickListener(view -> {
                 onItemClickListener.onItemClick(getAdapterPosition());
            });
            more.setOnClickListener(view -> onItemClickListener.onMenuClick(getAdapterPosition()));
        }

    }

    public StudioAdapter(Context c, List<StudioItem> list, StudioAdapter.OnItemClickListener onItemClickListener,boolean isVideo){
        this.list=list;
        this.context=c;
        this.onItemClickListener = onItemClickListener;
        this.isVideo = isVideo;
    }
    @NonNull
    @Override
    public StudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studio_row,parent,false);
        return new StudioAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StudioAdapter.ViewHolder holder, int position) {
        String img = list.get(position).getPath();
        if(isVideo){
            Glide.with(context)
                    .load(img)
                    .thumbnail(.1f)
                    .into(holder.imgView);
        }else{
            Glide.with(context)
                    .load(img)
                    .into(holder.imgView);
        }
        holder.date.setText(list.get(position).getName());
    }
    public interface OnItemClickListener{
        void onItemClick(int i);
        void onMenuClick(int i);
    }
    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }
}
