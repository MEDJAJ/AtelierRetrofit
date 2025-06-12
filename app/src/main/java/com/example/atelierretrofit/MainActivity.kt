package com.example.atelierretrofit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atelierretrofit.Adapter.PostAdapter
import com.example.atelierretrofit.ApiService.RetrofitClient
import com.example.atelierretrofit.Model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var postAdapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        postAdapter = PostAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        fetchAllPosts()

        searchButton.setOnClickListener {
            val id = searchEditText.text.toString().toIntOrNull()
            if (id != null) {
                searchPostById(id)
            } else {
                Toast.makeText(this, "Entrer un ID valide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAllPosts() {
        RetrofitClient.api.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    postAdapter.updatePosts(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun searchPostById(id: Int) {
        RetrofitClient.api.getPostById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        postAdapter.updatePosts(listOf(it))
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Post introuvable", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
