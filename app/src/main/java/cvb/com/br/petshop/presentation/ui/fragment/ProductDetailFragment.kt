package cvb.com.br.petshop.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import cvb.com.br.petshop.R
import cvb.com.br.petshop.databinding.FragmentProductDetailBinding
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.presentation.viewmodel.FragProductDetailViewModel
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.DialogUtil
import cvb.com.br.petshop.util.InformProductQuantity
import cvb.com.br.petshop.util.StringUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    companion object {
        private val TAG = ProductDetailFragment::class.java.simpleName
    }

    @Inject
    lateinit var dialogUtil: DialogUtil

    @Inject
    lateinit var glide: RequestManager

    private var _binding: FragmentProductDetailBinding? = null
    private val binding: FragmentProductDetailBinding get() = _binding!!

    private val viewModel by viewModels<FragProductDetailViewModel>()

    private val args: ProductDetailFragmentArgs by navArgs()

    private lateinit var product: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProductDetailBinding.bind(view)

        product = args.PRODUCT

        setupObserver()
        viewModel.loadItemPurchase(product)

        setListener()

        showProductData()
    }

    private fun setListener() {
        binding.apply {
            this.ivDecrease.setOnClickListener { configPurchaseQuantity(InformProductQuantity.C_DECREASE_QTD) }

            this.ivIncrease.setOnClickListener { configPurchaseQuantity(InformProductQuantity.C_INCREASE_QTD) }

            this.btAddProduct.setOnClickListener { addProductListener() }

            this.btPurchase.setOnClickListener { viewPurchaseListener() }
        }
    }

    private fun addProductListener() {
        val qtd = binding.tvPurchaseQtd.text.toString().toInt()

        if (qtd == 0) {
            val btOK = {}
            dialogUtil.showErrorDialog(getString(R.string.frag_product_detail_erro_qtd), btOK)
            return
        }

        viewModel.addItemPurchase(product, qtd)
    }

    private fun viewPurchaseListener() {
        val navTo = ProductDetailFragmentDirections.actionProductDetailFragmentToListPurchaseInProgressFragment()
        findNavController().navigate(navTo)
    }

    private fun setupObserver() {
        viewModel.loadItemPurchaseStatus.observe(viewLifecycleOwner, this::onLoadItemPurchaseStatus)
        viewModel.addPurchaseStatus.observe(viewLifecycleOwner, this::onAddPurchaseStatus)
        viewModel.totalPurchase.observe(viewLifecycleOwner, this::configTotalPurchase)
    }

    private fun onLoadItemPurchaseStatus(uiStatus: UIStatus<ItemPurchase>) {
        when (uiStatus) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onLoadItemPurchaseStatus::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = uiStatus.error.message ?: "-"
                Log.i(TAG, "onLoadItemPurchaseStatus::Status=Error Message: $errorMessage")

                handleLoadItemPurchaseError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onLoadItemPurchaseStatus::Status=Success")

                val itemPurchase = uiStatus.result
                setItemPurchaseInfo(itemPurchase)
            }
        }
    }

    private fun handleLoadItemPurchaseError(errorMessage: String) {
        val btRetry = { viewModel.loadItemPurchase(product) }
        dialogUtil.showErrorRetryDialog(errorMessage, btRetry)
    }

    private fun onAddPurchaseStatus(uiStatus: UIStatus<Nothing>) {
        when (uiStatus) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onAddPurchaseStatus::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = uiStatus.error.message ?: "-"
                Log.i(TAG, "onAddPurchaseStatus::Status=Error Message: $errorMessage")

                handleCrudError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onAddPurchaseStatus::Status=Success")

                findNavController().popBackStack()
            }
        }
    }

    private fun handleCrudError(errorMessage: String) {
        val btRetry = { viewModel.execRetryEvent() }
        dialogUtil.showErrorRetryDialog(errorMessage, btRetry)
    }

    private fun showProductData() {
        binding.apply {
            glide.load(product.imageUrl).into(this.ivProd)

            this.tvDesc.text = product.description
            this.tvPrice.text = StringUtil.formatValue(product.amount)
            this.tvWeight.text = product.weight
            this.tvQuantity.text = product.quantity.toString()
        }
    }

    private fun setItemPurchaseInfo(itemPurchase: ItemPurchase?) {
        var qtd = 0

        itemPurchase?.let { item -> qtd = item.quantity }

        binding.tvPurchaseQtd.text = qtd.toString()

        viewModel.getTotalPurchase(product, qtd)
    }

    private fun configPurchaseQuantity(type: Int) {
        InformProductQuantity.process(type, binding.tvPurchaseQtd) { qtd ->
            viewModel.getTotalPurchase(product, qtd) }
    }

    private fun configTotalPurchase(total: Double) {
        binding.tvTotal.text = getString(R.string.frag_product_detail_total, StringUtil.formatValue(total))
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}