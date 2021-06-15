package pl.uwr.beat_store.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.viewmodels.SongViewModel
import java.io.IOException


class MusicPlayerFragment() : Fragment() {

    private lateinit var beatnameText: TextView;
    private lateinit var producerText: TextView;
    private lateinit var playButton: ImageButton;
    private lateinit var pauseButton: ImageButton;
    private lateinit var musicImage: ImageView;
    private lateinit var cartButton: ImageButton;
    private lateinit var favoriteButton: ImageButton;
    private lateinit var seekBar: SeekBar;
    private lateinit var mediaPlayer: MediaPlayer;
    private lateinit var bottomNavigationMenu: BottomNavigationView;
    private lateinit var audioUrl: String;
    private lateinit var audioName: String;
    private lateinit var audioProducer: String;
    private lateinit var audioImg: String;
    private lateinit var song: Song;
    private var startTime = 0;
    private var finalTime = 0;
    private var pausedTime = 0;

    var job: Job? = null;
    private var SongList: ArrayList<Song>? = null;
    private val firestore = Firebase.firestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer()
        audioUrl = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"; //temp
        song = arguments?.get("song") as Song; // get arguments from singlebeatadapter- discover fragment
        audioName = song.name;
        audioProducer = song.producer;
        audioUrl = song.url
        audioImg = song.image;

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
            SongList = it;
        })

        //println("slist"+SongList?.get(3))
        beatnameText = view.findViewById(R.id.beatname);
        producerText = view.findViewById(R.id.producername);
        playButton = view.findViewById(R.id.start);
        pauseButton = view.findViewById(R.id.pause);
        seekBar = view.findViewById(R.id.seekBar);
        musicImage = view.findViewById(R.id.fragment_musicplayer_image);
        cartButton = view.findViewById(R.id.addtocart)
        favoriteButton = view.findViewById(R.id.addtofavorites)

        bottomNavigationMenu = activity?.findViewById(R.id.nav_view)!!; //Hiding navbar in musicplayer. It is not needed there
        bottomNavigationMenu.visibility = View.GONE;

        beatnameText.text = song.name;
        producerText.text = song.producer;

        Picasso.get().load(song.image).into(musicImage);
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying)
            {
                playAudio(audioUrl, pausedTime);
            }

        }

        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pausedTime = mediaPlayer.currentPosition;
                job?.cancel();
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                Toast.makeText(this.context, "audio paused", Toast.LENGTH_SHORT).show();
            } else {
                //job?.cancel();
                Toast.makeText(this.context, "Audio not workign", Toast.LENGTH_SHORT).show();
            }
        }

        cartButton.setOnClickListener {
            val user = Firebase.auth.currentUser;
            val email = user.email;
            val beatname = song.name;
            val docRef = firestore.collection("users").document(email);
            docRef.get().addOnSuccessListener { document ->
                if (!document.contains("cart")) { //if there is no cart in firebase I have to create one
                    var arr: List<String> = listOf(song.name);

                    val usercart = mapOf(
                            "cart" to arr,
                    )
                    firestore.collection("users").document(email).update(usercart);
                }
            }
            firestore.collection("users").document(email).update("cart", FieldValue.arrayUnion(beatname))
            Toast.makeText(this.context, beatname + " added to cart", Toast.LENGTH_SHORT).show();
        }

        favoriteButton.setOnClickListener {
            val user = Firebase.auth.currentUser;
            val email = user.email;
            val beatname = song.name;
            val docRef = firestore.collection("users").document(email);
            docRef.get().addOnSuccessListener { document ->
                if (!document.contains("favorites")) { //if there is no favorites arr in firebase I have to create one
                    var arr: List<String> = listOf(song.name);

                    val favorites = mapOf(
                            "favorites" to arr,
                    )
                    firestore.collection("users").document(email).update(favorites);
                }
            }
            firestore.collection("users").document(email).update("favorites", FieldValue.arrayUnion(beatname))
            Toast.makeText(this.context, beatname + " added to favorites", Toast.LENGTH_SHORT).show();
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { //using seekbar to change song position
                if (seekBar != null) {
                    mediaPlayer.seekTo(mediaPlayer.duration * seekBar.progress / 100);
                };
            }
        })
        return view;
    }

    @InternalCoroutinesApi
    private fun playAudio(audioUrl: String, pausedOn: Int) {
        mediaPlayer = MediaPlayer(); //initialization
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //setting audio stream type

        try {
            //mediaPlayer.isPlaying=true
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            finalTime = mediaPlayer.duration;
            startTime = mediaPlayer.currentPosition;
            println(startTime);
            seekBar.progress = startTime
            job = updateTime();
            mediaPlayer.start();
            if (pausedOn != 0)
                mediaPlayer.seekTo(pausedOn);
            Toast.makeText(this.context, "Audio is now playing", Toast.LENGTH_SHORT).show();
        } catch (e: IOException) {
            Toast.makeText(this.context, "error in playing: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    @InternalCoroutinesApi
    private fun updateTime(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                // println(SongList);
                startTime = mediaPlayer.currentPosition
                println((startTime.toFloat() / finalTime.toFloat()) * 100);
                seekBar.progress = ((startTime.toFloat() / finalTime.toFloat()) * 100).toInt();
                delay(100)
            }
        }
    }
}

