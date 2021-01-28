package com.example.videoeditor.UI.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.videoeditor.R;
import com.example.videoeditor.Adapters.StickersPagerAdapter;
import com.example.videoeditor.UI.fragments.stickers.StickersFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StickerBSFragment extends BottomSheetDialogFragment implements StickersFragment.StickerListener{

    public StickerBSFragment() {
        // Required empty public constructor
    }

    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    @Override
    public void onStickerClick(int res) {
        dismiss();
        mStickerListener.onStickerClick(BitmapFactory.decodeResource(getResources(),res));
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabs = view.findViewById(R.id.sticker_tab);
        ViewPager viewPager = view.findViewById(R.id.stickers_pager);
        StickersPagerAdapter adapter = new StickersPagerAdapter(getChildFragmentManager());
        //ArrayList<Drawable> listDrawable=getStickers();
        StickersFragment fragment1 = StickersFragment.newInstance(0);
        fragment1.setStickerListener(this);
        adapter.addFragment(fragment1);
        StickersFragment fragment2 = StickersFragment.newInstance(1);
        fragment2.setStickerListener(this);
        adapter.addFragment(fragment2);
        StickersFragment fragment3 = StickersFragment.newInstance(2);
        fragment3.setStickerListener(this);
        adapter.addFragment(fragment3);
        StickersFragment fragment4 = StickersFragment.newInstance(3);fragment4.setStickerListener(this);adapter.addFragment(fragment4);
        StickersFragment fragment5 = StickersFragment.newInstance(4);fragment5.setStickerListener(this);adapter.addFragment(fragment5);
        StickersFragment fragment6 = StickersFragment.newInstance(5);fragment6.setStickerListener(this);adapter.addFragment(fragment6);
        StickersFragment fragment7 = StickersFragment.newInstance(6);fragment7.setStickerListener(this);adapter.addFragment(fragment7);
        StickersFragment fragment8 = StickersFragment.newInstance(7);fragment8.setStickerListener(this);adapter.addFragment(fragment8);
        StickersFragment fragment9 = StickersFragment.newInstance(8);fragment9.setStickerListener(this);adapter.addFragment(fragment9);
        StickersFragment fragment10 = StickersFragment.newInstance(9);fragment10.setStickerListener(this);adapter.addFragment(fragment10);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.sticker_1_1);
        tabs.getTabAt(1).setIcon(R.drawable.sticker_2_1);
        tabs.getTabAt(2).setIcon(R.drawable.sticker_4_1);
        tabs.getTabAt(3).setIcon(R.drawable.sticker_5_1);
        tabs.getTabAt(4).setIcon(R.drawable.sticker_6_1);
        tabs.getTabAt(5).setIcon(R.drawable.sticker_7_1);
        tabs.getTabAt(6).setIcon(R.drawable.sticker_8_1);
        tabs.getTabAt(7).setIcon(R.drawable.sticker_9_1);
        tabs.getTabAt(8).setIcon(R.drawable.sticker_10_1);
        tabs.getTabAt(9).setIcon(R.drawable.sticker_12_1);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_stickers, container, false);
//        LinearLayout layout = v.findViewById(R.id.main);
//        final BottomSheetBehavior behavior = BottomSheetBehavior.from(layout);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//            }

//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });
        return v;
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
    private ArrayList<Integer> getStickerstz() {
//
//        final Field[] fields =  R.drawable.class.getDeclaredFields();
//        final R.drawable drawableResources = new R.drawable();
//        ArrayList<Integer> lipsList = new ArrayList<>();
//        int resId;
//        for (int i = 0; i < fields.length; i++) {
//            try {
//                if (fields[i].getName().contains("type")){
//                    resId = fields[i].getInt(drawableResources);
//                    lipsList.add(resId);
//                }
//            } catch (Exception e) {
//                continue;
//            }
//        }
//        return lipsList;
        return null;
    }
    public Drawable getDrawable(String name){
        Resources resources = getContext().getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                getContext().getPackageName());
        return resources.getDrawable(resourceId);
    }
    private ArrayList<Drawable> getStickers() {

        ArrayList<Integer> lipsList = new ArrayList<>();
        ArrayList<Drawable> list = new ArrayList<>();
        int resId;
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 30; j++) {
                try{
                    Drawable d= getDrawable("sticker_"+i+"_"+j);

                    list.add(d);
                    Log.e("drawable"+i+"_"+j,d.toString());
                }catch (Exception e){
                    continue;
                }

            }
        }
        return list;
    }
    private Drawable getSticker() {

        ArrayList<Integer> lipsList = new ArrayList<>();
        ArrayList<Drawable> list = new ArrayList<>();
        int resId;
        Drawable d = null;
                try{
                    d= getDrawable("sticker_1_1");
                    list.add(d);
                    Log.e("deawable",d.toString());
                }catch (Exception e){
                    Log.e("nari",d.toString());
                }
                return d;
    }
}