package pl.uwr.beat_store.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.SongRepository


class SongViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    private var songRepository: SongRepository = SongRepository();
    private var songs = MutableLiveData<ArrayList<Song>>();


    init {
        viewModelScope.launch {
            songs.postValue(songRepository.getSongData());
        }
    }

    fun getSongLiveData(): MutableLiveData<ArrayList<Song>> {
        println(" getSongLiveData")
        return songs;
    }


}