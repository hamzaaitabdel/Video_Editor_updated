package com.example.videoeditor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoeditor.Entities.Draft;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.DraftActivity;

import java.util.List;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {
    private Context context;
    private List<Draft> list;
    private OnItemClickListener onItemClickListener;



    public void swapData(List<Draft> list){
        this.list= list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView,delete;
        TextView date;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.draft_pic);
            date=itemView.findViewById(R.id.draft_date);
            delete=itemView.findViewById(R.id.draft_delete);
            itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(getAdapterPosition());
            });
            delete.setOnClickListener(view -> {
                onItemClickListener.onDelete(getAdapterPosition());
            });
        }

        }

    public DraftAdapter(Context c, List<Draft> list, OnItemClickListener onItemClickListener){
        this.list=list;
        this.context=c;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public DraftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.draft_row,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date=list.get(position).getDate();
        String img = list.get(position).getPics()[0];
        Glide.with(context)
                .load(img)
                .into(holder.imgView);

        holder.date.setText(date);
    }
    public interface OnItemClickListener{
        void onItemClick(int i);
        void onDelete(int i);
    }
    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }
}
