package com.example.videoeditor.UI.fragments.stickers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.MusicLibFragment;

import java.util.ArrayList;

public class StickersFragment extends Fragment {
    private static final String ARG_PARAM ="sticker_type";
    private int sticker_src;
    int []list;
    public StickersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sticker_src = getArguments().getInt(ARG_PARAM);
            switch (sticker_src){
                case 0:list=new int[]{
                        R.drawable.sticker_1_1,
                        R.drawable.sticker_1_2,
                        R.drawable.sticker_1_3,
                        R.drawable.sticker_1_4,
                        R.drawable.sticker_1_5,
                        R.drawable.sticker_1_6,
                        R.drawable.sticker_1_7,
                        R.drawable.sticker_1_8,
                        R.drawable.sticker_1_9,
                        R.drawable.sticker_1_10,
                        R.drawable.sticker_1_11,
                        R.drawable.sticker_1_12,
                        R.drawable.sticker_1_13,
                        R.drawable.sticker_1_14,
                        R.drawable.sticker_1_15,
                        R.drawable.sticker_1_16,
                        R.drawable.sticker_1_17,
                        R.drawable.sticker_1_18,
                        R.drawable.sticker_1_19,
                        R.drawable.sticker_1_20,
                        R.drawable.sticker_1_21,
                        R.drawable.sticker_1_22,
                        R.drawable.sticker_1_23,
                        R.drawable.sticker_1_24,
                        R.drawable.sticker_1_25,
                        R.drawable.sticker_1_26,

                };

                    break;
                case 1:list=new int[]{
                        R.drawable.sticker_2_1,
                        R.drawable.sticker_2_2,
                        R.drawable.sticker_2_3,
                        R.drawable.sticker_2_4,
                        R.drawable.sticker_2_5,
                        R.drawable.sticker_2_6,
                        R.drawable.sticker_2_7,
                };
                    break;
                case 2:list=new int[]{
                        R.drawable.sticker_4_1,
                        R.drawable.sticker_4_2,
                        R.drawable.sticker_4_3,
                        R.drawable.sticker_4_4,
                        R.drawable.sticker_4_5,
                        R.drawable.sticker_4_6,
                        R.drawable.sticker_4_7,
                        R.drawable.sticker_4_8,
                        R.drawable.sticker_4_9,
                        R.drawable.sticker_4_10,
                        R.drawable.sticker_4_11,
                        R.drawable.sticker_4_12,
                        R.drawable.sticker_4_13,
                        R.drawable.sticker_4_14,
                        R.drawable.sticker_4_15,
                        R.drawable.sticker_4_16,
                        R.drawable.sticker_4_17,
                        R.drawable.sticker_4_18,
                        R.drawable.sticker_4_19,
                        R.drawable.sticker_4_20,
                        R.drawable.sticker_4_21,
                        R.drawable.sticker_4_22,
                        R.drawable.sticker_4_23,
                };
                break;
                case 3:
                    list = new int[]{
                            R.drawable.sticker_5_1,
                            R.drawable.sticker_5_2,
                            R.drawable.sticker_5_3,
                            R.drawable.sticker_5_4,
                            R.drawable.sticker_5_5,
                            R.drawable.sticker_5_6,
                            R.drawable.sticker_5_7,
                            R.drawable.sticker_5_8,
                            R.drawable.sticker_5_9,
                            R.drawable.sticker_5_10,
                            R.drawable.sticker_5_11,
                            R.drawable.sticker_5_12,
                            R.drawable.sticker_5_13,
                            R.drawable.sticker_5_14,
                            R.drawable.sticker_5_15,
                            R.drawable.sticker_5_16,
                            R.drawable.sticker_5_17,
                            R.drawable.sticker_5_18,
                            R.drawable.sticker_5_19,
                            R.drawable.sticker_5_20,
                            R.drawable.sticker_5_21,
                            R.drawable.sticker_5_22,
                            R.drawable.sticker_5_23,
                            R.drawable.sticker_5_24,
                            R.drawable.sticker_5_25,
                            R.drawable.sticker_5_26,
                            R.drawable.sticker_5_27,
                            R.drawable.sticker_5_28,
                            R.drawable.sticker_5_29
                    };break;
                case 4:
                    list=new int[]{
                            R.drawable.sticker_6_1,
                            R.drawable.sticker_6_2,
                            R.drawable.sticker_6_3,
                            R.drawable.sticker_6_4,
                            R.drawable.sticker_6_5,
                            R.drawable.sticker_6_6,
                            R.drawable.sticker_6_7,
                            R.drawable.sticker_6_8,
                            R.drawable.sticker_6_9,
                            R.drawable.sticker_6_10,
                            R.drawable.sticker_6_11,
                            R.drawable.sticker_6_12,
                            R.drawable.sticker_6_13,
                            R.drawable.sticker_6_14,
                            R.drawable.sticker_6_15,
                            R.drawable.sticker_6_16,
                            R.drawable.sticker_6_17,
                            R.drawable.sticker_6_18,
                            R.drawable.sticker_6_19,
                            R.drawable.sticker_6_20,
                            R.drawable.sticker_6_21,
                            R.drawable.sticker_6_22,
                            R.drawable.sticker_6_23,
                            R.drawable.sticker_6_24,
                            R.drawable.sticker_6_25,
                            R.drawable.sticker_6_26,
                            R.drawable.sticker_6_27,
                            R.drawable.sticker_6_28,
                            R.drawable.sticker_6_29
                    };break;
                case 5:
                    list=new int[]{
                            R.drawable.sticker_7_1,
                            R.drawable.sticker_7_2,
                            R.drawable.sticker_7_3,
                            R.drawable.sticker_7_4,
                            R.drawable.sticker_7_5,
                            R.drawable.sticker_7_6,
                            R.drawable.sticker_7_7,
                            R.drawable.sticker_7_8,
                            R.drawable.sticker_7_9,
                            R.drawable.sticker_7_10,
                            R.drawable.sticker_7_11,
                            R.drawable.sticker_7_12,
                            R.drawable.sticker_7_13,
                            R.drawable.sticker_7_14,
                            R.drawable.sticker_7_15,
                            R.drawable.sticker_7_16,
                            R.drawable.sticker_7_17,
                            R.drawable.sticker_7_18,
                            R.drawable.sticker_7_19,
                            R.drawable.sticker_7_20,
                            R.drawable.sticker_7_21,
                            R.drawable.sticker_7_22,
                            R.drawable.sticker_7_23,
                            R.drawable.sticker_7_24,
                            R.drawable.sticker_7_25,
                            R.drawable.sticker_7_26,
                            R.drawable.sticker_7_27,
                            R.drawable.sticker_7_28,
                            R.drawable.sticker_7_29
                    };break;
                case 6:
                    list=new int[]{
                            R.drawable.sticker_8_1,
                            R.drawable.sticker_8_2,
                            R.drawable.sticker_8_3,
                            R.drawable.sticker_8_4,
                            R.drawable.sticker_8_5,
                            R.drawable.sticker_8_6,
                            R.drawable.sticker_8_7,
                            R.drawable.sticker_8_8,
                            R.drawable.sticker_8_9,
                            R.drawable.sticker_8_10,
                            R.drawable.sticker_8_11,
                            R.drawable.sticker_8_12,
                            R.drawable.sticker_8_13,
                            R.drawable.sticker_8_14,
                            R.drawable.sticker_8_15,
                            R.drawable.sticker_8_16,
                            R.drawable.sticker_8_17,
                            R.drawable.sticker_8_18,
                            R.drawable.sticker_8_19,
                            R.drawable.sticker_8_20,
                            R.drawable.sticker_8_21,
                            R.drawable.sticker_8_22,
                            R.drawable.sticker_8_23,
                            R.drawable.sticker_8_24,
                            R.drawable.sticker_8_25,
                            R.drawable.sticker_8_26,
                            R.drawable.sticker_8_27,
                            R.drawable.sticker_8_28,
                            R.drawable.sticker_8_29
                    };break;
                case 7:
                    list=new int[]{
                            R.drawable.sticker_9_1,
                            R.drawable.sticker_9_2,
                            R.drawable.sticker_9_3,
                            R.drawable.sticker_9_4,
                            R.drawable.sticker_9_5,
                            R.drawable.sticker_9_6,
                            R.drawable.sticker_9_7,
                            R.drawable.sticker_9_8,
                            R.drawable.sticker_9_9,
                            R.drawable.sticker_9_10,
                            R.drawable.sticker_9_11,
                            R.drawable.sticker_9_12,
                            R.drawable.sticker_9_13,
                            R.drawable.sticker_9_14,
                            R.drawable.sticker_9_15,
                            R.drawable.sticker_9_16,
                            R.drawable.sticker_9_17,
                            R.drawable.sticker_9_18,
                            R.drawable.sticker_9_19,
                            R.drawable.sticker_9_20,
                            R.drawable.sticker_9_21,
                            R.drawable.sticker_9_22,
                            R.drawable.sticker_9_23,
                            R.drawable.sticker_9_24,
                            R.drawable.sticker_9_25,
                            R.drawable.sticker_9_26,
                            R.drawable.sticker_9_27,
                            R.drawable.sticker_9_28,
                            R.drawable.sticker_9_29
                    };break;
                case 8:
                    list=new int[]{
                            R.drawable.sticker_10_1,
                            R.drawable.sticker_10_2,
                            R.drawable.sticker_10_3,
                            R.drawable.sticker_10_4,
                            R.drawable.sticker_10_5,
                            R.drawable.sticker_10_6,
                            R.drawable.sticker_10_7,
                            R.drawable.sticker_10_8,
                            R.drawable.sticker_10_9,
                            R.drawable.sticker_10_10,
                            R.drawable.sticker_10_11,
                            R.drawable.sticker_10_12,
                            R.drawable.sticker_10_13,
                            R.drawable.sticker_10_14,
                            R.drawable.sticker_10_15,
                            R.drawable.sticker_10_16,
                            R.drawable.sticker_10_17,
                            R.drawable.sticker_10_18,
                            R.drawable.sticker_10_19,
                            R.drawable.sticker_10_20,
                            R.drawable.sticker_10_21,
                            R.drawable.sticker_10_22,
                            R.drawable.sticker_10_23,
                            R.drawable.sticker_10_24,
                            R.drawable.sticker_10_25,
                            R.drawable.sticker_10_26,
                            R.drawable.sticker_10_27,
                            R.drawable.sticker_10_28,
                            R.drawable.sticker_10_29
                    };break;
                case 9:
                    list=new int[]{
                            R.drawable.sticker_12_1,
                            R.drawable.sticker_12_2,
                            R.drawable.sticker_12_3,
                            R.drawable.sticker_12_4,
                            R.drawable.sticker_12_5,
                            R.drawable.sticker_12_6,
                            R.drawable.sticker_12_7,
                            R.drawable.sticker_12_8,
                            R.drawable.sticker_12_9,
                            R.drawable.sticker_12_10,
                            R.drawable.sticker_12_11,
                            R.drawable.sticker_12_12,
                            R.drawable.sticker_12_13,
                            R.drawable.sticker_12_14,
                            R.drawable.sticker_12_15,
                            R.drawable.sticker_12_16,
                            R.drawable.sticker_12_17,
                            R.drawable.sticker_12_18,
                            R.drawable.sticker_12_19,
                            R.drawable.sticker_12_20,
                            R.drawable.sticker_12_21,
                            R.drawable.sticker_12_22,
                            R.drawable.sticker_12_23,
                            R.drawable.sticker_12_24,
                            R.drawable.sticker_12_25,
                    };break;

            }
        }

    }
    public static StickersFragment newInstance(int source) {
        StickersFragment fragment = new StickersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, source);
        fragment.setArguments(args);
        return fragment;
    }
    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(int res);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_bottom_sticker_emoji_dialog, container, false);
        RecyclerView rvEmoji = v.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 6);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);
        return v;
    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

        int[] stickerList = list;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(getContext())
                    .load(getContext().getDrawable(stickerList[position]))
                    .into(holder.imgSticker);

        }

        @Override
        public int getItemCount() {
            return stickerList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStickerListener != null) {
                            mStickerListener.onStickerClick(stickerList[getLayoutPosition()]);
                        }
                    }
                });
            }
        }
    }

    private String convertEmoji(String emoji) {
        String returnedEmoji = "";
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}