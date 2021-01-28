package com.example.videoeditor.UI.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.videoeditor.Adapters.EditorPagerAdapter;
import com.example.videoeditor.Database.DraftViewModel;
import com.example.videoeditor.Entities.Draft;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.QualityDialog;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.EffectFragment;
import com.example.videoeditor.UI.fragments.FrameFragment;
import com.example.videoeditor.UI.fragments.MusicFragment;
import com.example.videoeditor.UI.fragments.PhotoEditFragment;
import com.example.videoeditor.UI.fragments.TimeFragment;
import com.example.videoeditor.UI.fragments.TransitionFragment;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.example.videoeditor.Utils.TransitionUtils;
import com.example.videoeditor.VideoApplication;
import com.example.videoeditor.dialogs.CustomDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.tabs.TabLayout;
import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.PhotoMoviePlayer;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.model.SimplePhotoData;
import com.hw.photomovie.record.GLMovieRecorder;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.GLTextureMovieRender;
import com.hw.photomovie.render.GLTextureView;
import com.hw.photomovie.segment.MovieSegment;
import com.hw.photomovie.timer.IMovieTimer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import static com.example.videoeditor.UI.activities.MainActivity.getAdSize;
import static com.example.videoeditor.UI.activities.MainActivity.mapAdViewToLayout;
import static com.example.videoeditor.UI.fragments.FrameFragment.OnFrameSelect;
import static com.example.videoeditor.UI.fragments.MusicFragment.OnMusicSelect;
import static com.example.videoeditor.UI.fragments.TimeFragment.OnTimeSelect;
import static com.example.videoeditor.UI.fragments.TransitionFragment.OnTransitionSelect;
import static com.example.videoeditor.Utils.AdUtils.loadBanner;
import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class EditorActivity extends AppCompatActivity implements OnFrameSelect, OnTimeSelect, OnTransitionSelect, OnMusicSelect, EffectFragment.OnEffectSelect
        , PhotoEditFragment.OnPhotoUpdate {
    private List<PhotoData> photoDataList;
    private PhotoMoviePlayer photoMoviePlayer;
    private TabLayout tabs;
    private ViewPager pager;
    private ImageView playBt, thumbImg;
    private SeekBar seekBar;
    private TextView timeNow, totalTime, saveBt, progressTv;
    private LinearLayout loadingLayout, mainLayout, savingLayout;
    private EditorPagerAdapter pagerAdapter;
    private List<Image> images;
    private GLTextureView glTextureView;
    private Executor executor;
    private GLSurfaceMovieRenderer movieRender;
    private PhotoMovie mPhotoMovie;
    private boolean isDone = false, fromDraft = false, isSaved = false;
    private int frame = -1, effect = -1, trans, time = 3000, alpha = 70, draft_id = -1;
    private String music;
    private CustomDialog dialog;
    private DraftViewModel viewModel;
    private EffectFragment effectFrag;
    private PhotoEditFragment photoEditFragment;
    AdUtils.Inters mInterstitialAd;
    private void initAds() {

        FrameLayout adContainer = findViewById(R.id.editor_banner);
        //interstitial
        loadBanner(this,adContainer);
        mInterstitialAd = new AdUtils.Inters(this,false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initViews();
        initAds();
        effectFrag = new EffectFragment();
        setDefaultMusic();
        executor = ((VideoApplication) getApplication()).executorService;
        Intent intent = getIntent();
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DraftViewModel.class);
        images = intent.getParcelableArrayListExtra("imgs");
        setupTabs();
        if (images == null) {
            int draftId = intent.getIntExtra("draft_id", -1);
            if (draftId == -1) {
                Toast.makeText(this, "Something wrong! please try again!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                fromDraft = true;
                draft_id = draftId;
                viewModel.getDraft(draftId, draft -> {
                    trans = draft.getTrans();
                    frame = draft.getFrame();
                    music = draft.getMusic();
                    effect = draft.getEffect();
                    time = draft.getDuration();
                    images = new ArrayList<>();
                    for (String imPath : draft.getPics()) {
                        images.add(new Image("album", imPath, 0));
                    }
                    runOnUiThread(() -> {
                        loadData();
                        effectFrag.setSelectedRes(effect);
                    });
                });


            }
        } else {
            loadData();
        }
        glTextureView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().widthPixels * 9 / 16));
        movieRender = new GLTextureMovieRender(glTextureView);
        photoMoviePlayer = new PhotoMoviePlayer(this);
        saveBt.setOnClickListener(view -> {
            saveVideo();
        });

        dialog = new CustomDialog(this, () -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
            String date = formatter.format(Calendar.getInstance().getTime());
            if (fromDraft) {
                Draft draft = new Draft(date, getImages(), trans, frame, effect, music, time);
                draft.setId(draft_id);
                viewModel.update(draft);
            } else {
                viewModel.insert(new Draft(date, getImages(), trans, frame, effect, music, time));
            }
            Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
                mInterstitialAd.show();

            finish();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                photoMoviePlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isDone) {
                    photoMoviePlayer.seekTo(seekBar.getProgress() * mPhotoMovie.getDuration() / 100);
                }
            }
        });

    }

    private void setDefaultMusic() {
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdir();
        }
        File mFile = new File(file, "Music");
        if (!mFile.exists()) {
            mFile.mkdir();
        }
        File musicF = new File(mFile, "Default Music1.mp3");
        if (musicF.exists()) {
            music = musicF.getAbsolutePath();
        }
    }

    private void saveVideo() {
        if (photoMoviePlayer.isPlaying()) {
            photoMoviePlayer.pause();
        }
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdir();
        }
        Glide.with(this)
                .load(images.get(0).getUri())
                .into(thumbImg);
        File videoFile = new File(file, "Video Created");
        if (!videoFile.exists()) {
            videoFile.mkdir();
        }
        initNative();
        GLMovieRecorder recorder = new GLMovieRecorder(this);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy-hh:mm");
        String date = formatter.format(Calendar.getInstance().getTime());
        String savedFilePath = new File(videoFile, "Video" + date + ".mp4").getAbsolutePath();
        new QualityDialog(this, i -> {
            int qw = 1280, qh = 720;
            switch (i) {
                case 0:
                    qh = 1080;
                    qw = 1920;
                    break;
                case 1:
                    qh = 720;
                    qw = 1280;
                    break;
                case 2:
                    qh = 480;
                    qw = 854;
                    break;
            }
            recorder.configOutput(qw, qh, 4000000, 60, 1, savedFilePath);
            recorder.setDataSource(movieRender);
            if (music != null && new File(music).exists()) {
                recorder.setMusic(music);
            }
            savingLayout.setVisibility(View.VISIBLE);
            recorder.startRecord(new GLMovieRecorder.OnRecordListener() {
                @Override
                public void onRecordFinish(boolean success) {
                    if (success) {
                        isSaved = true;
                            mInterstitialAd.show();
                        Toast.makeText(EditorActivity.this, "Video Saved Successfully! ", Toast.LENGTH_SHORT).show();
                        Intent mediaScanIntent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(new File(savedFilePath));
                        mediaScanIntent.setData(contentUri);
                        sendBroadcast(mediaScanIntent);
                        Intent intent = new Intent(EditorActivity.this, ShareActivity.class);
                        intent.putExtra("path", savedFilePath);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditorActivity.this, "Saving failed, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onRecordProgress(int recordedDuration, int totalDuration) {
                    int p = recordedDuration * 100 / totalDuration;
                    progressTv.setText(p + "%");
                }
            });
        }).show();

    }

    private void initNative() {
        //native
        AdLoader adLoader = new AdLoader.Builder(this, Provider.nativeId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        mapAdViewToLayout(unifiedNativeAd, adView);
                        FrameLayout frameLayout = findViewById(R.id.native_saving_container);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadData() {
        createData(photoMovie -> {
            runOnUiThread(() -> {
                movieRender.setPhotoMovie(photoMovie);
                photoMoviePlayer.setMovieRenderer(movieRender);
                photoMoviePlayer.setMovieListener(new IMovieTimer.MovieListener() {
                    @Override
                    public void onMovieUpdate(int elapsedTime) {
                        timeNow.setText(formatTime(elapsedTime));
                        totalTime.setText(formatTime(photoMovie.getDuration()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            seekBar.setProgress(elapsedTime * 100 / photoMovie.getDuration(), true);
                        } else {
                            seekBar.setProgress(elapsedTime * 100 / photoMovie.getDuration());
                        }
                    }

                    @Override
                    public void onMovieStarted() {
                        playBt.setVisibility(View.GONE);
                    }

                    @Override
                    public void onMoviedPaused() {
                        playBt.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onMovieResumed() {
                        playBt.setVisibility(View.GONE);
                    }

                    @Override
                    public void onMovieEnd() {
                        playBt.setVisibility(View.VISIBLE);
                    }
                });
                photoMoviePlayer.setDataSource(photoMovie);
                photoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
                    @Override
                    public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
                    }

                    @Override
                    public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                        photoMoviePlayer.start();
                        loadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(PhotoMoviePlayer moviePlayer) {
                    }
                });
                File f = new File(new File(Environment.getExternalStorageDirectory(), "MusicVideoMaker"), "Music");
                if (!f.exists()) {
                    f.mkdir();
                }
                if (music != null && new File(music).exists()) {
                    photoMoviePlayer.setMusic(music);
                }
                photoMoviePlayer.prepare();

                photoMoviePlayer.setLoop(true);
            });
            mPhotoMovie = photoMovie;
            isDone = true;
        });
    }

    private void createData(OnDataHandlingFinish dataHandlingFinish) {
        executor.execute(() -> {
            photoDataList = new ArrayList<>();
            for (Image im : images) {
                photoDataList.add(newPhotoData(im.getUri(), frame));
            }
            PhotoSource source = new PhotoSource(photoDataList);
            List<MovieSegment> segments = TransitionUtils.getTransitionSegments(trans, time, images.size());
            PhotoMovie photoMoviee = new PhotoMovie(source, segments);
            dataHandlingFinish.onComplete(photoMoviee);
        });
    }

    @Override
    public void onFrameSelect(int frame) {
        if (frame != this.frame && isDone) {
            isDone = false;
            this.frame = frame;
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
        }
    }

    @Override
    public void onTimeSelect(int time) {
        if (time * 1000 != this.time && isDone) {
            isDone = false;
            this.time = time * 1000;
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
        }
    }

    @Override
    public void onTransitionSelect(int i) {
        if (trans != i) {
            trans = i;
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
        }
    }

    @Override
    public void onMusicSelect(Music music) {
        if (this.music != null) {
            if (music.getPath() == null) {
                this.music = music.getPath();
                loadData();
                loadingLayout.setVisibility(View.VISIBLE);
                photoMoviePlayer.destroy();
                playBt.setVisibility(View.GONE);
                photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
            } else {
                if (!this.music.equals(music.getPath())) {
                    this.music = music.getPath();
                    loadData();
                    loadingLayout.setVisibility(View.VISIBLE);
                    photoMoviePlayer.destroy();
                    playBt.setVisibility(View.GONE);
                    photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
                }
            }
        } else if (music.getPath() != null) {
            this.music = music.getPath();
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
        }
    }

    @Override
    public void onEffectSelect(int resId, int alpha) {
        if ((this.alpha != alpha || effect != resId) && isDone) {
            isDone = false;
            effect = resId;
            this.alpha = alpha;
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
        }
    }

    @Override
    public boolean onPhotoDelete(int i) {
        if (isDone) {
            isDone = false;
            images.remove(i);
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onPhotoAdded() {
        Intent addIntent = new Intent(EditorActivity.this, CreateActivity.class);
        addIntent.putExtra("for_res", true);
        addIntent.putExtra("remaining", 80 - images.size());
        startActivityForResult(addIntent, PhotoEditFragment.ADD_PHOTO_CODE);
            mInterstitialAd.show();

    }

    @Override
    public void onPhotoEdit(int i) {
        Intent editIntent = new Intent(EditorActivity.this, PhotoEditorActivity.class);
        editIntent.putExtra("imgs", images.get(i).getUri());
        editIntent.putExtra("index", i);
        editIntent.putExtra("for_res", true);
        startActivityForResult(editIntent, PhotoEditFragment.EDIT_PHOTO_CODE);
            mInterstitialAd.show();

    }

    public interface OnDataHandlingFinish {
        void onComplete(PhotoMovie photoMovie);
    }

    private void setupTabs() {
        pagerAdapter = new EditorPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new TransitionFragment(), "Transition");
        pagerAdapter.addFrag(new TimeFragment(), "Time");
        pagerAdapter.addFrag(new MusicFragment(), "Music");
        pagerAdapter.addFrag(new FrameFragment(), "Frame");
        pagerAdapter.addFrag(effectFrag, "Effect");
        photoEditFragment = PhotoEditFragment.newInstance(images);
        pagerAdapter.addFrag(photoEditFragment, "Edit Photo");
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(5);
        tabs.setupWithViewPager(pager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_video_trans);
        tabs.getTabAt(1).setIcon(R.drawable.ic_time);
        tabs.getTabAt(2).setIcon(R.drawable.ic_music);
        tabs.getTabAt(3).setIcon(R.drawable.ic_frame);
        tabs.getTabAt(4).setIcon(R.drawable.ic_effect);
        tabs.getTabAt(5).setIcon(R.drawable.ic_btn_edit_pt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (int i = 0; i < tabs.getTabCount(); i++) {
                tabs.getTabAt(i).getIcon().setTint(getColor(R.color.white));

            }
            tabs.getTabAt(tabs.getSelectedTabPosition()).getIcon().setTint(getColor(R.color.colorAccent));
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tab.getIcon().setTint(getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tab.getIcon().setTint(getColor(R.color.white));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void initViews() {
        tabs = findViewById(R.id.editor_tabs);
        pager = findViewById(R.id.editor_viewpager);
        playBt = findViewById(R.id.video_play_bt);
        seekBar = findViewById(R.id.video_seekbar);
        timeNow = findViewById(R.id.timing_now_tv);
        totalTime = findViewById(R.id.total_time_tv);
        saveBt = findViewById(R.id.save_bt);
        loadingLayout = findViewById(R.id.applying_theme_layout);
        glTextureView = findViewById(R.id.glTextureView);
        mainLayout = findViewById(R.id.editor_main_layout);
        progressTv = findViewById(R.id.percent_progress);
        savingLayout = findViewById(R.id.saving_layout);
        thumbImg = findViewById(R.id.thumb_img);
    }

    public void finishActivity(View view) {
        onBackPressed();
    }

    private Bitmap fixAspectRatio(String uri, int frame_res) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        int height, width, hRatio = 9, wRatio = 16, bH, bW, top = 0, left = 0;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            height = bitmap.getHeight();
            width = height * wRatio / hRatio;
            bH = height * wRatio / hRatio;
            bW = width;
            top = -(height * wRatio / hRatio - height) / 2;
        } else {
            width = bitmap.getWidth();
            height = width * hRatio / wRatio;
            bW = width * wRatio / hRatio;
            bH = height;
            left = -(width * wRatio / hRatio - width) / 2;
        }
        Bitmap res = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(res);
        canvas.drawBitmap(Bitmap.createScaledBitmap(blur(Bitmap.createScaledBitmap(bitmap
                , bW / 10, bH / 10, false)), bW, bH, false), left, top, null);
        canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2, (height - bitmap.getHeight()) / 2, null);
        if (effect != -1) {
            Bitmap eff = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), effect), width, height, true);
            Paint paint = new Paint();
            paint.setAlpha(alpha);
            canvas.drawBitmap(eff, 0, 0, paint);
        }
        if (frame_res != -1) {
            Bitmap filter = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), frame_res), width, height, true);
            canvas.drawBitmap(filter, 0, 0, null);
        }

        return res;
    }

    private SimplePhotoData newPhotoData(String uri, int frame_res) {
        SimplePhotoData simplePhotoData = new SimplePhotoData(this, "", PhotoData.STATE_BITMAP);
        simplePhotoData.setBitmap(fixAspectRatio(uri, frame_res));
        return simplePhotoData;
    }

    public Bitmap blur(Bitmap image) {
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        RenderScript rs = RenderScript.create(this);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, image);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setRadius(10);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public void pausePlayVideo(View view) {
        if (photoMoviePlayer.isPlaying()) {
            photoMoviePlayer.pause();
            playBt.setVisibility(View.VISIBLE);
        } else {
            photoMoviePlayer.start();
            playBt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (photoMoviePlayer.isPlaying()) {
            photoMoviePlayer.pause();
        }
    }

    @Override
    public void onBackPressed() {
        if (photoMoviePlayer.isPlaying()) {
            photoMoviePlayer.pause();
        }
        if (!isSaved) {
            dialog.show();
        } else {

            finish();
        }
    }

    private String[] getImages() {
        String[] imgs = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            imgs[i] = images.get(i).getUri();
        }
        return imgs;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PhotoEditFragment.EDIT_PHOTO_CODE) {
            int index = data.getIntExtra("index", -1);
            if (index != -1) {
                Image temp = images.get(index);
                images.remove(index);
                images.add(index, new Image(temp.getAlbum(), data.getStringExtra("path"), 0));
                loadData();
                loadingLayout.setVisibility(View.VISIBLE);
                photoMoviePlayer.destroy();
                playBt.setVisibility(View.GONE);
                photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
                photoEditFragment.notifyDataChange();
            }
        } else if (resultCode == PhotoEditFragment.ADD_PHOTO_CODE) {
            List<Image> newImages = data.getParcelableArrayListExtra("imgs");
            for (Image m : newImages) {
                images.add(m);
            }
            loadData();
            loadingLayout.setVisibility(View.VISIBLE);
            photoMoviePlayer.destroy();
            playBt.setVisibility(View.GONE);
            photoMoviePlayer = new PhotoMoviePlayer(EditorActivity.this);
            photoEditFragment.notifyDataChange();
        }
    }

    @Override
    public void onAddClick() {
            mInterstitialAd.show();

    }

}