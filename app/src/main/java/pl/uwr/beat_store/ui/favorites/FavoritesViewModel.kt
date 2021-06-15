package pl.uwr.beat_store.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.FavoritesRepository
import pl.uwr.beat_store.data.repos.SongRepository

class FavoritesViewModel : ViewModel() {

    private var favoritesRepository: FavoritesRepository = FavoritesRepository();
    private var favorites = MutableLiveData<ArrayList<String>>();

    private var songRepository: SongRepository = SongRepository();
    private var songs = MutableLiveData<ArrayList<Song>>();

    private var songsInFavorites = MutableLiveData<ArrayList<Song>>();
    private var songsTmp = ArrayList<Song>();

    init {
        viewModelScope.launch {
            val queue = async(Dispatchers.IO) {
                favorites.postValue(favoritesRepository.getFavoritesData());
                songs.postValue(songRepository.getSongData())
            }
            queue.await()

            for (x in songs.value!!) {
                if (favorites.value?.contains(x.name) == true) {
                    songsTmp.add(x);
                }
            }
            songsInFavorites.postValue(songsTmp);

        }
    }

    fun getFavoritesLiveData(): MutableLiveData<ArrayList<Song>> {
        println("getCartLiveData")
        return songsInFavorites;
    }

    fun deleteFavorite(name: String) {
        favoritesRepository.deleteFavoritesData(name);

    }
}