package com.example.videoeditor.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.R;
import com.example.videoeditor.VideoApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MediaUtils {

    public static void getImagesCursor(Context context,Executor executor, OnCursorReady onCursorReady){
        executor.execute(()-> {
            final String[] projection;
                projection = new String[]{
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                };

            final String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder);
            onCursorReady.onCursorReady(cursor);
        });
    }
    public static void getMusicCursor(Context context,Executor executor, OnMusicReady onMusicReady){
        executor.execute(()-> {
            List<Music> musicList = new ArrayList<>();
            final String[] projection;
                projection = new String[]{
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                };

            final String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder);
            while (cursor.moveToNext()){
                    musicList.add(new Music(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    ));
            }
            onMusicReady.onMusicReady(musicList);
        });
    }
    public static void getVideosCursor(Context context,Executor executor, OnCursorReady onCursorReady){
        executor.execute(()-> {
            final String[] projection;
                projection = new String[]{
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                };

            final String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder);
            onCursorReady.onCursorReady(cursor);
        });
    }

    public interface OnCursorReady{
        void onCursorReady(Cursor cursor);
    }
public interface OnMusicReady{
        void onMusicReady(List<Music> musicList);
    }

    public static String formatTime(int time) {
        String res = "";
        int seconds = (time / 1000) % 60;
        int minutes = ((time - seconds) / 1000) / 60;
        if (minutes < 10) {
            res += ("0" + minutes + ":");
        } else {
            res += (minutes + ":");
        }
        if (seconds < 10) {
            res += ("0" + seconds);
        } else {
            res += seconds;
        }
        return res;
    }
    public static void saveResMusic(Application application, int res,String name,OnMusicSave onMusicSave){
        ((VideoApplication)application).executorService.execute(()->{
            InputStream stream = null;

            File direct = new File(Environment.getExternalStorageDirectory(),application.getString(R.string.app_name));

            if(!direct.exists()) {
                direct.mkdir();

            }
            File musicDir = new File(direct,"Music");
            if(!musicDir.exists()){
                musicDir.mkdir();
            }
            stream =  application.getResources().openRawResource(res);

            String outFileName = new File(musicDir,name+".mp3").getAbsolutePath();
            OutputStream myOutput = null;
            try {
                myOutput = new FileOutputStream(outFileName);
            }
            catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }


            // transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;

            try {
                while ((length = stream.read(buffer)) > 0) {
                    try {
                        myOutput.write(buffer, 0, length);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            // Close the streams
            try {
                myOutput.flush();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                myOutput.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                stream.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            onMusicSave.onMusicSave(outFileName);
        });
    }
    public interface OnMusicSave{
        void onMusicSave(String path);
    }

    public static void downloadMusic(Context context,String link,String name,OnMusicSave onMusicSave){
        ((VideoApplication)((Activity)context).getApplication()).executorService.execute(()->{
            String path=null;
            try {
                String mus_path =new File(Environment.getExternalStorageDirectory(),context.getString(R.string.app_name)).getAbsolutePath()
                        +"/Music";
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                int length = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                path = new File(mus_path,name+".mp3").getAbsolutePath();
                OutputStream output = new FileOutputStream(new File(path));
                byte data[] = new byte[1024];
                int count;
                long total =0;

                while((count=input.read(data))!=-1){
                total+=count;
                output.write(data,0,count);
                }

                output.flush();
                output.close();
                input.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            onMusicSave.onMusicSave(path);
        });
    }
}
