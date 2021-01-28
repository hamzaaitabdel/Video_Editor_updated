package com.example.videoeditor.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class FontsAdapter extends RecyclerView.Adapter<FontsAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Typeface> fontsPicker;
    private FontsAdapter.OnFontsPickerClickListener OnFontsPickerClickListener;
    String []labels=new String[]{
            "Bambi Bold","Champagne & Limousines","Hand Writer","Love and heart","Duality-Regular","FairfaxStation"
            ,"Honey Script SemiBold","Romance Fatal Serif Std","Lobster1.4","Missed Your Exit","LaurenScript Regular","Mom´sTypewriter"
    };
    public FontsAdapter(@NonNull Context context, @NonNull List<Typeface> fontsPicker) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.fontsPicker = fontsPicker;
    }



    @Override
    public FontsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_fonts, parent, false);
        return new FontsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FontsAdapter.ViewHolder holder, int position) {
        holder.font.setTypeface(fontsPicker.get(position));
        holder.font.setText(labels[position]);
    }

    @Override
    public int getItemCount() {
        return fontsPicker.size();
    }


    public void setOnFontsPickerClickListener(FontsAdapter.OnFontsPickerClickListener OnFontsPickerClickListener) {
        this.OnFontsPickerClickListener = OnFontsPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView font;

        public ViewHolder(View itemView) {
            super(itemView);
            font = itemView.findViewById(R.id.fontsTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnFontsPickerClickListener != null)
                        OnFontsPickerClickListener.OnFontsPickerClickListener(fontsPicker.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnFontsPickerClickListener {
        void OnFontsPickerClickListener(Typeface typeface);
    }


}
