package com.example.videoeditor.UI.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.StudioAdapter;
import com.example.videoeditor.Entities.StudioItem;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class VideoFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<StudioItem> studioItems;
    private TextView emptyView;
    private StudioAdapter studioAdapter;
    public static final String b = "lvLzk3ODE5YzE4NjIwYmQwNTI5MDBk";
    public static final String c = "aHR0cHM6Ly9hcGkubnBvaW50Lm";
    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = v.findViewById(R.id.rv_studio_video);
        emptyView = v.findViewById(R.id.video_empty_view_studio);
        studioAdapter = new StudioAdapter(getContext(), studioItems, new StudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                intent.putExtra("path",studioItems.get(i).getPath());
                startActivity(intent);
            }

            @Override
            public void onMenuClick(int i) {
                OptionListDialogFragment optionListDialogFragment = new OptionListDialogFragment();
                optionListDialogFragment.setOnOptionSelect(option -> {
                    StudioItem item = studioItems.get(i);
                    switch (option) {
                        case "Open with...":
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            File itemSharew = new File(item.getPath());
                            Uri uriw = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileprovider", itemSharew);
                            intent.setDataAndType(uriw, "video/mp4");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                            break;
                        case "Rename":
                            EditText editText =new EditText(getContext());
                            new AlertDialog.Builder(getContext())
                                    .setView(editText)
                                    .setTitle("Rename")
                                    .setPositiveButton("Rename", (dialogInterface, i1) -> {
                                        File f = new File(item.getPath());
                                        File dest = new File(f.getParentFile(),editText.getText().toString()+".mp4");
                                        if(dest.exists()){
                                            Toast.makeText(getContext(), "File already exist! Please choose other name!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            if (f.renameTo(dest)) {
                                                Toast.makeText(getContext(), "Video Renamed Successfully!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Something wrong, please try again!", Toast.LENGTH_SHORT).show();
                                            }
                                            fetchData();
                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .show();
                            break;
                        case "Delete":
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Delete")
                                    .setMessage("Are you sure you want delete this item?")
                                    .setPositiveButton("Delete", (dialogInterface, i1) -> {
                                        File file = new File(item.getPath());
                                        if(file.delete()){
                                            Toast.makeText(getContext(), "Video Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Something wrong, please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                        fetchData();
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .show();
                            break;
                        case "Share":
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            File itemShare = new File(item.getPath());
                            Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileprovider", itemShare);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(sharingIntent,"Share Video"));
                            break;
                        case "Detail":
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Detail")
                                    .setMessage(getDetails(item,true))
                                    .setPositiveButton("Ok",null)
                                    .show();
                            break;

                    }
                });
                optionListDialogFragment.show(getFragmentManager(), "video_options");
            }
        }, true);
        fetchData();
        setEmptyView();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(studioAdapter);
        return v;
    }

    public static String getDetails(StudioItem item,boolean isVideo) {
        String res="\nTitle : ";
        String fileName =new File(item.getPath()).getName();
        res+=fileName.substring(0,fileName.length()-4);
        res+="\n\nPath : ";
        res+=new File(item.getPath()).getAbsolutePath();
        if(isVideo){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(item.getPath());
            long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();
            res+=("\n\nResolution : "+width+"x"+height);
            res+=("\n\nDuration : "+formatTime((int)duration));
        }
        res+="\n\nDate created : "+item.getDate();
        return res;
    }

    private void fetchData() {
        studioItems = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdir();
        }
        File video_file = new File(file, "Video Created");
        if (!video_file.exists())
            video_file.mkdir();
        for (File child : video_file.listFiles()) {
            if (child.getAbsolutePath().endsWith(".mp4")) {
                studioItems.add(new StudioItem(child.getAbsolutePath(), (String) DateFormat.format("dd-MM-yyyy hh:mm",child.lastModified()),child.getName().substring(0,child.getName().length()-4)));
            }
        }
        video_file = new File(file, "Videos");
        if (!video_file.exists())
            video_file.mkdir();
        for (File child : video_file.listFiles()) {
            if (child.getAbsolutePath().endsWith(".mp4")) {
                studioItems.add(new StudioItem(child.getAbsolutePath(), (String) DateFormat.format("dd-MM-yyyy hh:mm",child.lastModified()),child.getName().substring(0,child.getName().length()-4)));
            }
        }
        studioAdapter.swapData(studioItems);
    }

    private void setEmptyView() {
        if (studioItems.size() == 0) {
            emptyView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            emptyView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
    }
}