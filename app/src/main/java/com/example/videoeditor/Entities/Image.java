package com.example.videoeditor.Entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String uri;
    private String album;
    private int selectCount=0;

    public Image( String album, String uri,int selectCount) {
        this.uri = uri;
        this.album = album;
        this.selectCount = selectCount;
    }

    protected Image(Parcel in) {
        uri = in.readString();
        album = in.readString();
        selectCount = in.readInt();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public int getSelectCount() {
        return selectCount;
    }

    public String getUri() {
        return uri;
    }

    public String getAlbum() {
        return album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(album);
        parcel.writeInt(selectCount);
    }
}
