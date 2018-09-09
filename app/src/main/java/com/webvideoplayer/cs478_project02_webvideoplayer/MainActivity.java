//
// Michal Bochnak, Netid: mbochn2
// CS 478 - Project #02, Web Video Player
// UIC, March 2, 2018
// Professor: Ugo Buy
//
// MainActivity.java
//


package com.webvideoplayer.cs478_project02_webvideoplayer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;



//
// Functionality:
//  - displays the song library
//  - add / remove song
//  - play song video
//  - see song / artist information web page
//
public class MainActivity extends AppCompatActivity {

    private SongsLibrary songsLibrary = new SongsLibrary();
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListViewListener();
        registerForContextMenu(findViewById(R.id.libraryListView));
        loadAndDisplaySongList();

        // setup dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_song);
        addDialogButtonsListeners();

        if (savedInstanceState != null) {
            // rebuilt song list
            list.clear();
            songsLibrary = (SongsLibrary)savedInstanceState.getParcelable("songs");
            buildList();
            setAdapter();

            // rebuilt dialog
            ((EditText)dialog.findViewById(R.id.titleEditText)).setText
                    ((String)savedInstanceState.getSerializable("title"));
            ((EditText)dialog.findViewById(R.id.artistEditText)).setText
                    ((String)savedInstanceState.getSerializable("artist"));
            ((EditText)dialog.findViewById(R.id.videoUrlEditText)).setText
                    ((String)savedInstanceState.getSerializable("videoURL"));
            ((EditText)dialog.findViewById(R.id.songInfoUrlEditText)).setText
                    ((String)savedInstanceState.getSerializable("songInfoUrl"));
            ((EditText)dialog.findViewById(R.id.artistInfoUrlEditText)).setText
                    ((String)savedInstanceState.getSerializable("artistInfoUrl"));

            // display dialog if it was displayed
            if (((Boolean)savedInstanceState.getSerializable("dialogOn")) == true) {
                dialog.show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save songs library
        outState.putParcelable("songs", songsLibrary);

        // save dialog values
        outState.putSerializable("title", ((EditText)dialog.findViewById
                (R.id.titleEditText)).getText().toString());
        outState.putSerializable("artist", ((EditText)dialog.findViewById
                (R.id.artistEditText)).getText().toString());
        outState.putSerializable("videoURL", ((EditText)dialog.findViewById
                (R.id.videoUrlEditText)).getText().toString());
        outState.putSerializable("songInfoUrl", ((EditText)dialog.findViewById
                (R.id.songInfoUrlEditText)).getText().toString());
        outState.putSerializable("artistInfoUrl", ((EditText)dialog.findViewById
                (R.id.artistInfoUrlEditText)).getText().toString());
        // save if dialog should be shown or not
        if (dialog.isShowing())
            outState.putSerializable("dialogOn", true);
        else
            outState.putSerializable("dialogOn", false);

        super.onSaveInstanceState(outState);
    }

    @Override
    // override default menu and inflate with menu options
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return  true;
    }

