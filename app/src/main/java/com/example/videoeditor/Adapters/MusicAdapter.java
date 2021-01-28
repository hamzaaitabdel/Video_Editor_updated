package com.example.videoeditor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.MediaUtils;

import java.io.IOException;
import java.util.List;

import static com.example.videoeditor.Utils.MediaUtils.formatTime;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private Context context;
    private List<Music> list;
    private OnItemClickListener onItemClickListener;
    private boolean isLib;
    public MusicAdapter(Context context, List<Music> list, OnItemClickListener onItemClickListener,boolean isLib) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.isLib = isLib;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.music_rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = list.get(position).getName();
        if(name.endsWith(".mp3")){
            name = name.substring(0,name.length()-4);
        }
        holder.name.setText(name);
        holder.duration.setText(formatTime(list.get(position).getDuration()));
        if (list.get(position).getPath() == null) {
            holder.duration.setText("");
            holder.download.setVisibility(View.VISIBLE);
        }
        if(isLib){

            holder.download.setOnClickListener(view -> {
                holder.download.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                MediaUtils.downloadMusic(context,list.get(position).getRes(),list.get(position).getName(),(path)->{

                        ((Activity) context).runOnUiThread(() -> {
                            holder.progressBar.setVisibility(View.GONE);
                            if(path==null){
                                Toast.makeText(context,"Please make sure you have internet connection!",Toast.LENGTH_SHORT).show();
                                holder.download.setVisibility(View.VISIBLE);
                            }else {
                            int dur = Integer.parseInt(getDur(path));
                            holder.duration.setText(formatTime(dur));
                            onItemClickListener.onMusicSave(position, path, dur);
                        }
                        });
                });
            });
        }
    }
    private String getDur(String path){
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context.getApplicationContext(),uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return durationStr;
    }
    @Override
    public int getItemCount() {
        return (list==null)?0:list.size();
    }

    public void swapData(List<Music> musics) {
        list = musics;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,duration;
        ProgressBar progressBar;
        ImageView download;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.music_name_rv);
            duration=itemView.findViewById(R.id.music_duration_rv);
            progressBar=itemView.findViewById(R.id.progressbar);
            download=itemView.findViewById(R.id.download);
            itemView.setOnClickListener(view -> onItemClickListener.onItemClick(getAdapterPosition()));

        }

    }

    public interface OnItemClickListener{
        void onItemClick(int i);
        void onMusicSave(int i,String path,int dur);
    }
}
