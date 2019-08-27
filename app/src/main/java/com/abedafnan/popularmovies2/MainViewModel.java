package com.abedafnan.popularmovies2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.abedafnan.popularmovies2.data.AppDatabase;
import com.abedafnan.popularmovies2.data.MovieEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<MovieEntity>> favoriteMovies;

    public MainViewModel(Application application) {
        super(application);

        // Use the loadAllTasks of the taskDao to initialize the favoriteMovies variable
        AppDatabase database = AppDatabase.getInstance(this.getApplication());

        Log.d(TAG, "Actively retrieving the favoriteMovies from the DataBase");
        favoriteMovies = database.getDao().loadAllFavs();
    }

    public LiveData<List<MovieEntity>> getFavourites() {
        return favoriteMovies;
    }
}
