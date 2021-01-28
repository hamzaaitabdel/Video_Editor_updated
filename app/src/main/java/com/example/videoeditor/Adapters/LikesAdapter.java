package com.example.videoeditor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Entities.Likes;
import com.example.videoeditor.R;

import java.util.ArrayList;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Likes> likes;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int i);
    }

    public LikesAdapter(Context context2, ArrayList<Likes> arrayList) {
        this.inflater = LayoutInflater.from(context2);
        this.context = context2;
        this.likes = arrayList;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate((int) R.layout.item_likes, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Likes likes2 = this.likes.get(i);
        viewHolder.imgLikes.setImageResource(likes2.getImgLikes());
        viewHolder.txtLikes.setText(likes2.getTxtLikes());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LikesAdapter.this.onItemClickListener.onItemClicked(viewHolder.getAdapterPosition());
            }
        });
    }

    public int getItemCount() {
        return this.likes.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cView;

        public ImageView imgLikes;

        public TextView txtLikes;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.cView = (CardView) view.findViewById(R.id.cView);
            this.imgLikes = (ImageView) view.findViewById(R.id.imgLikes);
            this.txtLikes = (TextView) view.findViewById(R.id.txtLikes);
        }
    }
}
