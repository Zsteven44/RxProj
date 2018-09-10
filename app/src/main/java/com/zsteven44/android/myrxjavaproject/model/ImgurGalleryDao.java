package com.zsteven44.android.myrxjavaproject.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ImgurGalleryDao {

    @Query("SELECT * FROM ImgurGallery")
    MutableLiveData<List<ImgurGallery>> getAll();

    @Query("SELECT * FROM ImgurGallery WHERE id IN (:itemIds)")
    MutableLiveData<List<ImgurGallery>> getAllByIds(int[] itemIds);

    @Query("SELECT * FROM ImgurGallery WHERE title LIKE (:title) LIMIT 1")
    MutableLiveData<ImgurGallery> getByTitle(String title);

    @Insert(onConflict = REPLACE)
    void insertAll(List<ImgurGallery> galleries);

    @Delete
    void delete(ImgurGallery gallery);

    @Query("DELETE FROM ImgurGallery")
    void deleteAll();

}
