package cvb.com.br.petshop.presentation.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.databinding.DialogEditPurchaseQuantityBinding
import cvb.com.br.petshop.util.InformProductQuantity

class EditPurchaseQuantityDialog (
    val activity: Activity,
    private val product: Product,
    private val itemPurchase: ItemPurchase
) : Dialog(activity) {

    private var onSaveEvent: ((itemPurchase: ItemPurchase) -> Unit)? = null

    private lateinit var binding: DialogEditPurchaseQuantityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogEditPurchaseQuantityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        setupListener()

        showInformation()
    }

    private fun setupListener() {
        binding.ivDecrease.setOnClickListener { configPurchaseQuantity(InformProductQuantity.C_DECREASE_QTD) }

        binding.ivIncrease.setOnClickListener { configPurchaseQuantity(InformProductQuantity.C_INCREASE_QTD) }

        binding.btSave.setOnClickListener {
            val newQuantity = binding.tvPurchaseQtd.text.toString().toInt()
            val newItemPurchase = itemPurchase.copy(quantity = newQuantity)
            onSaveEvent?.invoke(newItemPurchase)
        }

        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnSaveEvent(event: (itemPurchase: ItemPurchase) -> Unit) {
        this.onSaveEvent = event
    }

    private fun configPurchaseQuantity(type: Int) {
        InformProductQuantity.process(type, binding.tvPurchaseQtd, null)
    }

    private fun showInformation() {
        binding.tvDesc.text = product.description
        binding.tvPurchaseQtd.text = itemPurchase.quantity.toString()
    }
}
