package pl.uwr.beat_store.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.SongRepository


class SongViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver  {
    private var songRepository : SongRepository = SongRepository();
    //private var songs = MutableLiveData<List<Song>>();
    private var songs = MutableLiveData<ArrayList<Song>>();





    init {
       // ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        println("MUSIC INIT")
        viewModelScope.launch {
           println("songRepository.getSongData()")
           songs.postValue(songRepository.getSongData());
               // songs.postValue()

            println("songs: " + songs);
       }
        //songs = songRepository.getSongDData();
    }

    fun getSongLiveData() : MutableLiveData<ArrayList<Song>> {
        println(" getSongLiveData")
        return songs;
    }


}