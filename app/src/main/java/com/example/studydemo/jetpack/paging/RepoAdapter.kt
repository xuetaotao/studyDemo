package com.example.studydemo.jetpack.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.studydemo.databinding.ItemRepoBinding

//必须继承自PagingDataAdapter
//这里最特殊的地方就是要提供一个COMPARATOR。因为Paging 3在内部会使用DiffUtil来管理数据变化，所以这个COMPARATOR是必须的。
//除此之外，我们并不需要传递数据源给到父类，因为数据源是由Paging 3在内部自己管理的。同时也不需要重写getItemCount()函数了，原因也是相同的，有多少条数据Paging 3自己就能够知道。
class RepoAdapter : PagingDataAdapter<Repo, RepoAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        if (repo != null) {
            holder.name.text = repo.name
            holder.description.text = repo.description
            holder.starCount.text = repo.starCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.nameText
        val description = binding.descriptionText
        val starCount = binding.starCountText
    }
}