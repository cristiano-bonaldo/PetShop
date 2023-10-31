package cvb.com.br.petshop.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.databinding.ItemPurchaseItemListBinding
import cvb.com.br.petshop.util.StringUtil
import javax.inject.Inject

class ItemPurchaseAdapter @Inject constructor(private val glide: RequestManager) : RecyclerView.Adapter<ItemPurchaseAdapter.ItemPurchaseViewHolder>() {

    private var onEditEvent: ((itemPurchase: ItemPurchase) -> Unit)? = null
    private var onDeleteEvent: ((itemPurchase: ItemPurchase) -> Unit)? = null

    private var listItemPurchase: List<ItemPurchase> = listOf()


    fun submitList(listItemPurchase: List<ItemPurchase>) {
        this.listItemPurchase = listItemPurchase
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPurchaseViewHolder {
        val binding = ItemPurchaseItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPurchaseViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ItemPurchaseViewHolder, position: Int) {
        val itemPurchase = listItemPurchase[position]
        holder.bind(itemPurchase)
    }

    override fun getItemCount(): Int {
        return listItemPurchase.size
    }

    fun setOnDeleteEvent(deleteEvent: (itemPurchase: ItemPurchase) -> Unit) {
        this.onDeleteEvent = deleteEvent
    }

    fun setOnEditEvent(editEvent: (itemPurchase: ItemPurchase) -> Unit) {
        this.onEditEvent = editEvent
    }

    //--------------------------

    inner class ItemPurchaseViewHolder(private val binding: ItemPurchaseItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemPurchase: ItemPurchase) {
            binding.apply {
                this.ivDelete.setOnClickListener {
                    onDeleteEvent?.invoke(itemPurchase)
                }

                this.ivEdit.setOnClickListener {
                    onEditEvent?.invoke(itemPurchase)
                }

                this.tvDesc.text = itemPurchase.prodDesc
                this.tvPrice.text = StringUtil.formatValue(itemPurchase.prodPrice)
                this.tvQuantity.text = itemPurchase.quantity.toString()

                glide.load(itemPurchase.prodUrl).into(this.ivProd)
            }
        }
    }
}