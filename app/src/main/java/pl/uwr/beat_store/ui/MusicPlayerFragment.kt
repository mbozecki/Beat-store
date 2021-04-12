package pl.uwr.beat_store.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.coroutines.*
import pl.uwr.beat_store.R
import java.io.IOException
import java.lang.Runnable
import java.util.concurrent.TimeUnit

class MusicPlayerFragment : Fragment() {

    private lateinit var beatnameText: TextView;
    private lateinit var producerText: TextView;
    private lateinit var playButton : Button;
    private lateinit var pauseButton : Button;
    private lateinit var seekbar :SeekBar;
    private lateinit var mediaPlayer: MediaPlayer;
    private lateinit var firebaseDatabase: FirebaseDatabase; //real-time database
    private lateinit var audioUrl : String;
    private lateinit var databaseReference: DatabaseReference;

    private var myHandler : Handler = Handler();

    private var startTime=0;
    private var finalTime=0;

    val scope= MainScope();
    var job: Job? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate: ")
        audioUrl = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"; //temp
        firebaseDatabase= FirebaseDatabase.getInstance();
        println(FirebaseDatabase.getInstance());
        databaseReference= firebaseDatabase.getReference("url") //potem zmien na miejsce w bazie danych
        //databaseReference.get();
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot.val
                println("XXX"+snapshot.exists());
                audioUrl = snapshot.value as String;

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "failed to get audio url..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_musicplayer, container, false);

        beatnameText= view.findViewById(R.id.beatname);
        producerText= view.findViewById(R.id.producername);
        playButton = view.findViewById(R.id.start);
        pauseButton= view.findViewById(R.id.pause);
        seekbar= view.findViewById(R.id.seekBar);

        playButton.setOnClickListener {
            playAudio(audioUrl);
        }

        pauseButton.setOnClickListener{
            if (mediaPlayer.isPlaying)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                Toast.makeText(this.context, "audio paused", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this.context, "Audio aint workin", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    @InternalCoroutinesApi
    private fun playAudio(audioUrl: String) {
        mediaPlayer= MediaPlayer(); //initialization
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //setting audio stream type

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();

            finalTime= mediaPlayer.duration;
            startTime= mediaPlayer.currentPosition;
            println(startTime);
            seekbar.progress = startTime
           // myHandler.postDelayed(UpdateSongTime, 100)
            job= updateTime();
            mediaPlayer.start();
            Toast.makeText(this.context, "Audio is now playing", Toast.LENGTH_SHORT).show();
        } catch (e: IOException) {
            Toast.makeText(this.context, "error in playing: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    /*private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition
            println((startTime.toFloat() / finalTime.toFloat())*100);
            seekbar.progress = ((startTime.toFloat() / finalTime.toFloat())*100).toInt();
            myHandler.postDelayed(this, 100)
        }
    }

     */

    @InternalCoroutinesApi
    private fun updateTime() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                startTime = mediaPlayer.currentPosition
                println((startTime.toFloat() / finalTime.toFloat())*100);
                seekbar.progress = ((startTime.toFloat() / finalTime.toFloat())*100).toInt();
                delay(100)
            }
        }
    }
}

