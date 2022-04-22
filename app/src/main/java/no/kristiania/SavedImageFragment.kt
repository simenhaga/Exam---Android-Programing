package no.kristiania

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import no.kristiania.databinding.SavedImageFragment1Binding

class SavedImageFragment : Fragment(R.layout.saved_image_fragment1) {
    lateinit var binding: SavedImageFragment1Binding
    private lateinit var savedImageRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavedImageFragment1Binding.inflate(inflater, container, false)
        val database = activity?.let { Globals.getDatabase(it.baseContext) }

        // Init views
        savedImageRV = binding.savedImageRV
        savedImageRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        val savedImages = database?.getStoredImages()
        val converted = savedImages?.map { ImageApi(imageUri = it.uri) }
        savedImageRV.adapter = converted?.let { savedImageAdapter(context, it) }

        return binding.root
    }
}