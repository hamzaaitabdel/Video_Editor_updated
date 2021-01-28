package com.example.videoeditor;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class QualityDialog extends Dialog {

    public QualityDialog(@NonNull Context context,OnItemSelected onItemSelected) {
        super(context);
        setContentView(R.layout.quality_dialog);
        Button item0 = findViewById(R.id.item0);
        Button item1 = findViewById(R.id.item1);
        Button item2 = findViewById(R.id.item2);
        item0.setOnClickListener(view -> {
            dismiss();
            onItemSelected.onItemSelect(0);
        });
        item1.setOnClickListener(view -> {
            onItemSelected.onItemSelect(1);
            dismiss();
        });
        item2.setOnClickListener(view -> {
            dismiss();
            onItemSelected.onItemSelect(2);
        });
    }

    public interface OnItemSelected{
        void onItemSelect(int i);
    }
}
