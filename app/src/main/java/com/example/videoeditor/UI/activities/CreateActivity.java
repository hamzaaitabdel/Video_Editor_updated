package com.example.videoeditor.UI.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.AlbumsSpinnerAdapter;
import com.example.videoeditor.Adapters.ImagesRVAdapter;
import com.example.videoeditor.Adapters.SelectedImagesAdapter;
import com.example.videoeditor.Entities.Album;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.PhotoEditFragment;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.Utils.Provider;
import com.example.videoeditor.VideoApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private MotionLayout motionLayout;
    private ImageView expandBt,spinnerDrawable;
    private boolean isExpanded = false;
    private RecyclerView selectedRv, mainRv, albumsRv;
    private ImagesRVAdapter imagesRVAdapter;
    private SelectedImagesAdapter selectedImagesAdapter;
    private AlbumsSpinnerAdapter albumsSpinnerAdapter;
    private List<Image> images,selected,selectedAlbum;
    private List<Album> albums;
    private TextView showCountTv,spinner;
    private Button nextBt;
    private LinearLayout spinnerContainer;
    private String dir=null;
    int limit=-1,edit_lim;
    private boolean forResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
        Intent intentt =getIntent();
        if(intentt!=null){
            dir=intentt.getStringExtra("dir");
            if(forResult=intentt.getBooleanExtra("for_res",false)){
                edit_lim = intentt.getIntExtra("remaining",-1);
            }
        }
        if(forResult){
            limit =edit_lim;
        }else {
            limit = (dir == null) ? 80 : 1;
        }
        requestPermission(this);
        images = new ArrayList<>();
        selectedAlbum = new ArrayList<>();
        albums = new ArrayList<>();
        selected = new ArrayList<>();
        updateCount();
        imagesRVAdapter = new ImagesRVAdapter(this, images, i -> {

            if(selected.size()<limit){
            selected.add(selectedAlbum.get(i));
            selectedImagesAdapter.swapData(selected);
            imagesRVAdapter.swapSelectedAt(selected,i);
            updateCount();
            }
            else {
                Toast.makeText(CreateActivity.this, "You can't select more than "+limit+" images !", Toast.LENGTH_SHORT).show();
            }
        },selected);
        selectedImagesAdapter = new SelectedImagesAdapter(selected, this, i1 -> {
            selected.remove(i1);
            selectedImagesAdapter.swapData(selected);
            imagesRVAdapter.swapSelected(selected);
            updateCount();
        });
        albumsSpinnerAdapter = new AlbumsSpinnerAdapter(albums, this, i1 -> {
            if(!albums.get(i1).getName().equals(albumsSpinnerAdapter.getAlbum())){
            filterImage(albums.get(i1).getName());
            imagesRVAdapter.swapData(selectedAlbum);
            spinner.setText(albums.get(i1).getName());
            albumsSpinnerAdapter.setAlbum(albums.get(i1).getName());
            }
            albumsRv.setVisibility(View.GONE);
            spinnerDrawable.setImageResource(R.drawable.ic_arrow_down);
        });
        fetchData();
        fixRvSize();
        setupRvs();

        expandBt.setOnClickListener(view -> {
            if (isExpanded) {
                motionLayout.transitionToStart();
            } else {
                motionLayout.transitionToEnd();
            }
        });

        motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                if (motionLayout.getProgress() < .15) {
                    isExpanded = false;
                    expandBt.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    isExpanded = true;
                    expandBt.setImageResource(R.drawable.ic_arrow_down);
                }
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
            }
        });

        nextBt.setOnClickListener(view -> {
            if(dir!=null){
                Intent intent = new Intent(CreateActivity.this,PhotoEditorActivity.class);
                intent.putExtra("imgs", selected.get(0).getUri());
                startActivity(intent);
                finish();
            }
            else {
                if (forResult) {
                    if (selected.size() < 1) {
                        Toast.makeText(this, "Please choose at least 1 images", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent data = new Intent();
                        data.putParcelableArrayListExtra("imgs", (ArrayList<Image>) selected);
                        setResult(PhotoEditFragment.ADD_PHOTO_CODE,data);
                        finish();
                    }
                } else {
                    if (selected.size() < 3) {
                        Toast.makeText(this, "Please choose at least 3 images", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(CreateActivity.this, EditorActivity.class);
                        intent.putParcelableArrayListExtra("imgs", (ArrayList<Image>) selected);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        spinnerContainer.setOnClickListener(view -> {
            if(albumsRv.getVisibility()==View.GONE){
                albumsRv.setVisibility(View.VISIBLE);
                spinnerDrawable.setImageResource(R.drawable.ic_arrow_up);
            }
            else{
                spinnerDrawable.setImageResource(R.drawable.ic_arrow_down);
                albumsRv.setVisibility(View.GONE);
            }
        });
    }
    private void filterImage(String name) {
        selectedAlbum.clear();
        if(name.equals("All Albums")){
            selectedAlbum=new ArrayList<>(images);
        }else {
            for (int i=0;i<images.size();i++) {
                if (images.get(i).getAlbum().equals(name)) {
                    selectedAlbum.add(images.get(i));
                }
            }
        }
    }

    private void fixRvSize() {
        FrameLayout layout = findViewById(R.id.main_container);
        int height = getResources().getDisplayMetrics().heightPixels-(int)getResources().getDimension(R.dimen.expand_view);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                height));
    }

    private void updateCount(){
        showCountTv.setText("Selected "+selected.size()+"/"+limit+" image(s)");
    }
    private void setupRvs() {
        mainRv.setHasFixedSize(true);
        albumsRv.setHasFixedSize(true);
        selectedRv.setHasFixedSize(true);
        mainRv.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false));
        selectedRv.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false));
        albumsRv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        albumsRv.setAdapter(albumsSpinnerAdapter);
        selectedRv.setAdapter(selectedImagesAdapter);
        mainRv.setAdapter(imagesRVAdapter);
    }

    private void fetchData() {
        MediaUtils.getImagesCursor(this,((VideoApplication)getApplication()).executorService,cursor->{
                boolean is_first = true;
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    if(is_first){
                        albums.add(new Album("All Albums",images.size(),path));
                    }
                    if (!albumContains(album)) {
                        albums.add(new Album(album,1,path));
                    }else{
                        increaseAlbumCount(album);
                    }
                    images.add(new Image(album, path,0));

                    is_first=false;
            }
            selectedAlbum=new ArrayList<>(images);
            albums.get(0).setCount(images.size());
            runOnUiThread(()->{
                imagesRVAdapter.swapData(selectedAlbum);
            });
        });

    }

    private boolean albumContains(String album) {
        for (int i=0;i<albums.size();i++) {
            if(albums.get(i).getName().equals(album)) {
                return true;
            }
        }
        return false;
    }

    private void increaseAlbumCount(String album) {
        for (int i=0;i<albums.size();i++) {
            if(albums.get(i).getName().equals(album) && !albums.get(i).getName().equals("All Albums")){
                albums.get(i).increaseCount();
                break;
            }
        }
    }

    private void initViews() {
        motionLayout = findViewById(R.id.motion_layout);
        expandBt = findViewById(R.id.expand_bt);
        selectedRv = findViewById(R.id.create_selected_rv);
        mainRv = findViewById(R.id.create_rv);
        nextBt = findViewById(R.id.next_bt);
        showCountTv = findViewById(R.id.show_select_count);
        spinner = findViewById(R.id.create_spinner);
        albumsRv = findViewById(R.id.albums_rv);
        spinnerDrawable = findViewById(R.id.spinner_drawable);
        spinnerContainer = findViewById(R.id.spinner_container);
    }

    public void finishActivity(View view) {
        finish();
    }

    public static void requestPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 121);
        }
    }
}