    @Override
    // handle click from menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_song_menu_item:
                addSong();
                return true;
            case R.id.remove_song_menu_option:
                generateRemoveSongSubmenu(item.getSubMenu());
                return true;
            case R.id.exit_menu_item:
                exitApp();
        }
        // song to remove clicked
        removeSong(item.getTitle().toString());

        return false;
    }

    private void exitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory( Intent.CATEGORY_HOME );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu,
                                    final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("View video");
        menu.add("View song info");
        menu.add("View artist info");
    }

    @Override
    // open URL for video, song info, or artist info
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("View video")) {
            startWebVideoPlayer(songsLibrary.getVideoURL
                    (((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position));
            return true;
        }
        if (item.getTitle().equals("View song info")) {
            startWebVideoPlayer(songsLibrary.getSongInfoURL
                    (((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position));
            return true;
        }
        if (item.getTitle().equals("View artist info")) {
            startWebVideoPlayer(songsLibrary.getArtistInfoURL
                    (((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position));
            return true;
        }

        return false;
    }

    // build list of songs fot the adapter
    public void buildList() {
        HashMap<String,String> item;
        for (Song s: songsLibrary.getSongs()) {
            item = new HashMap<String,String>();
            item.put( "title", s.getTitle());
            item.put( "artist", s.getArtist());
            list.add(item);
        }
    }

    private boolean addSong() {
        dialog.show();
        return true;
    }

    private void removeSong(String title) {
        if (songsLibrary.numberOfSongs() > 1) {
            // find song index
            int songIndex = songsLibrary.getSongIndex(title);
            list.remove(songIndex);
            songsLibrary.remove(songIndex);
            // Update UI list
            ((ListView)findViewById(R.id.libraryListView)).invalidateViews();
        }
        else {
            Toast.makeText(getApplicationContext(), "Cannot remove last song.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void generateRemoveSongSubmenu(SubMenu sm) {
        sm.clear();
        for (HashMap<String, String> s : list)
            sm.add(s.get("title"));
    }

    // load songs and artists from resources file
    private  void loadAndDisplaySongList() {

        HashMap<String,String> item;
        String titlesArray[] = getResources().getStringArray(R.array.titles);
        String artistArray[] = getResources().getStringArray(R.array.artists);
        String videoArrayURL[] = getResources().getStringArray(R.array.videoURLs);
        String songInfoArrayURL[] = getResources().getStringArray(R.array.songInfoURLs);
        String artistInfoArrayURL[] = getResources().getStringArray(R.array.artistInfoURLs);

        for(int i=0; i< titlesArray.length; i++){
            item = new HashMap<String,String>();
            // capitalize title and artist first letter
            String title = titlesArray[i].substring(0, 1).toUpperCase() + titlesArray[i].substring(1);
            String artist = artistArray[i].substring(0, 1).toUpperCase() + artistArray[i].substring(1);
            item.put( "title", title);
            item.put( "artist", artist);
            list.add(item);

            songsLibrary.add(new Song(titlesArray[i], artistArray[i], videoArrayURL[i],
                    songInfoArrayURL[i], artistInfoArrayURL[i]));
        }

        sortList();
        setAdapter();
    }

    private void setAdapter() {
        ((ListView)findViewById(R.id.libraryListView)).setAdapter(
                new ListAdapter(this, list));
    }

    private void addListViewListener() {
        ((ListView)findViewById(R.id.libraryListView)).setOnItemClickListener
                (new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adptr, View v, int pos, long id) {
                        // start web view activity with the URL
                        startWebVideoPlayer(songsLibrary.getVideoURL(pos));
                    }
                }
        );
    }

    private void startWebVideoPlayer(String url) {
            Intent intent = new Intent(getApplicationContext(), WebVideoPlayer.class);
            intent.putExtra("url", url);
            startActivity(intent);
    }

    private void sortList() {
        Collections.sort(list, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return o1.get("title").compareTo(o2.get("title"));
            }
        });
    }

    private void addDialogButtonsListeners() {
        dialog.findViewById(R.id.addSongButton).setOnClickListener(new AddSongListener());
        dialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogAllFields();
            }
        });
    }

    private void clearDialogAllFields() {
        ((EditText)dialog.findViewById(R.id.titleEditText)).setText("");
        ((EditText)dialog.findViewById(R.id.artistEditText)).setText("");
        ((EditText)dialog.findViewById(R.id.videoUrlEditText)).setText("");
        ((EditText)dialog.findViewById(R.id.songInfoUrlEditText)).setText("");
        ((EditText)dialog.findViewById(R.id.artistInfoUrlEditText)).setText("");
    }

    //
    // Verify if needed info is provided, if so update the data structures with new song added
    //
    public class AddSongListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String title = ((EditText)dialog.findViewById(R.id.titleEditText))
                    .getText().toString();
            String artist = ((EditText)dialog.findViewById(R.id.artistEditText))
                    .getText().toString();
            String videoURL = ((EditText)dialog.findViewById(R.id.videoUrlEditText))
                    .getText().toString();
            String songInfoURL = ((EditText)dialog.findViewById(R.id.songInfoUrlEditText))
                    .getText().toString();
            String artistInfoURL = ((EditText)dialog.findViewById(R.id.artistInfoUrlEditText))
                    .getText().toString();

            if (title.length() > 0 && artist.length() > 0 && videoURL.length() > 0
                    && songInfoURL.length() > 0 && artistInfoURL.length() > 0) {

                HashMap<String, String> item = new HashMap<String, String>();
                // capitalize title and artist first letter
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
                artist = artist.substring(0, 1).toUpperCase() + artist.substring(1);
                item.put("title", title);
                item.put("artist", artist);
                list.add(item);
                ((ListView)findViewById(R.id.libraryListView)).invalidateViews();
                songsLibrary.add(new Song(title, artist, videoURL, songInfoURL, artistInfoURL));
                sortList();

                for (HashMap<String, String> el : list) {
                    Log.i("----A", "+" + el.get("title"));
                }
                //adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(),"\'" + title + "\' by "
                                + artist + " was added to your library.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Fill all fields.",
                        Toast.LENGTH_SHORT).show();
        }
    }


}   // MainActivity class
