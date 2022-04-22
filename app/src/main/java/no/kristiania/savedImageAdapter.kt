package no.kristiania

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class savedImageAdapter(val context: Context?, private val imageList: List<ImageApi>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.savedimage_rv_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        imageList[position].imageUri?.let {
            holder.itemView.apiImage.setImageURI(Uri.parse(it))
        }
        imageList[position].imageUrl?.let {
            Picasso.get()
                .load(imageList[position].imageUrl)
                .into(holder.itemView.apiImage)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(v:View?) : RecyclerView.ViewHolder(v!!), View.OnClickListener{
        override fun onClick(p0: View?) {
            //onClick function here
        }

        init {
            itemView.setOnClickListener(this)
        }

        val apiImage = itemView.apiImage!!
    }
}