package no.kristiania

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class ImageAdapter(val context: Context?, private val imageList: List<ImageApi>): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val saveButton: Button = view.findViewById(R.id.saveImageBtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_rv_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imageList[position].imageUri?.let {
            holder.itemView.apiImage.setImageURI(Uri.parse(it))
        }
        imageList[position].imageUrl?.let {
            Picasso.get()
                .load(imageList[position].imageUrl)
                .into(holder.itemView.apiImage)
        }

        holder.saveButton.setOnClickListener {
            imageList[position].imageUrl?.let{ url ->
                context?.let { context ->
                    Toast.makeText(holder.view.context, R.string.saveBtn, Toast.LENGTH_LONG).show()
                    Globals.getDatabase(context).saveImagesFromResult(StoredResultsModel(imageLink = url))
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    /*class ViewHolder(val view:View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener{
        override fun onClick(p0: View?) {
            //onClick function here
        }

        init {
            itemView.setOnClickListener(this)
        }

        val apiImage = itemView.apiImage!!
    }*/
}