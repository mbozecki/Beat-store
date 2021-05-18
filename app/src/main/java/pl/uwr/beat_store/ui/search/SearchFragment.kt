package pl.uwr.beat_store.ui.search

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song

class SearchFragment : Fragment() {

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var lvSearchToolbar: ListView;
  private lateinit var searchToolbar : SearchView;
  private  var songList = ArrayList<Song>();

  val bestCities =
          listOf("Lahore", "Berlin", "Lisbon", "Tokyo", "Toronto", "Sydney", "Osaka", "Istanbul")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



  }
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_search, container, false)

    lvSearchToolbar= root.findViewById(R.id.lv_searchToolbar);
    searchToolbar= root.findViewById(R.id.searchToolbar) ;
    searchViewModel.text.observe(viewLifecycleOwner, Observer {
      println("TEXTOBSERVE")

    })
   // songList = searchViewModel.getSearchData();

    searchViewModel.getSearchLiveData().observe(viewLifecycleOwner, Observer {
      songList= it;

      if (songList!=null)
      {

      }
    })
    println("SONGX"+songList);
    var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, songList)
    lvSearchToolbar.adapter = adapter
    searchToolbar.setOnQueryTextListener (object:
      SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
          return false;
      }

        override fun onQueryTextChange(p0: String?): Boolean { //searching live
          adapter.filter.filter(p0)
          return false
    }
  });

    return root
  }


}