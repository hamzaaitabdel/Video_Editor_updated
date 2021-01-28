package com.example.videoeditor.UI.fragments;

import android.annotation.SuppressLint;
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
import com.example.videoeditor.Entities.Followers;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.HashTagActivity;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {
    private FollowersAdapter adapter;
    public ArrayList<Followers> followers = new ArrayList<>();
    private RecyclerView rcvFollowres;
    private int pos;
    private AdUtils.Inters mInterstitialAd;
    int status = 0;

    public FollowersFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        initView(view);
    }
    private void initView(View view) {
        this.rcvFollowres = (RecyclerView) view.findViewById(R.id.rcvFollowres);
        this.followers = generateFollowers();
        this.adapter = new FollowersAdapter(getContext(), this.followers);
        this.rcvFollowres.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvFollowres.setAdapter(adapter);




        adapter.setOnItemClickListener(new FollowersAdapter.OnItemClickListener() {
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
                while (FollowersFragment.this.status < 100) {
                    FollowersFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setProgress(FollowersFragment.this.status);
                            textView.setText(String.valueOf(FollowersFragment.this.status));
                            imageView.setImageResource(((Followers) FollowersFragment.this.followers.get(i2)).getImgFollowers());
                            textView2.setText(((Followers) FollowersFragment.this.followers.get(i2)).getTxtFollowers());
                        }
                    });
                    FollowersFragment.this.status += 2;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                FollowersFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(FollowersFragment.this.getActivity(), HashTagActivity.class);
                        intent.putExtra("followers", ((Followers) FollowersFragment.this.followers.get(i2)).getTxtFollowers());
                        intent.putExtra("hashtagfollowers", ((Followers) FollowersFragment.this.followers.get(i2)).getTxtFollowersHashtags());
                        Log.d("tungnt", "HashTag : " + ((Followers) FollowersFragment.this.followers.get(i2)).getTxtFollowersHashtags());
                        dialog2.dismiss();
                        FollowersFragment.this.startActivity(intent);
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

    private ArrayList<Followers> generateFollowers() {
        ArrayList<Followers> arrayList = new ArrayList<>();
        arrayList.add(new Followers(R.drawable.okfollowers, "10 Followers", "#10follow #f4f #10followme #TagsForLikes #TFLers #TagsLikesApp #10followforfollow #follow4follow #teamfollowback #10followher #followbackteam #followhim #followall #10followalways #followback #me #love #pleasefollow #10follows #follower #10following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "50 Followers", "#50follow #f4f #50followme #TagsForLikes #TFLers #TagsLikesApp #50followforfollow #follow4follow #teamfollowback #50followher #followbackteam #followhim #followall #50followalways #followback #me #love #pleasefollow #50follows #follower #50following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "100 Followers", "#100follow #f4f #100followme #TagsForLikes #TFLers #TagsLikesApp #100followforfollow #follow4follow #teamfollowback #100followher #followbackteam #followhim #followall #100followalways #followback #me #love #pleasefollow #100follows #follower #100following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "200 Followers", "#200follow #f4f #200followme #TagsForLikes #TFLers #TagsLikesApp #200followforfollow #follow4follow #teamfollowback #200followher #followbackteam #followhim #followall #200followalways #followback #me #love #pleasefollow #200follows #follower #200following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "500 Followers", "#500follow #f4f #500followme #TagsForLikes #TFLers #TagsLikesApp #500followforfollow #follow4follow #teamfollowback #500followher #followbackteam #followhim #followall #500followalways #followback #me #love #pleasefollow #500follows #follower #500following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "1000 Followers", "#1000follow #f4f #1000followme #TagsForLikes #TFLers #TagsLikesApp #1000followforfollow #follow4follow #teamfollowback #1000followher #followbackteam #followhim #followall #1000followalways #followback #me #love #pleasefollow #1000follows #follower #1000following"));
        arrayList.add(new Followers(R.drawable.okfollowers, "5000 Followers", "#5Kfollow #f4f #5Kfollowme #TagsForLikes #TFLers #TagsLikesApp #5Kfollowforfollow #follow4follow #teamfollowback #5Kfollowher #followbackteam #followhim #followall #5Kfollowalways #followback #me #love #pleasefollow #5Kfollows #follower #5Kfollowing"));
        arrayList.add(new Followers(R.drawable.okfollowers, "10K Followers", "#10Kfollow #f4f #10Kfollowme #TagsForLikes #TFLers #TagsLikesApp #10Kfollowforfollow #follow4follow #teamfollowback #10Kfollowher #followbackteam #followhim #followall #10Kfollowalways #followback #me #love #pleasefollow #10Kfollows #follower #10Kfollowing"));
        return arrayList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initAds();
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }
}