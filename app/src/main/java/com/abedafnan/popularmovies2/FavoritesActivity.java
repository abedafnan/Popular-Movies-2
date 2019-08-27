package com.abedafnan.popularmovies2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.abedafnan.popularmovies2.adapters.FavsAdapter;
import com.abedafnan.popularmovies2.data.MovieEntity;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = FavoritesActivity.class.getSimpleName();

    private FavsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favourites");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Load Favourite Movies
        setUpViewModel();

        // Generate the RecyclerView
        setupTheList();
    }

    private void setUpViewModel() {
        // Declare a ViewModel variable and initialize it
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavourites().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> taskEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                mAdapter.setFavsList(taskEntries);
            }
        });
    }

    private void setupTheList() {
        RecyclerView recyclerView = findViewById(R.id.rv_favs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mAdapter = new FavsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
