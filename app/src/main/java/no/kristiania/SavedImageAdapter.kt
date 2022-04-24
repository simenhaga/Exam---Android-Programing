package no.kristiania

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class SavedImageAdapter(val context: Context?, private val parentFragment: SavedImageFragment, private val imageList: List<StoredImageModel>): RecyclerView.Adapter<SavedImageAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val deleteButton: Button = view.findViewById(R.id.deleteImageBtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.delete_stored_image_rv, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imageList[position].uri.let {
            holder.itemView.apiImage.setImageURI(Uri.parse(it))
        }
        holder.view.findViewById<Button>(R.id.deleteImageBtn).setOnClickListener {
            imageList[position].uri.let{
                context?.let { context ->
                    Toast.makeText(holder.view.context, R.string.deleteBtn, Toast.LENGTH_LONG).show()
                    Globals.getDatabase(context).deleteStoredImage(imageList[position])
                    parentFragment.updateSavedRV()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}