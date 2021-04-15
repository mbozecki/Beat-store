package pl.uwr.beat_store.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.SongRepository

class SongViewModel(application: Application) : AndroidViewModel(application) {
    //private var songRepository: SongRepository();
    private val _songs = MutableLiveData<List<Song>>(); //TODO : Change the type returned
    val songs: LiveData<Song> = _songs; //Mutable properties shouldt be exposed to fragment, hence the _songs and songs

    init {
        viewModelScope.launch {
            _songs.value= SongRepository.getSongData();
        }
    }


}