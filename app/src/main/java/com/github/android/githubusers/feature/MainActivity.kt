package com.github.android.githubusers.feature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.android.githubusers.GithubApplication
import com.github.android.githubusers.R
import com.github.android.githubusers.core.networking.Response
import com.github.android.githubusers.feature.di.DaggerGitHubComponent
import com.github.android.githubusers.feature.model.GithubResponse
import com.github.android.githubusers.feature.viewmodel.GithubViewModel
import com.github.android.githubusers.feature.viewmodel.GithubViewModelFactory
import javax.inject.Inject
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.github.android.githubusers.databinding.ActivityMainBinding
import com.github.android.githubusers.databinding.GithubItemBinding
import com.github.android.githubusers.feature.model.Item
import android.support.v7.widget.DividerItemDecoration
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.CropCircleTransformation


class MainActivity : AppCompatActivity() {

    private val mGiftCardListComponent by lazy {
        DaggerGitHubComponent.builder().coreComponent(GithubApplication.coreComponent).build()
    }

    @Inject
    lateinit var mViewModelFactory: GithubViewModelFactory

    private val mViewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(GithubViewModel::class.java)
    }

    private var mBinding:ActivityMainBinding?=null
    private var mUserList:List<Item>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mGiftCardListComponent.inject(this)
        mViewModel.mGetUserLiveData.observe(this, Observer<Response<GithubResponse>> {
            when(it){
                is Response.Progress -> mBinding?.progressBar?.visibility = if(it.loading) View.VISIBLE else View.GONE
                is Response.Success -> handleSuccess(it.data)
                is Response.Failure -> Toast.makeText(this, it.e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mBinding?.recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding?.recyclerView?.addItemDecoration(itemDecorator)
    }

    private fun handleSuccess(data: GithubResponse) {
        mUserList = data.items
        if (mUserList!= null){
            mBinding?.recyclerView?.adapter = GithubAdapter(this, mUserList!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val search = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(search) as SearchView
        search(searchView)
        return true
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mViewModel.searchUser(newText)
                return true
            }
        })
    }


    inner class GithubAdapter(private var context:Context, private var items:List<Item>): RecyclerView.Adapter<GitHubViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubViewHolder {
            var binding : GithubItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.github_item, parent, false)
            return GitHubViewHolder(binding)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: GitHubViewHolder, position: Int) {
            holder.binding.item = items[position]
            showImage(items[position].avatarUrl, holder.binding.image)
        }

        private fun showImage(url: String, view: ImageView){
            var requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .apply(bitmapTransform(CropCircleTransformation()))
                    .into(view)
        }
    }

    inner class GitHubViewHolder(var binding: GithubItemBinding):RecyclerView.ViewHolder(binding.root)

}
