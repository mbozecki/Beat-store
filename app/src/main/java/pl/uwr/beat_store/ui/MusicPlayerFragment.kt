package pl.uwr.beat_store.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.app.Person.fromBundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.ui.discover.DiscoverFragment
import pl.uwr.beat_store.ui.discover.SingleBeatAdapter
import pl.uwr.beat_store.viewmodels.SongViewModel
import java.io.IOException


class MusicPlayerFragment() : Fragment() {

    private lateinit var beatnameText: TextView;
    private lateinit var producerText: TextView;
    private lateinit var playButton : ImageButton;
    private lateinit var pauseButton : ImageButton;
    private lateinit var musicImage : ImageView;
    private lateinit var seekBar :SeekBar;
    private lateinit var mediaPlayer: MediaPlayer;
    private lateinit var firebaseDatabase: FirebaseDatabase;
    private lateinit var bottomNavigationMenu: BottomNavigationView;
    private lateinit var audioUrl : String;
    private lateinit var audioName: String;
    private lateinit var audioProducer : String;
    private lateinit var audioImg: String;
    private lateinit var song: Song;
    private var startTime=0;
    private var finalTime=0;
    private var pausedTime=0;

    var job: Job? =null;
    private var SongList : ArrayList<Song>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        audioUrl = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"; //temp
       //
        song = arguments?.get("song") as Song; // get arguments from singlebeatadapter- discover fragment
        audioName= song.name;
        audioProducer= song.producer;
        println("URI"+song.url);
        audioUrl= song.url.toString();
        audioImg=  song.image;
        println("myson"+ song);
        /* firebaseDatabase= FirebaseDatabase.getInstance();
        val firestore = Firebase.firestore;
        firestore.firestoreSettings= FirebaseFirestoreSettings.Builder().build();

        firestore
                .collection("urls").document("links")
                .get()
                .addOnSuccessListener {
                    audioUrl= it.data?.get("link").toString();
                }.addOnFailureListener {
                    e -> Log.e("E", "Error writing document", e)
                }

        firestore
                .collection("producers")
                .get()
                .addOnSuccessListener { users ->
                    for (user in users)
                    {
                        Log.e("a_url", user.id.toString());
                        firestore.collection("producers").document(user.id).collection("beats").get().addOnSuccessListener { beats ->
                           for (beat in beats)
                           {
                               //Log.e("bv.url", beat.data?.get("image").toString());
                               //Log.e("bv.url", beat.data?.get("name").toString());
                                //Log.e("a.url", beat.data?.get("url").toString());
                            }
                        }
                    }
                }.addOnFailureListener {
                    e -> Log.e("E", "Error writing document", e)
                }


         */
    }

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_musicplayer, container, false);

        //val viewModel= ViewModelProvider(this)[SongViewModel::class.java];
        val viewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
        lifecycle.addObserver(viewModel);
        viewModel.getSongLiveData().observe(viewLifecycleOwner, {
            println("Notcalled"+ it[2]);
            SongList= it;
            //}

        })


        println("slist"+SongList?.get(3))
        beatnameText= view.findViewById(R.id.beatname);
        producerText= view.findViewById(R.id.producername);
        playButton = view.findViewById(R.id.start);
        pauseButton= view.findViewById(R.id.pause);
        seekBar= view.findViewById(R.id.seekBar);
        musicImage= view.findViewById(R.id.fragment_musicplayer_image);

        bottomNavigationMenu = activity?.findViewById(R.id.nav_view)!!; //Hiding navbar in musicplayer. It is not needed there
        bottomNavigationMenu.visibility = View.GONE;

        beatnameText.text = song.name;
        producerText.text = song.producer;

        Picasso.get().load(song.image).into(musicImage);
        playButton.setOnClickListener {
            playAudio(audioUrl, pausedTime);
        }

        pauseButton.setOnClickListener{
            if (mediaPlayer.isPlaying)
            {
                pausedTime = mediaPlayer.currentPosition;
                job?.cancel();
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                Toast.makeText(this.context, "audio paused", Toast.LENGTH_SHORT).show();
            }
            else {
                //job?.cancel();
                Toast.makeText(this.context, "Audio not workign", Toast.LENGTH_SHORT).show();
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { //using seekbar to change song position
                if (seekBar != null ) {
                    mediaPlayer.seekTo(mediaPlayer.duration*seekBar.progress/100);
                };
            }
        })
        return view;
    }

    @InternalCoroutinesApi
    private fun playAudio(audioUrl: String, pausedOn: Int) {
        mediaPlayer= MediaPlayer(); //initialization
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //setting audio stream type

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            finalTime= mediaPlayer.duration;
            startTime= mediaPlayer.currentPosition;
            println(startTime);
            seekBar.progress = startTime
            job= updateTime();
            mediaPlayer.start();
            if (pausedOn !=0)
                mediaPlayer.seekTo(pausedOn);
            Toast.makeText(this.context, "Audio is now playing", Toast.LENGTH_SHORT).show();
        } catch (e: IOException) {
            Toast.makeText(this.context, "error in playing: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    @InternalCoroutinesApi
    private fun updateTime() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
               // println(SongList);
                startTime = mediaPlayer.currentPosition
                println((startTime.toFloat() / finalTime.toFloat()) * 100);
                seekBar.progress = ((startTime.toFloat() / finalTime.toFloat())*100).toInt();
                delay(100)
            }
        }
    }
}

