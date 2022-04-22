package no.kristiania

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_rv_layout.view.*


class SavedResultsAdapter(val context: Context?, val parentFragment: SavedImageFragment, private val imageList: List<StoredResultsModel>): RecyclerView.Adapter<SavedResultsAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val deleteButton: Button = view.findViewById(R.id.deleteResultImageBtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.delete_result_image_rv_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imageList[position].imageLink?.let {
            Picasso.get()
                .load(it)
                .into(holder.itemView.apiImage)
        }

        holder.deleteButton.setOnClickListener {
            imageList[position].id?.let{ id ->
                context?.let { context ->
                    Globals.getDatabase(context).deleteResultImage(imageList[position])
                    parentFragment.updateResultRV()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}