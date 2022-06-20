package com.example.githubuserapi.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.views.UserAdapter
import com.example.githubuserapi.retrofit.Client
import com.example.githubuserapi.model.User
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail_follow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailFollowFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "arg username"
        private const val ARG_TAB = "arg tab"

        fun newInstance(username : String?, tab : String?) : DetailFollowFragment {
            val fragment =
                DetailFollowFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putString(ARG_TAB, tab)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var githubUser = arrayListOf<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var username = "username"
        var tab = "tab"
        if (arguments != null){
            username = arguments?.getString(ARG_USERNAME, "username") as String
            tab = arguments?.getString(ARG_TAB, "tab") as String
        }
        initRecyclerView()
        getFollowApi(username, tab)
    }

    private fun initRecyclerView(){
        recycler_view_fragment.layoutManager = LinearLayoutManager(activity)
        recycler_view_fragment.setHasFixedSize(true)
        val listUserAdapter = UserAdapter(githubUser)
        recycler_view_fragment.adapter = listUserAdapter
    }

    private fun getFollowApi(username : String, follow : String){

        val client = Client.getApiServices().getDetailFollow(username, follow)
        client.enqueue(object : Callback<ArrayList<User>>{

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                try {
                    val dataArray = response.body() as ArrayList
                    for (data in dataArray){
                        githubUser.add(data)
                    }
                    initRecyclerView()
                    progressBarFragment.visibility = View.INVISIBLE
                } catch (e : Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(activity, "Gagal, silahkan coba lagi", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBarFragment.visibility = View.INVISIBLE


            }
        })
    }
}