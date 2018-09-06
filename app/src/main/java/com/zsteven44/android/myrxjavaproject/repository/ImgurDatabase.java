package com.zsteven44.android.myrxjavaproject.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryDao;
import com.zsteven44.android.myrxjavaproject.repository.utils.ImgurConverter;

@Database(entities = {ImgurGallery.class}, version=1)
@TypeConverters({ImgurConverter.class})
public abstract class ImgurDatabase extends RoomDatabase {
    public abstract ImgurGalleryDao getGalleryDao();

    private static final String DB_NAME = "imgurDatabase.db";
    private static volatile ImgurDatabase instance;

    static synchronized ImgurDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private ImgurDatabase() {};

    private static ImgurDatabase create(final Context context) {
        return Room.databaseBuilder(
                    context,
                    ImgurDatabase.class,
                    DB_NAME)
                .build();
    }

}
