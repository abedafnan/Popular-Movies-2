package com.abedafnan.popularmovies2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abedafnan.popularmovies2.R;
import com.abedafnan.popularmovies2.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private List<Trailer> mTrailersList;
    private Context mContext;

    public TrailersAdapter(Context context, List<Trailer> trailers) {
        mTrailersList = trailers;
        mContext = context;
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_trailer, viewGroup, false);

        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder trailersViewHolder, int i) {
        final Trailer currentTrailer = mTrailersList.get(i);

        String thumbUrl = "https://img.youtube.com/vi/" + currentTrailer.getKey() +"/mqdefault.jpg";
        Picasso.get().load(thumbUrl).placeholder(R.drawable.placeholder).into(trailersViewHolder.videoImageView);

        trailersViewHolder.playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.youtube.com/watch?v=" + currentTrailer.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailersList.size();
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImageView;
        ImageView playImageView;

        TrailersViewHolder(@NonNull View itemView) {
            super(itemView);

            videoImageView = itemView.findViewById(R.id.iv_trailer_thumb);
            playImageView = itemView.findViewById(R.id.iv_play);
        }
    }
}
