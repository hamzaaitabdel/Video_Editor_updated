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

import com.example.videoeditor.Entities.Followers;
import com.example.videoeditor.R;

import java.util.ArrayList;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Followers> followers;
    private LayoutInflater inflater;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int i);
    }

    public FollowersAdapter(Context context2, ArrayList<Followers> arrayList) {
        this.inflater = LayoutInflater.from(context2);
        this.context = context2;
        this.followers = arrayList;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate((int) R.layout.item_followers, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Followers followers2 = this.followers.get(i);
        viewHolder.imgFollowers.setImageResource(followers2.getImgFollowers());
        viewHolder.txtFollowers.setText(followers2.getTxtFollowers());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FollowersAdapter.this.onItemClickListener.onItemClicked(viewHolder.getAdapterPosition());
            }
        });
    }

    public int getItemCount() {
        return this.followers.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cView;

        public ImageView imgFollowers;

        public TextView txtFollowers;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.cView = (CardView) view.findViewById(R.id.cView);
            this.imgFollowers = (ImageView) view.findViewById(R.id.imgFollowers);
            this.txtFollowers = (TextView) view.findViewById(R.id.txtFollowers);
        }
    }
}
