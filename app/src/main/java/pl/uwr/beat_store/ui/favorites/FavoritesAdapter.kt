package pl.uwr.beat_store.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song

class FavoritesAdapter(
        private var songs: ArrayList<Song>,
        private var context: Context
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private lateinit var song: Song;
    private var layoutinflater: LayoutInflater= LayoutInflater.from(context);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        var view: View = layoutinflater.inflate(R.layout.single_cart_element, parent, false)
        return FavoritesViewHolder(view);
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        song= songs[position];
        holder.songName.text = song.name;
        holder.producerName.text= song.producer;
        Picasso.get().load(song.image).into(holder.songImage);
    }

    override fun getItemCount(): Int {
        return songs.size;
    }

    class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var songImage: ImageView = view.findViewById(R.id.cart_beatImage);
        var songName: TextView = view.findViewById(R.id.cart_beatName);
        var producerName: TextView = view.findViewById(R.id.cart_producerName);

    }
}
