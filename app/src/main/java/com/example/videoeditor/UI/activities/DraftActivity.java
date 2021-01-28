package com.example.videoeditor.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.DraftAdapter;
import com.example.videoeditor.Database.DraftViewModel;
import com.example.videoeditor.Entities.Draft;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.videoeditor.UI.activities.MainActivity.getAdSize;
import static com.example.videoeditor.Utils.AdUtils.loadBanner;

public class DraftActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DraftAdapter draftAdapter;
    private List<Draft> drafts;
    private FrameLayout emptyView;
    private DraftViewModel viewModel;
    private void initAds() {
        //ads
        FrameLayout adContainer = findViewById(R.id.adView_draft);
        loadBanner(this,adContainer);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        recyclerView=findViewById(R.id.rv_draft);
        emptyView = findViewById(R.id.draft_empty_view);
        initAds();
        drafts = new ArrayList<>();
        viewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DraftViewModel.class);
        viewModel.getDrafts().observe(this, drafts -> {
            this.drafts = drafts;
            draftAdapter.swapData(drafts);
            setEmptyView();
        });
        try{
            draftAdapter = new DraftAdapter(getApplicationContext(), drafts, new DraftAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int i) {
                    Intent intent = new Intent(DraftActivity.this,EditorActivity.class);
                    intent.putExtra("draft_id",drafts.get(i).getId());
                    startActivity(intent);
                }

                @Override
                public void onDelete(int i) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DraftActivity.this)
                            .setMessage("Are you sure you want to delete this draft?")
                            .setTitle("Delete")
                            .setPositiveButton("Delete", (dialogInterface, i1) -> {
                                viewModel.delete(drafts.get(i));
                                draftAdapter.notifyItemRemoved(i);
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i12) -> {
                               dialogInterface.dismiss();
                            });
                    builder.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        setEmptyView();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(draftAdapter);
    }
    private void setEmptyView() {
        if(drafts.size()==0){
            emptyView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        }else{
            emptyView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
    }

    public void finishActivity(View view) {
        onBackPressed();
    }
}