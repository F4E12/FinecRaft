package com.example.qualif.fragment


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.qualif.adapter.ItemAdapter
import com.example.qualif.database.DatabaseHelper
import com.example.qualif.databinding.FragmentItemBinding
import com.example.qualif.model.Item
import org.json.JSONException

class ItemFragment : Fragment() {
    private lateinit var binding : FragmentItemBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        requestQueue = Volley.newRequestQueue(context)

        setUpRecycler()
        val url = "https://api.npoint.io/507e2dd338984672df78"
        requestQueue = Volley.newRequestQueue(requireContext())  // Ensure this is initialized

        if (!isInternetAvailable()) {
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                Log.d("API Response", response.toString()) // Debugging log

                if (!response.has("items")) {
                    Toast.makeText(context, "Invalid API response!", Toast.LENGTH_SHORT).show()
                    return@JsonObjectRequest
                }

                databaseHelper.deleteAllItems()
                val itemArray = response.getJSONArray("items")
                for (i in 0 until itemArray.length()) {
                    val itemObject = itemArray.getJSONObject(i)

                    // Check for missing keys
                    if (!itemObject.has("name") || !itemObject.has("description") ||
                        !itemObject.has("price") || !itemObject.has("compatible_minecraft_version")) {
                        continue
                    }

                    val item = Item(
                        0,
                        itemObject.getString("name"),
                        itemObject.getString("description"),
                        itemObject.optDouble("price", 0.0), // Default price = 0.0 if missing
                        itemObject.getString("compatible_minecraft_version")
                    )
                    databaseHelper.insertItem(item)
                }

                // Ensure UI is loaded before calling
                if (isAdded && context != null) {
                    setUpRecycler()
                }
            } catch (e: JSONException) {
                Toast.makeText(context, "JSON Parsing Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("JSON Error", e.message.toString())
            }
        }, { error ->
            Log.e("Volley error", error.toString())
            Toast.makeText(context, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        requestQueue.add(request)

        return binding.root
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun setUpRecycler() {
        val items = databaseHelper.getItems()
        if (items.isEmpty()) {
            Toast.makeText(context, "No items available!", Toast.LENGTH_SHORT).show()
        }
        itemAdapter = ItemAdapter(requireContext(), items)

        binding.rvItemList.adapter = itemAdapter

        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}