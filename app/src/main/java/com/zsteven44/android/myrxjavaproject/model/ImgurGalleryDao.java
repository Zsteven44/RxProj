package com.zsteven44.android.myrxjavaproject.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ImgurGalleryDao {

    @Query("SELECT * FROM ImgurGallery")
    List<ImgurGallery> getAll();

    @Query("SELECT * FROM ImgurGallery WHERE item_id IN (:itemIds)")
    List<ImgurGallery> getAllByIds(int[] itemIds);

    @Query("SELECT * FROM ImgurGallery where title LIKE (:title) LIMIT 1")
    ImgurGallery getByTitle(String title);

    @Insert
    void insertAll(ImgurGallery... galleries);

    @Delete
    void delete(ImgurGallery gallery);

}
