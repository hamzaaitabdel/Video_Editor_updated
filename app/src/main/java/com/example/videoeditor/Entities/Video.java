package com.example.videoeditor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    private String uri,name;
    private int duration;
    public Video(String name, String uri, int duration) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
    }

    protected Video(Parcel in) {
        uri = in.readString();
        name = in.readString();
        duration = in.readInt();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(name);
        parcel.writeInt(duration);
    }
}
