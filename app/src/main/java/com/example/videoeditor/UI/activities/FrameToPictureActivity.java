package com.example.videoeditor.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.EmojiBSFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;

public class FrameToPictureActivity extends AppCompatActivity {
    private Bitmap image,withFrame;
    private ImageView previewImg,save;
    int[] frameList=new int[]{R.drawable.ic_magazine_1
            , R.drawable.ic_magazine_2
            , R.drawable.ic_magazine_3
            , R.drawable.ic_magazine_4
            , R.drawable.ic_magazine_5
            , R.drawable.ic_magazine_6
            , R.drawable.ic_magazine_7
            , R.drawable.ic_magazine_8
            , R.drawable.ic_magazine_9
            , R.drawable.ic_magazine_10
            , R.drawable.ic_magazine_11
            , R.drawable.ic_magazine_12
            , R.drawable.ic_magazine_13
            , R.drawable.ic_magazine_14
            , R.drawable.ic_magazine_15
            , R.drawable.ic_magazine_16
            , R.drawable.ic_magazine_17
            , R.drawable.ic_magazine_18
            , R.drawable.ic_magazine_19
            , R.drawable.ic_magazine_20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_to_picture);
        Intent intent= getIntent();
        previewImg = findViewById(R.id.pictureFrame);
        save = findViewById(R.id.save_frame);
        String path = intent.getStringExtra("img_path");
        Glide.with(this)
                .load(new File(path))
                .into(previewImg);
        image = BitmapFactory.decodeFile(path);
        FrameAdapter adapter =new FrameAdapter();
        RecyclerView recyclerView = findViewById(R.id.framePicRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,6));
        recyclerView.setAdapter(adapter);
        save.setOnClickListener(view -> {
            saveImage();
        });
    }

    public Bitmap addFrame(Bitmap bitmap,int drawable){
        Bitmap res = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        Bitmap frame = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),drawable),res.getWidth(),res.getHeight(),true);
        Canvas canvas = new Canvas(res);
        canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawBitmap(frame,0,0,null);
        return res;
    }

    public void finishActivity(View view) {
        onBackPressed();
    }
    private void saveImage() {
            File dir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (!dir.exists()) {
                dir.mkdir();
            }
            File subDir = new File(dir, "Photo Editor");
            if (!subDir.exists()) {
                subDir.mkdir();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy-hh:mm a");
            String date = formatter.format(Calendar.getInstance().getTime());
            File file = new File(subDir, "IE" + date + ".png");
            try {
                FileOutputStream out = new FileOutputStream(file);
                if(withFrame!=null) {
                    withFrame.compress(Bitmap.CompressFormat.PNG, 100, out);
                }else{
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                }
                out.flush();
                out.close();
                Toast.makeText(this, "Image Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(FrameToPictureActivity.this,ShareImageActivity.class);
                intent.putExtra("img_path",file.getAbsolutePath());
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("exception", e.getMessage());
            }
        }

    public class FrameAdapter extends RecyclerView.Adapter<FrameToPictureActivity.FrameAdapter.ViewHolder> {

        @Override
        public FrameToPictureActivity.FrameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_frame, parent, false);
            return new FrameToPictureActivity.FrameAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FrameToPictureActivity.FrameAdapter.ViewHolder holder, int position) {
            holder.frame.setImageResource(frameList[position]);
        }

        @Override
        public int getItemCount() {
            return frameList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView frame;

            ViewHolder(View itemView) {
                super(itemView);
                frame = itemView.findViewById(R.id.frame);

                itemView.setOnClickListener(v -> {
                    withFrame = addFrame(image,frameList[getAdapterPosition()]);
                    previewImg.setImageBitmap(withFrame);
                });
            }
        }
    }
}
