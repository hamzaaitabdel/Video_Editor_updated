package com.example.videoeditor.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.videoeditor.R;

import java.io.File;

public class SaveImageDialog extends Dialog {
    private Context context;
    private OnDialogSave onDialogSave;
    private String path;
    public SaveImageDialog(@NonNull Context context,String path,OnDialogSave onDialogSave) {
        super(context);
        this.context = context;
        this.onDialogSave = onDialogSave;
        this.path =path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_image_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView cancel = findViewById(R.id.cancel_dialog_bt);
        Button save = findViewById(R.id.save_with_frame);
        Button savenow = findViewById(R.id.save_now);
        ImageView imageView = findViewById(R.id.image_preview);
        Glide.with(context)
                .load(new File(path))
                .into(imageView);
        cancel.setOnClickListener(view -> {
         dismiss();
        });

        save.setOnClickListener(view -> {
            onDialogSave.onImageSaveWithFrame();
            dismiss();
        });

        savenow.setOnClickListener(view -> {
            onDialogSave.onImageSave();
            dismiss();
        });
    }

    public interface OnDialogSave{
        void onImageSave();
        void onImageSaveWithFrame();
    }
}
