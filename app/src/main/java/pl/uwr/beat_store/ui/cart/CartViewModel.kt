package pl.uwr.beat_store.ui.cart

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
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
            cart.postValue(cartRepository.getCartData());
            songs.postValue(songRepository.getSongData()); //TODO change gettins songs

            println("VALSALD"+songs.value);
            for(x in songs.value!!) {
                println("VALX"+x);
                if (cart.value?.contains(x.name) == true)
                {
                    songsTmp.add(x);
                }
            }
            songsInCart.postValue(songsTmp);

            //cartRepository.getCartData()
        }
    }

    fun getCartLiveData() : MutableLiveData<ArrayList<Song>> {
        println("getCartLiveData")
        return songsInCart;
    }
}