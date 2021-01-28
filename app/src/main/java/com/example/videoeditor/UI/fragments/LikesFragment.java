package com.example.videoeditor.UI.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.videoeditor.Adapters.FollowersAdapter;
import com.example.videoeditor.Adapters.LikesAdapter;
import com.example.videoeditor.Entities.Followers;
import com.example.videoeditor.Entities.Likes;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.HashTagActivity;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class LikesFragment extends Fragment {
    private LikesAdapter adapter;
    Handler handler = new Handler();

    public ArrayList<Likes> likes = new ArrayList<>();
    private RecyclerView rcvLikes;
    int status = 0;


    int pos=0;
    private AdUtils.Inters mInterstitialAd;

    public LikesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        initView(view);
    }
    private void initView(View view) {
        this.rcvLikes = (RecyclerView) view.findViewById(R.id.rcvLikes);
        this.likes = generateLikes();
        this.adapter = new LikesAdapter(getContext(), this.likes);
        this.rcvLikes.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.rcvLikes.setAdapter(this.adapter);




        adapter.setOnItemClickListener(new LikesAdapter.OnItemClickListener() {
            public final void onItemClicked(int i) {
                pos=i;
                    mInterstitialAd.show();

                startActi(i);
            }
        });
    }
    public  void startActi(int i) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView((int) R.layout.progressbar_dialog);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_horizontal);
        final TextView textView = (TextView) dialog.findViewById(R.id.value123);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imgLovey);
        final TextView textView2 = (TextView) dialog.findViewById(R.id.txtNumberLikes);
        this.status = 0;
        final int i2 = i;
        final Dialog dialog2 = dialog;
        new Thread(new Runnable() {
            public void run() {
                while (LikesFragment.this.status < 100) {
                    LikesFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setProgress(LikesFragment.this.status);
                            textView.setText(String.valueOf(LikesFragment.this.status));
                            imageView.setImageResource(((Likes) LikesFragment.this.likes.get(i2)).getImgLikes());
                            textView2.setText(((Likes) LikesFragment.this.likes.get(i2)).getTxtLikes());
                        }
                    });
                    LikesFragment.this.status += 2;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                LikesFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(LikesFragment.this.getActivity(), HashTagActivity.class);
                        intent.putExtra("followers", ((Likes) LikesFragment.this.likes.get(i2)).getTxtLikes());
                        intent.putExtra("hashtagfollowers", ((Likes) LikesFragment.this.likes.get(i2)).getTxtLikesHashtags());
                        dialog2.dismiss();
                        LikesFragment.this.startActivity(intent);
                    }
                });
            }
        }).start();
        dialog.show();
        dialog.getWindow().setLayout(-1, -2);
    }

    private void initAds() {
        //interstitial

        mInterstitialAd = new AdUtils.Inters(getContext(),false);
    }

    private ArrayList<Likes> generateLikes() {
        ArrayList<Likes> arrayList = new ArrayList<>();
        arrayList.add(new Likes(R.drawable.hearto, "10 Likes", "#10likes #f4f #10likesme #TagsForLikes #TFLers #TagsLikesApp #10likesforlikes #likes4likes #teamlikesback #10likeher #likebackteam #likehim #likesall #10likealways #likeback #me #love #pleaselike #10likes #likesr #10likeing"));
        arrayList.add(new Likes(R.drawable.hearto, "50 Likes", "#50likes #f4f #50likesme #TagsForLikes #TFLers #TagsLikesApp #50likesforlikes #likes4likes #teamlikesback #50likeher #likebackteam #likehim #likesall #50likealways #likeback #me #love #pleaselike #50likes #likes #50likesing"));
        arrayList.add(new Likes(R.drawable.hearto, "100 Likes", "#100likes #f4f #100likesme #TagsForLikes #TFLers #TagsLikesApp #100likesforlikes #likes4likes #teamlikesback #100likeher #likebackteam #likehim #likesall #100likealways #likeback #me #love #pleaselike #100likes #likes #100likesing"));
        arrayList.add(new Likes(R.drawable.hearto, "200 Likes", "#200likes #f4f #200likesme #TagsForLikes #TFLers #TagsLikesApp #200likesforlikes #likes4likes #teamlikesback #200likeher #likebackteam #likehim #likesall #200likealways #likeback #me #love #pleaselike #200likes #likes #200likesing"));
        arrayList.add(new Likes(R.drawable.hearto, "500 Likes", "#500likes #f4f #500likesme #TagsForLikes #TFLers #TagsLikesApp #500likesforlikes #likes4likes #teamlikesback #500likeher #likebackteam #likehim #likesall #500likealways #likeback #me #love #pleaselike #500likes #likes #500likesing"));
        arrayList.add(new Likes(R.drawable.hearto, "1000 Likes", "#1000likes #f4f #1000likesme #TagsForLikes #TFLers #TagsLikesApp #1000likesforlikes #likes4likes #teamlikesback #1000likeher #likebackteam #likehim #likesall #1000likealways #likeback #me #love #pleaselike #1000likes #likeser #1000likesing"));
        arrayList.add(new Likes(R.drawable.hearto, "5000 Likes", "#5Klikes #f4f #5Klikesme #TagsForLikes #TFLers #TagsLikesApp #5Klikesforlikes #likes4likes #teamlikesback #5Klikeher #likebackteam #followhim #likeall #5Klikealways #likeback #me #love #pleaselike #5Klikes #likes #5Klikesing"));
        arrayList.add(new Likes(R.drawable.hearto, "10K Likes", "#10Klikes #f4f #10Klikesme #TagsForLikes #TFLers #TagsLikesApp #10Klikesforlikes #likes4likes #teamlikesback #10likeher #likebackteam #followhim #likewall #10Klikealways #likeback #me #love #pleaselike #10Klikes #likes #10Klikesing"));
        return arrayList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initAds();
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }
}