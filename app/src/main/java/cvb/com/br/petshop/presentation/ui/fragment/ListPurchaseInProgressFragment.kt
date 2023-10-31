package cvb.com.br.petshop.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cvb.com.br.petshop.R
import cvb.com.br.petshop.databinding.FragmentListPurchaseInProgressBinding
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.presentation.ui.adapter.ItemPurchaseAdapter
import cvb.com.br.petshop.presentation.ui.dialog.EditPurchaseQuantityDialog
import cvb.com.br.petshop.presentation.viewmodel.FragListPurchaseInProgressViewModel
import cvb.com.br.petshop.presentation.viewmodel.status.CrudStatus
import cvb.com.br.petshop.presentation.viewmodel.status.LoadPurchaseStatus
import cvb.com.br.petshop.util.DataShareUtil
import cvb.com.br.petshop.util.DateTimeUtil
import cvb.com.br.petshop.util.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListPurchaseInProgressFragment : Fragment(R.layout.fragment_list_purchase_in_progress) {

    companion object {
        private val TAG = ListPurchaseInProgressFragment::class.java.simpleName
    }

    @Inject
    lateinit var dialogUtil: DialogUtil

    @Inject
    lateinit var dataShareUtil: DataShareUtil

    @Inject
    lateinit var itemPurchaseAdapter: ItemPurchaseAdapter

    private var _binding: FragmentListPurchaseInProgressBinding? = null
    private val binding: FragmentListPurchaseInProgressBinding get() = _binding!!

    private val viewModel by viewModels<FragListPurchaseInProgressViewModel>()

    private var editPurchaseQuantityDialog: EditPurchaseQuantityDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListPurchaseInProgressBinding.bind(view)

        setupObserver()

        setupRecycler()

        setupListener()

        viewModel.loadPurchase()
    }

    private fun setupListener() {
        binding.btPurchase.setOnClickListener { showFinishPurchaseDialog() }
    }

    private fun showFinishPurchaseDialog() {
        val total = binding.tvTotal.text

        val btFinish = { viewModel.finishPurchase() }

        dialogUtil.showDialog(
            getString(R.string.frag_purchase_in_progress_finish_purchase_title),
            getString(R.string.frag_purchase_in_progress_finish_purchase_msg, total),
            getString(R.string.bt_ok),
            btFinish,
            getString(R.string.bt_cancelar),
            null
        )
    }

    private fun editListener(itemPurchase: ItemPurchase) {
        val product = viewModel.findProductById(itemPurchase.prodId)
        product?.let { prod -> openEditDialog(prod, itemPurchase) }
    }

    private fun deleteListener(itemPurchase: ItemPurchase) {
        val btOk = { viewModel.deleteItemPurchase(itemPurchase) }

        dialogUtil.showDialog(
            getString(R.string.frag_purchase_in_progress_delete_title),
            getString(R.string.frag_purchase_in_progress_delete_msg, itemPurchase.prodDesc),
            getString(R.string.bt_ok),
            btOk,
            getString(R.string.bt_cancelar),
            null)
    }

    private fun openEditDialog(product: Product, itemPurchase: ItemPurchase) {
        editPurchaseQuantityDialog = EditPurchaseQuantityDialog (requireActivity(), product, itemPurchase)
        editPurchaseQuantityDialog?.setOnSaveEvent { item -> editPurchaseQuantityListener(item)  }
        editPurchaseQuantityDialog?.show()
    }

    private fun editPurchaseQuantityListener(itemPurchase: ItemPurchase) {
        viewModel.updateItemPurchase(itemPurchase)
    }

    private fun setupRecycler() {
        itemPurchaseAdapter.setOnEditEvent { itemPurchase -> editListener(itemPurchase) }
        itemPurchaseAdapter.setOnDeleteEvent { itemPurchase -> deleteListener(itemPurchase) }

        binding.recycler.adapter = itemPurchaseAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObserver() {
        viewModel.loadPurchaseStatus.observe(viewLifecycleOwner, this::onLoadPurchaseStatus)

        viewModel.updateItemPurchaseStatus.observe(viewLifecycleOwner, this::onUpdateOrDeletePurchaseStatus)

        viewModel.deleteItemPurchaseStatus.observe(viewLifecycleOwner, this::onUpdateOrDeletePurchaseStatus)

        viewModel.finishPurchaseStatus.observe(viewLifecycleOwner, this::onFinishPurchaseStatus)

        viewModel.updateTotalPurchase.observe(viewLifecycleOwner, this::onUpdateTotalPurchase)
    }

    private fun showPurchaseData(purchase: Purchase) {
        binding.tvPurchaseID.text = purchase.id.toString()
        binding.tvPurchaseDate.text = DateTimeUtil.convertTimeMillisToString(purchase.createAt)
    }

    private fun onLoadPurchaseStatus(loadPurchaseStatus: LoadPurchaseStatus) {
        when (loadPurchaseStatus) {
            is LoadPurchaseStatus.Loading -> {
                Log.i(TAG, "onLoadPurchaseStatus::Status=Loading")
            }

            is LoadPurchaseStatus.Error -> {
                val errorMessage = loadPurchaseStatus.error.message ?: "-"
                Log.i(TAG, "onLoadPurchaseStatus::Status=Success Message: $errorMessage")
                handleLoadPurchaseError(errorMessage) }

            is LoadPurchaseStatus.Success -> {
                Log.i(TAG, "onLoadPurchaseStatus::Status=Success")
                handleLoadPurchaseSuccess(loadPurchaseStatus.listPurchase)
            }
        }
    }

    private fun handleLoadPurchaseError(errorMessage: String) {
        val btRetry = { viewModel.loadPurchase() }
        dialogUtil.showErrorRetryDialog(errorMessage, btRetry)
    }

    private fun handleLoadPurchaseSuccess(listPurchase: List<Purchase?>) {
        var purchaseInProgress: Purchase? = null
        if (listPurchase.isNotEmpty()) {
            purchaseInProgress = listPurchase[0]
        }

        val isValidPurchase = (purchaseInProgress != null) && purchaseInProgress.listItemPurchase.isNotEmpty()

        if (isValidPurchase) {
            purchaseInProgress?.let { purchase ->
                showPurchaseData(purchase)
                itemPurchaseAdapter.submitList(purchase.listItemPurchase)
            }
        }

        configPurchaseVisibility(isValidPurchase)
    }

    private fun configPurchaseVisibility(isValidPurchase: Boolean) {
        binding.clBase.visibility = if (isValidPurchase) { View.VISIBLE } else { View.GONE }
        binding.tvEmptyPurchase.visibility = if (isValidPurchase) { View.GONE } else { View.VISIBLE }
    }

    private fun onUpdateTotalPurchase(total: String) {
        binding.tvTotal.text = getString(R.string.frag_purchase_in_progress_total, total)
    }

    private fun onUpdateOrDeletePurchaseStatus(crudStatus: CrudStatus) {
        when (crudStatus) {
            is CrudStatus.Loading -> {
                Log.i(TAG, "onUpdateOrDeletePurchaseStatus::Status=Loading")
            }

            is CrudStatus.Error -> {
                val errorMessage = crudStatus.error.message ?: "-"
                Log.i(TAG, "onUpdateOrDeletePurchaseStatus::Status=Error Message: $errorMessage")

                handleCrudError(errorMessage)
            }

            is CrudStatus.Success -> {
                Log.i(TAG, "onUpdateOrDeletePurchaseStatus::Status=Success")

                handleCrudSuccess()
            }
        }
    }

    private fun handleCrudError(errorMessage: String) {
        val btRetry = { viewModel.execRetryEvent() }
        dialogUtil.showErrorRetryDialog(errorMessage, btRetry)
    }

    private fun handleCrudSuccess() {
        val listItemPurchase = viewModel.getCurrentListItemPurchase()

        var newListItemPurchase = listOf<ItemPurchase>()
        listItemPurchase?.let { list -> newListItemPurchase = list.toList() }

        configPurchaseVisibility(newListItemPurchase.isNotEmpty())

        itemPurchaseAdapter.submitList(newListItemPurchase)

        editPurchaseQuantityDialog?.dismiss()
    }

    private fun onFinishPurchaseStatus(crudStatus: CrudStatus) {
        when (crudStatus) {
            is CrudStatus.Loading -> {
                Log.i(TAG, "onFinishPurchaseStatus::Status=Loading")
            }

            is CrudStatus.Error -> {
                val errorMessage = crudStatus.error.message ?: "-"
                Log.i(TAG, "onFinishPurchaseStatus::Status=Error Message: $errorMessage")

                handleCrudError(errorMessage)
            }

            is CrudStatus.Success -> {
                Log.i(TAG, "onFinishPurchaseStatus::Status=Success")

                handleFinishPurchaseSuccess()
            }
        }
    }

    private fun handleFinishPurchaseSuccess() {
        val purchaseInfo = viewModel.getPurchaseInformation()

        val optionChoice: ((Int) -> Unit) = { selectedOption ->
            when (selectedOption) {
                0 -> dataShareUtil.sendMessageToWhatsApp(purchaseInfo)
                1 -> dataShareUtil.sendMessageToEmail(purchaseInfo)
                else -> dataShareUtil.shareData(purchaseInfo)
            }
        }

        val btClose: (() -> Unit)  = { findNavController().popBackStack() }

        dialogUtil.showDataShareDialog(optionChoice, btClose)
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}