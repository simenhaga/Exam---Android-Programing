package no.kristiania

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import no.kristiania.databinding.SavedImageFragmentBinding

class SavedImageFragment : Fragment(R.layout.saved_image_fragment) {
    lateinit var binding: SavedImageFragmentBinding
    private lateinit var savedImageRV: RecyclerView
    private lateinit var savedResultImageRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SavedImageFragmentBinding.inflate(inflater, container, false)

        // Init views
        savedImageRV = binding.savedImageRV
        savedResultImageRV = binding.resultImageRV

        savedImageRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        savedResultImageRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)

        updateResultRV()
        updateSavedRV()

        return binding.root
    }

    fun updateResultRV(){
        val database = activity?.let { Globals.getDatabase(it.baseContext) }

        val savedResultImages = database?.getResultImages()
        savedResultImageRV.adapter = savedResultImages?.let { SavedResultsAdapter(context, this, it ) }
    }

    fun updateSavedRV(){
        val database = activity?.let { Globals.getDatabase(it.baseContext) }

        val savedImages = database?.getStoredImages()
        savedImageRV.adapter = savedImages?.let { SavedImageAdapter(context, this, it) }
    }

}