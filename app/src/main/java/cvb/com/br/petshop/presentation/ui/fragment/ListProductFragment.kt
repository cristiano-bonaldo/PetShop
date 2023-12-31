package cvb.com.br.petshop.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cvb.com.br.petshop.R
import cvb.com.br.petshop.databinding.FragmentListProductBinding
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.presentation.ui.adapter.ProductAdapter
import cvb.com.br.petshop.presentation.viewmodel.FragListProductViewModel
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class ListProductFragment : Fragment(R.layout.fragment_list_product) {

    @Inject
    lateinit var dialogUtil: DialogUtil

    @Inject
    lateinit var productAdapter: ProductAdapter

    companion object {
        private val TAG = ListProductFragment::class.java.simpleName
    }

    private var _binding: FragmentListProductBinding? = null
    private val binding: FragmentListProductBinding get() = _binding!!

    private val viewModel by viewModels<FragListProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListProductBinding.bind(view)

        setupObserver()

        setupListener()

        setupRecycler()

        viewModel.loadProducts()
    }

    private fun setupListener() {
        binding.btPurchase.setOnClickListener { navigateToPurchaseInProgress() }
    }

    private fun navigateToProductDetail(product: Product) {
        val navTo = ListProductFragmentDirections.actionListProductFragmentToProductDetailFragment(product)
        findNavController().navigate(navTo)
    }

    private fun navigateToPurchaseInProgress() {
        val navTo = ListProductFragmentDirections.actionListProductFragmentToListPurchaseInProgressFragment()
        findNavController().navigate(navTo)
    }

    private fun setupRecycler() {
        productAdapter.setOnSelectProductEvent { product -> navigateToProductDetail(product) }

        binding.recycler.adapter = productAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObserver() {
        viewModel.loadProductStatus.observe(viewLifecycleOwner, this::onLoadProductStatus)

        viewModel.isLoading.observe(viewLifecycleOwner, this::onLoadingStatus)
    }

    private fun onLoadingStatus(isLoading: Boolean) {
        binding.vgLoad.visibility = if (isLoading) { View.VISIBLE } else { View.GONE }
    }

    private fun onLoadProductStatus(loadProductStatus: UIStatus<List<Product>>) {
        when (loadProductStatus) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onLoadProductStatus::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = loadProductStatus.error.message ?: "-"
                Log.i(TAG, "onLoadProductStatus::Status=Error Message: $errorMessage")

                val btRetry = { viewModel.loadProducts() }
                val btCancel = { exitProcess(0) }
                dialogUtil.showErrorRetryDialog(errorMessage, btRetry, btCancel)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onLoadProductStatus::Status=Success")

                val listProduct = loadProductStatus.result
                productAdapter.submitList(listProduct)
            }
        }
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}