package pl.uwr.beat_store.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.data.repos.CartRepository
import pl.uwr.beat_store.data.repos.SongRepository

class CartViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    private var cartRepository: CartRepository = CartRepository();
    private var cart = MutableLiveData<ArrayList<String>>();

    private var songRepository: SongRepository = SongRepository();
    private var songs = MutableLiveData<ArrayList<Song>>();

    private var songsInCart = MutableLiveData<ArrayList<Song>>();
    private var songsTmp = ArrayList<Song>();

    private var songsPurchased= MutableLiveData<ArrayList<Song>>();
    private var purchase= MutableLiveData<ArrayList<String>>();
    private var purchaseTmp = ArrayList<Song>();
    init {
        viewModelScope.launch {
            val queue = async(Dispatchers.IO) {
                cart.postValue(cartRepository.getCartData());
                songs.postValue(songRepository.getSongData());
                purchase.postValue(cartRepository.getPurchaseData()) //for profile and generating pdf
            }
            queue.await(); //waiting for results in order to search for songs in cart

            for (x in songs.value!!) {
                if (cart.value?.contains(x.name) == true) {
                    songsTmp.add(x);
                }
                if (purchase.value?.contains(x.name)==true) {
                    purchaseTmp.add(x)
                }
            }
            songsPurchased.postValue(purchaseTmp) // for profile framgent
            songsInCart.postValue(songsTmp);

        }
    }

    fun getCartLiveData(): MutableLiveData<ArrayList<Song>> {
        return songsInCart;
    }

    fun deleteCart(name: String) {
        cartRepository.deleteCartData(name);
    }

    fun getPurchaseLiveData(): MutableLiveData<ArrayList<Song>>{
        return songsPurchased;
    }

    fun addToPurchase(songList: ArrayList<Song>)
    {
        cartRepository.addToPurchase(songList)
    }
}