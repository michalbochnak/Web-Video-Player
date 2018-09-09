//
// Michal Bochnak, Netid: mbochn2
// CS 478 - Project #02, Web Video Player
// UIC, March 2, 2018
// Professor: Ugo Buy
//
// Song.java
//


package com.webvideoplayer.cs478_project02_webvideoplayer;

import android.os.Parcel;
import android.os.Parcelable;


//
// Data related to song
//
public class Song implements Parcelable {

    private String title, artist, videoURL, songInfoURL, artistInfoURL;

    public Song(String title, String artist, String videoURL, String songInfoURL, String artistInfoURL) {
        this.title = title;
        this.artist = artist;
        this.videoURL = videoURL;
        this.songInfoURL = songInfoURL;
        this.artistInfoURL = artistInfoURL;
    }

    // Parcel constructor
    public Song(Parcel in) {
        this.title = in.readString();
        this.artist = in.readString();
        this.videoURL = in.readString();
        this.songInfoURL = in.readString();
        this.artistInfoURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(videoURL);
        dest.writeString(songInfoURL);
        dest.writeString(artistInfoURL);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getSongInfoURL() {
        return songInfoURL;
    }

    public String getArtistInfoURL() {
        return artistInfoURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public void setSongInfoURL(String songInfoURL) {
        this.songInfoURL = songInfoURL;
    }

    public void setArtistInfoURL(String artistInfoURL) {
        this.artistInfoURL = artistInfoURL;
    }

}   // Song class
