package pl.uwr.beat_store.ui.discover

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.CollectionUtils
import com.google.common.collect.Iterables.size
import com.google.common.collect.Iterators.size
import okio.Utf8.size
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import java.nio.file.Files;
import java.nio.file.Files.size


class TypeBeatsAdapter(
    private var typebeatslist : ArrayList<ArrayList<Song>>,
    private var context: Context
)
    : RecyclerView.Adapter<TypeBeatsAdapter.MyViewHolder>() {

    private var layoutInflater : LayoutInflater = LayoutInflater.from(context);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view : View = layoutInflater.inflate(R.layout.single_beat_category, parent, false);
        return MyViewHolder(view);
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recyclerView.adapter = SingleBeatAdapter(context, typebeatslist[position]) //here
        holder.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.recyclerView.setHasFixedSize(true)
        when (position) {
            0 -> holder.typeBeatHeading.text = "The Weeknd Type beats"
            1 -> holder.typeBeatHeading.text = "6lack Type beats"
        }

    }

    override fun getItemCount(): Int {
        println("sizeG"+typebeatslist.forEach { println(it.toString()) });
        return typebeatslist.size;
        // return 4; //TODO: change return
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerView = view.findViewById(R.id.rvTypeBeats) as RecyclerView
        var typeBeatHeading = view.findViewById(R.id.typeBeatHeading) as TextView

    }
}