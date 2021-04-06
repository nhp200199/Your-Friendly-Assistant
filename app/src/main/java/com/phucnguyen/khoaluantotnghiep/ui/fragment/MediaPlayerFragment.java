package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.phucnguyen.khoaluantotnghiep.R;

public class MediaPlayerFragment extends Fragment {
    private ImageView imgRealProductReview;
    private VideoView videoReadProductReview;

    private String url;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_player, container, false);
        imgRealProductReview = (ImageView) v.findViewById(R.id.imgRealProductReview);
        videoReadProductReview = (VideoView) v.findViewById(R.id.videoRealProductReview);

        url = getArguments().getString("url");
        //check if the url is containing a video media
        if (getArguments().containsKey("duration")){
            imgRealProductReview.setVisibility(View.GONE);

            videoReadProductReview.setVideoURI(Uri.parse(url));
            MediaController mediaController = new MediaController(requireContext(), false);
            videoReadProductReview.start();
            videoReadProductReview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //we need to wait for the video has been ready (decide the width and height it needs)
                    //then set the media controller accordingly
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                            videoReadProductReview.setMediaController(mediaController);
                            mediaController.setAnchorView(videoReadProductReview);
                        }
                    });
                }
            });
        }else {
            videoReadProductReview.setVisibility(View.GONE);
            Glide.with(requireContext())
                    .load(url)
                    .into(imgRealProductReview);
        }
        // Inflate the layout for this fragment
        return v;
    }
}