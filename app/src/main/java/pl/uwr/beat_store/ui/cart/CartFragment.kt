package pl.uwr.beat_store.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.ui.cart.CartAdapter

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
        songList.add(Song("url","Nazwa","Producento", "https://cdn.beatstars.com/eyJidWNrZXQiOiJidHMtY29udGVudCIsImtleSI6InVzZXJzL3Byb2QvMjAxMDIxL2ltYWdlL3YxTDhjVllabHVYSC9pNGIzeHNydWkuanBnIiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6bnVsbCwid2lkdGgiOjIwMCwiaGVpZ2h0IjoyMDB9fX0=","weeknd" ))
        songList.add(Song("url","Nazwa","Producento", "https://cdn.beatstars.com/eyJidWNrZXQiOiJidHMtY29udGVudCIsImtleSI6InVzZXJzL3Byb2QvMjAxMDIxL2ltYWdlL3YxTDhjVllabHVYSC9pNGIzeHNydWkuanBnIiwiZWRpdHMiOnsicmVzaXplIjp7ImZpdCI6bnVsbCwid2lkdGgiOjIwMCwiaGVpZ2h0IjoyMDB9fX0=","weeknd" ))

        cartViewModel =
                ViewModelProvider(this).get(CartViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        cartViewModel.text.observe(viewLifecycleOwner, Observer {
          //textView.text = it
        })
        return root
    }
}