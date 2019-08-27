package com.abedafnan.popularmovies2.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM fav_movies")
    LiveData<List<MovieEntity>> loadAllFavs();

    @Insert
    void insertFav(MovieEntity taskEntry);

    @Delete
    void deleteFav(MovieEntity taskEntry);
}
