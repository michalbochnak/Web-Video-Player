//
// Michal Bochnak, Netid: mbochn2
// CS 478 - Project #02, Web Video Player
// UIC, March 2, 2018
// Professor: Ugo Buy
//
// SongsLibrary.java
//

package com.webvideoplayer.cs478_project02_webvideoplayer;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


//
// Operations to be performed on the songs library
//
public class SongsLibrary implements Parcelable{

    private ArrayList<Song> songs;

    public SongsLibrary() {
        songs = new ArrayList<Song>();
    }

    // parcelable constructor
    public SongsLibrary(Parcel in) {
        in.readTypedList(songs, SongsLibrary.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.songs);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
       public SongsLibrary createFromParcel(Parcel in) {
           return new SongsLibrary(in);
       }

       public SongsLibrary[] newArray(int size) {
           return new SongsLibrary[size];
       }
    };

    public String getVideoURL(int index) {
        return songs.get(index).getVideoURL();
    }


    public void add(Song song) {
        songs.add(song);
        this.sortByTitle();
    }

    public String getSongInfoURL(int index) {
        return songs.get(index).getSongInfoURL();
    }

    public String getArtistInfoURL(int index) {
        return songs.get(index).getArtistInfoURL();
    }

    public void sortByTitle() {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getSongIndex(String title) {
        for (int i = 0; i < songs.size(); ++i)
            if (title.equals(songs.get(i).getTitle()))
                return i;

        // not found
        return -1;
    }

    public void remove(int index) {
        songs.remove(index);
    }

    public int numberOfSongs() {
        return songs.size();
    }

    // for debug
    public void listTitles() {
        Log.i("SongsLibrary: ", "Songs items:");
        for (Song s: songs)
            Log.i("SongsLibrary: ", s.getTitle());
    }
}
