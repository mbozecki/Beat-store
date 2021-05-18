package pl.uwr.beat_store.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.SongRepository

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text

    private var songRepository : SongRepository = SongRepository();
    private var songs = MutableLiveData<ArrayList<Song>>();

    init {
        viewModelScope.launch {
            //println("songRepository.getSongData()")
            songs.postValue(songRepository.getSongData());
            //println("songs: " + songs);
        }
    }

    fun getSearchLiveData() : MutableLiveData<ArrayList<Song>> {
        println(" getSongLiveData")
        return songs;
    }
}