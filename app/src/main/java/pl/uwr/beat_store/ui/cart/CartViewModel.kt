package pl.uwr.beat_store.ui.cart

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.CartRepository
import pl.uwr.beat_store.data.repos.SongRepository
import pl.uwr.beat_store.viewmodels.SongViewModel

class CartViewModel (application: Application) : AndroidViewModel(application), LifecycleObserver {
    private var cartRepository : CartRepository = CartRepository();
    private var cart = MutableLiveData<ArrayList<String>>();

    private var songRepository: SongRepository = SongRepository();
    private var songs= MutableLiveData<ArrayList<Song>>();

    private var songsInCart= MutableLiveData<ArrayList<Song>>();
    private var songsTmp = ArrayList<Song>();

    init {
        viewModelScope.launch {
            val queue= async(Dispatchers.IO){
                cart.postValue(cartRepository.getCartData());
                songs.postValue(songRepository.getSongData()); //TODO change gettins songs
            }
            queue.await(); //waiting for results in order to search for songs in cart

            for(x in songs.value!!) {
                if (cart.value?.contains(x.name) == true)
                {
                    songsTmp.add(x);
                }
            }
            songsInCart.postValue(songsTmp);

        }
    }

    fun getCartLiveData() : MutableLiveData<ArrayList<Song>> {
        println("getCartLiveData")
        return songsInCart;
    }
}