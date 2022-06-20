package submission3final.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.submission3final.githubuserapp.R
import submission3final.SettingActivity
import submission3final.view.adapter.UserAdapter
import submission3final.model.response.UsersResponse
import submission3final.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private lateinit var tvNone: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchUser: SearchView
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNone = findViewById(R.id.tv_none)
        progressBar = findViewById(R.id.progressBar)
        searchUser = findViewById(R.id.sv_user)
        rvUser = findViewById(R.id.rv_user)
        rvUser.setHasFixedSize(true)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        initData()
        initSearch()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            R.id.favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun initDataSearch(type: String) {
        showLoading(true)
        userViewModel.setSearchUser(type, this@MainActivity)
        userViewModel.getSearchUser().observe(this) {

            if (it!!.isNotEmpty()) {
                showLoading(false)
                rvUser.visibility = View.VISIBLE
                tvNone.visibility = View.GONE
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    rvUser.layoutManager = GridLayoutManager(applicationContext, 2)
                } else {
                    rvUser.layoutManager = LinearLayoutManager(applicationContext)
                }

                val userAdapter = UserAdapter(applicationContext, it)
                rvUser.adapter = userAdapter
                userAdapter.notifyDataSetChanged()

                userAdapter.setOnItemClickCallback(object :
                    UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: UsersResponse?) {
                        val intent =
                            Intent(this@MainActivity, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                        startActivity(intent)
                    }
                })

            } else {
                showLoading(false)
                tvNone.visibility = View.VISIBLE
                rvUser.visibility = View.GONE
            }

        }
    }

    private fun initSearch() {

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchUser.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchUser.queryHint = resources.getString(R.string.search_hint)
        searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    initDataSearch(query)
                } else {
                    initData()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun initData() {
        showLoading(true)
        userViewModel.showUserList(this)
        userViewModel.getUser().observe(this) {
            if (it != null) {
                showLoading(false)
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    rvUser.layoutManager = GridLayoutManager(applicationContext, 2)
                } else {
                    rvUser.layoutManager = LinearLayoutManager(applicationContext)
                }

                val userAdapter = UserAdapter(applicationContext, it)
                rvUser.adapter = userAdapter

                userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: UsersResponse?) {
                        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                        startActivity(intent)
                    }
                })
            } else {
                showLoading(false)
            }

        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}