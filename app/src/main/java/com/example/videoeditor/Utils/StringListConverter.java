package com.example.videoeditor.Utils;

import androidx.room.TypeConverter;

public class StringListConverter {
    @TypeConverter
    public static String[] fromString(String s) {
        return s.split(",mid");
    }

    @TypeConverter
    public static String toString(String[] stringList) {
        String res="";
        for(int i=0;i<stringList.length;i++){
            res+=stringList[i];
            if(i!=(stringList.length-1)){
                res+=",mid";
            }
        }
        return res;
    }
}