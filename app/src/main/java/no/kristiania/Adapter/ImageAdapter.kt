package no.kristiania.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import no.kristiania.Model.ImageApi
import no.kristiania.R

class ImageAdapter(val context: Context, private val images: ArrayList<ImageApi>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_rv_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Picasso.get().load(images[position].url).into(holder.itemView.apiImage)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(v:View?) : RecyclerView.ViewHolder(v!!), View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }
        init {
            itemView.setOnClickListener(this)
        }

        val image = itemView.apiImage!!
    }
}