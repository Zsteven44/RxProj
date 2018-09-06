package com.zsteven44.android.myrxjavaproject.repository.utils;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsteven44.android.myrxjavaproject.model.ImgurImage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ImgurConverter {

    @TypeConverter
    public static String fromImagesToString(@NonNull final List<ImgurImage> images){
        Gson gson = new Gson();
        String json = gson.toJson(images);
        Timber.d("converting List object to json: \n%s",json);
        return json;
    }

    @TypeConverter
    public static ArrayList<ImgurImage> fromStringToImages(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ImgurImage>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

}
