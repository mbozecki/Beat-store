package pl.uwr.beat_store.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.viewmodels.SongViewModel

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null;
    private var adapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>? = null;
    private var songList= ArrayList<Song>();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        var rvCart: RecyclerView = view.findViewById(R.id.rvCart);
        layoutManager= LinearLayoutManager(requireContext());
        adapter= CartAdapter(songList, requireContext());
        rvCart.layoutManager= layoutManager;
        rvCart.adapter= adapter;
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        songList.add(Song("url","Nazwa","Producento", "https://cdn.beatstars.com/eyJidWNrZXQiOiJidHMtY29udGVudCIsImtleSI6InVzZXJzL3Byb2QvMjAxMDIxL2ltYWdlL3YxTDhjVllabHVYSC9pNGIzeHNydWkuanBnIiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6bnVsbCwid2lkdGgiOjIwMCwiaGVpZ2h0IjoyMDB9fX0=","weeknd" ,21.12))
        songList.add(Song("url","Nazwa","Producento", "https://cdn.beatstars.com/eyJidWNrZXQiOiJidHMtY29udGVudCIsImtleSI6InVzZXJzL3Byb2QvMjAxMDIxL2ltYWdlL3YxTDhjVllabHVYSC9pNGIzeHNydWkuanBnIiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6bnVsbCwid2lkdGgiOjIwMCwiaGVpZ2h0IjoyMDB9fX0=","weeknd" ,13.3))

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        lifecycle.addObserver(cartViewModel);
        cartViewModel.getCartLiveData().observe(viewLifecycleOwner, {
            //println("Notcalled" + it[2]);
            println("BAMBA"+it);
        })
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
}