package com.example.dogedex.ui.dog.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dogedex.R
import com.example.dogedex.databinding.DogListItemBinding
import com.example.dogedex.domain.model.DogModel

class DogAdapter(
    private val dataSource: List<DogModel>,
    val context: Context,
    var onListItemClickListener: ((dogModel: DogModel) -> Unit)
)  : RecyclerView.Adapter<DogAdapter.ViewHolder>(){

    inner class ViewHolder(
        private var binding: DogListItemBinding,
        private var ctx: Context,
        var onListItemClickListener: ((dogModel: DogModel) -> Unit)
    ) : RecyclerView.ViewHolder(binding!!.root) {
        var root: ConstraintLayout = binding.dogListItemLayout

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(dataSource: DogModel){

            if(dataSource.inCollection){
                binding.tvDogName.text = dataSource.index.toString()

            }else{

                Glide.with(context)
                    .load(dataSource.image_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.dogImage)
                binding.tvDogName.text = dataSource.name_es
            }
            binding.dogListItemLayout.setBackgroundResource(R.drawable.bg_item_dog_list)

            binding.apply {

                dogListItemLayout.setOnClickListener{
                    binding.dogListItemLayout.setBackgroundResource(R.drawable.bg_item_dog_list)
                    onListItemClickListener.invoke(dataSource)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = DogListItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding, viewGroup.context, onListItemClickListener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dogModel: DogModel = dataSource[position]
        viewHolder.bind(dogModel)
    }

    override fun getItemCount(): Int = dataSource.size
}