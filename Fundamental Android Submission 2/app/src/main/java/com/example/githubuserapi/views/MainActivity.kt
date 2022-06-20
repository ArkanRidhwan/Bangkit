package com.example.githubuserapi.views

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapi.R
import com.example.githubuserapi.retrofit.Client
import com.example.githubuserapi.databinding.ActivityMainBinding
import com.example.githubuserapi.model.User
import com.example.githubuserapi.model.ResponseUser
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var rvUser : RecyclerView
    private lateinit var userAdapter : UserAdapter
    private var listUser = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = recycler_view
        getUserApi()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)
        userAdapter = UserAdapter(listUser)
        rvUser.adapter = userAdapter
        rvUser.setHasFixedSize(true)
    }

    private fun getUserApi(){
        val client = Client.getApiServices().getApi()
        client.enqueue(object : Callback<List<User>>{

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val githubUser = response.body()
                userAdapter.setData(githubUser as ArrayList<User>)
                progressBar.visibility = View.INVISIBLE
            }

        })
    }

    private fun searchUser(username : String?){

        val client = Client.getApiServices().getSearchUser(username)
        client.enqueue(object : Callback<ResponseUser>{

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                try {
                    val dataArray = response.body()?.items as ArrayList<User>
                    for (data in dataArray){
                        listUser.add(data)
                    }
                    recycler_view.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    initRecyclerView()
                } catch (e : Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    recycler_view.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                    listUser.clear()
                    searchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}