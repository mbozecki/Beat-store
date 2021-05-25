package pl.uwr.beat_store.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private var totalPrice=0.0;
    private lateinit var totalText : TextView;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        var rvCart: RecyclerView = view.findViewById(R.id.rvCart);
        layoutManager= LinearLayoutManager(requireContext());
        adapter= CartAdapter(songList, requireContext());
        rvCart.layoutManager= layoutManager;
        rvCart.adapter= adapter;

        totalText= view.findViewById(R.id.total);
        totalText.text = "Total: "+totalPrice.toString()+"$";
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        lifecycle.addObserver(cartViewModel);
        cartViewModel.getCartLiveData().observe(viewLifecycleOwner, {
            songList=it;
            for(x in it)
            {
                totalPrice+=x.price;
            }

            onViewCreated(requireView(), savedInstanceState)

        })
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
}