package com.example.videoeditor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoeditor.Entities.Video;
import com.example.videoeditor.R;

import java.util.List;

import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class MergeSelectedVideoAdapter extends RecyclerView.Adapter<MergeSelectedVideoAdapter.ViewHolder> {
    private MergeSelectedVideoAdapter.OnItemClickListener itemClickListener;
    private List<Video> videos;
    private Context context;
    public MergeSelectedVideoAdapter(Context context, List<Video> videos, MergeSelectedVideoAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.videos = videos;
        this.context = context;
    }

    public MergeSelectedVideoAdapter(Context applicationContext, List<Video> videos, DraftAdapter.OnItemClickListener onItemClickListener) {
    }

    @NonNull
    @Override
    public MergeSelectedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MergeSelectedVideoAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_rv_item, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MergeSelectedVideoAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load(videos.get(position).getUri())
                .thumbnail(.1f)
                .into(holder.thumbVideo);
    }



    @Override
    public int getItemCount() {
        return (videos == null) ? 0 : videos.size();
    }

    public void swapData(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,duree;
        ImageView thumbVideo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_rv_item_title);
            duree = itemView.findViewById(R.id.video_rv_item_duree);
            duree.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            thumbVideo = itemView.findViewById(R.id.video_thumb_rv);

            itemView.setOnClickListener(view -> itemClickListener.onItemClick(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int i);
    }
}

