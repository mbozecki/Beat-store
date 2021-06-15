package pl.uwr.beat_store.ui.discover


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.viewmodels.SongViewModel


class DiscoverFragment : Fragment() {

    private lateinit var discoverViewModel: DiscoverViewModel
    private var songList = ArrayList<Song>();
    private var typeBeats = ArrayList<ArrayList<Song>>(3);
    private lateinit var bottomNavigationMenu: BottomNavigationView;
    private var isDataLoaded = false;
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TypeBeatsAdapter.MyViewHolder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var rvSubject: RecyclerView = view.findViewById(R.id.rvTypeBeatsDiscover) as RecyclerView;
        layoutManager = LinearLayoutManager(requireContext())
        adapter = TypeBeatsAdapter(typeBeats, requireContext());
        rvSubject.layoutManager = layoutManager;
        rvSubject.adapter = adapter;
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        discoverViewModel =
                ViewModelProvider(this).get(DiscoverViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_discover, container, false)

        bottomNavigationMenu = activity?.findViewById(R.id.nav_view)!!; //set navabar to be visible
        bottomNavigationMenu.visibility = View.VISIBLE;


        val viewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
        lifecycle.addObserver(viewModel);
        viewModel.getSongLiveData().observe(viewLifecycleOwner, {
            songList = it;

            if (!isDataLoaded) {
                var weekndList = ArrayList<Song>();
                var blackList = ArrayList<Song>();
                var drakeList = ArrayList<Song>();

                songList.forEach { song ->
                    when (song.type) {
                        "theweeknd" -> {
                            weekndList.add(song);
                        }
                        "6lack" -> {
                            blackList.add(song);
                        }
                        "drake" -> {
                            drakeList.add(song);
                        }
                    }

                }
                typeBeats.add(weekndList);
                typeBeats.add(blackList);
                typeBeats.add(drakeList);

                isDataLoaded = true;
            }

            if (typeBeats != null) {
                onViewCreated(requireView(), savedInstanceState)

            }
        })

        return root
    }

}