package pl.uwr.beat_store.ui.discover

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.ui.MusicPlayerFragment


class SingleBeatAdapter(context: Context, private var songs: ArrayList<Song>) :
    RecyclerView.Adapter<SingleBeatAdapter.CustomViewHolder>() {
    private var inflater : LayoutInflater = LayoutInflater.from(context);
    private lateinit var song : Song;
    private lateinit var  mOnClickListener: View.OnClickListener;

    private fun setListener(song: Song) //setting correct song onClickListener
    {
        mOnClickListener= View.OnClickListener { v->
            println("ONCLICKED"+song);
            val action = DiscoverFragmentDirections.actionNavigationDiscoverToMusicPlayerFragment(song);
            Navigation.findNavController(v)
                    .navigate(action)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        var view: View = inflater.inflate(R.layout.single_beat_element, parent, false);

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        song=  songs[position];
        holder.songName.text = song.name;
        holder.producerName.text= song.producer;
        Picasso.get().load(song.image).into(holder.songImage);
        with(holder.itemView)
        {
            setListener(song);
            setOnClickListener(mOnClickListener);
        }
        }

    override fun getItemCount(): Int {
        return songs.size
    }

     class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
             var songImage: ImageView = itemView.findViewById<View>(R.id.ivChapter) as ImageView
             var songName: TextView =
                 itemView.findViewById<View>(R.id.tvChapterName) as TextView
             var producerName: TextView =
                itemView.findViewById<View>(R.id.producerName) as TextView


     }
}