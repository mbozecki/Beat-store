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
import pl.uwr.beat_store.ui.cart.CartViewModel

class FavoritesFragment : Fragment() {

  private lateinit var favoritesViewModel: FavoritesViewModel
  private var layoutManager: RecyclerView.LayoutManager? = null;
  private var adapter: RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>? = null;
  private var songList= ArrayList<Song>();

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState);
    var rvFavorites: RecyclerView = view.findViewById(R.id.rvFavorites);
    layoutManager= LinearLayoutManager(requireContext());
    adapter= FavoritesAdapter(songList, requireContext());
    rvFavorites.layoutManager= layoutManager;
    rvFavorites.adapter= adapter;

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_favorites, container, false)

    favoritesViewModel.getFavoritesLiveData().observe(viewLifecycleOwner, {
      songList=it;


      onViewCreated(requireView(), savedInstanceState)

    })
    return root
  }
}