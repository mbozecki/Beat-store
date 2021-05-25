package pl.uwr.beat_store.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.uwr.beat_store.R
import pl.uwr.beat_store.ui.cart.CartViewModel

class FavoritesFragment : Fragment() {

  private lateinit var favoritesViewModel: FavoritesViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_favorites, container, false)

    favoritesViewModel.text.observe(viewLifecycleOwner, Observer {
      //textView.text = it
    })
    return root
  }
}