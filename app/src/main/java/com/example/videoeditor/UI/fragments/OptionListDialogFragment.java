package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Entities.Option;
import com.example.videoeditor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class OptionListDialogFragment extends BottomSheetDialogFragment {

    private List<Option> optionList;
    private OnOptionSelect onOptionSelect;

    public static OptionListDialogFragment newInstance(Context context) {
        final OptionListDialogFragment fragment = new OptionListDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        optionList = new ArrayList<>();
        optionList.add(new Option("Open with...",R.drawable.ic_exter));
        optionList.add(new Option("Rename",R.drawable.ic_edit));
        optionList.add(new Option("Delete",R.drawable.ic_delete_outline));
        optionList.add(new Option("Share",R.drawable.ic_share));
        optionList.add(new Option("Detail",R.drawable.ic_info));
        return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new OptionAdapter());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        ImageView imageView;
        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.icon_dialog);
            itemView.setOnClickListener(view -> {onOptionSelect.onOptionSelect(optionList.get(getAdapterPosition()).getName());
            dismiss();
            });
        }
    }

    private class OptionAdapter extends RecyclerView.Adapter<ViewHolder> {


        OptionAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(optionList.get(position).getName());
            holder.imageView.setImageResource(optionList.get(position).getIconRes());
        }

        @Override
        public int getItemCount() {
            return optionList==null?0:optionList.size();
        }

    }
    public interface OnOptionSelect{
        void onOptionSelect(String option);
    }

    public void setOnOptionSelect(OnOptionSelect onOptionSelect) {
        this.onOptionSelect = onOptionSelect;
    }
}