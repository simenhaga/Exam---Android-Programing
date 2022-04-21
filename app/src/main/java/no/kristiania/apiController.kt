package no.kristiania

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URL

object apiController {
    private lateinit var imageRV: RecyclerView



    fun uploadFileToServer(file: File, callback: (String) -> Unit) {
        Log.d("ApiController", "Uploading $file to server")
        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            //.addMultipartParameter("key", "value")
            .addHeaders("Content-Type", "image/png")
            .setTag("uploadTest")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
            }
            .getAsString(object : StringRequestListener {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(response: String) {
                    Log.d("ApiController", "Uploaded image URL: $response")
                    callback(response)
                }

                override fun onError(anError: ANError?) {
                    print(anError.toString())
                }
            })
    }

    fun reversedImageSearchBing(url: String, callback: (ArrayList<ImageApi>) -> Unit){
        val imageList = ArrayList<ImageApi>()
        val url = URL(url)
        Log.d("apiController: ", "Doing reversed image search on Bing...")

        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/bing?url=$url")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener{
                override fun onResponse(response: String?) {
                    val result: JSONArray = JSONArray(response)

                    for (i in 0 until result.length()){
                        val image: String =
                            (result.get(i) as JSONObject).get("image_link").toString()

                        imageList.add(
                            ImageApi(image)
                        )
                        Log.i(Globals.TAG, "Image Link: " + image + "\n")
                    }
                    callback(imageList)
                }
                override fun onError(anError: ANError?) {
                    print(anError.toString())
                }

            })
    }

    fun reversedImageSearchGoogle(url: String, callback: (ArrayList<ImageApi>) -> Unit){
        val imageList = ArrayList<ImageApi>()
        val url = URL(url)
        Log.d("apiController: ", "Doing reversed image search on Google...")

        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/google?url=$url")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener{
                override fun onResponse(response: String?) {
                    val result: JSONArray = JSONArray(response)

                    for (i in 0 until result.length()){
                        val image: String =
                            (result.get(i) as JSONObject).get("image_link").toString()

                        imageList.add(
                            ImageApi(image)
                        )
                        Log.i(Globals.TAG, "Image Link: " + image + "\n")
                    }
                    callback(imageList)
                }
                override fun onError(anError: ANError?) {
                    print(anError.toString())
                }

            })
    }

    fun reversedImageSearchTinyeye(url: String, callback: (ArrayList<ImageApi>) -> Unit){
        val imageList = ArrayList<ImageApi>()
        val url = URL(url)
        Log.d("apiController: ", "Doing reversed image search on Tinyeye...")

        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/tinyeye?url=$url")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener{
                override fun onResponse(response: String?) {
                    val result: JSONArray = JSONArray(response)

                    for (i in 0 until result.length()){
                        val image: String =
                            (result.get(i) as JSONObject).get("image_link").toString()

                        imageList.add(
                            ImageApi(image)
                        )
                        Log.i(Globals.TAG, "Image Link: " + image + "\n")
                    }
                    callback(imageList)
                }
                override fun onError(anError: ANError?) {
                    print(anError.toString())
                }

            })
    }

}