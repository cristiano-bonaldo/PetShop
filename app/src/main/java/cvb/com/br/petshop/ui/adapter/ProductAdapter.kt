package cvb.com.br.petshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.databinding.ProductItemListBinding
import cvb.com.br.petshop.util.StringUtil
import javax.inject.Inject

class ProductAdapter @Inject constructor(private val glide: RequestManager) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffUtil) {

    private var onSelectProductEvent: ((product: Product) -> Unit)? = null

    object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    inner class ProductViewHolder(private val binding: ProductItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                this.root.setOnClickListener {
                    onSelectProductEvent?.invoke(product)
                }

                this.tvDesc.text = product.description
                this.tvPrice.text = StringUtil.formatValue(product.amount)

                glide.load(product.imageUrl).into(this.ivProd)
            }
        }

    }

    // ===========================

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    fun setOnSelectProductEvent(onSelectProductEvent: (product: Product) -> Unit) {
        this.onSelectProductEvent = onSelectProductEvent
    }
}