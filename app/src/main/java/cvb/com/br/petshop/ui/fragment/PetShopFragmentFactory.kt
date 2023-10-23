package cvb.com.br.petshop.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import cvb.com.br.petshop.ui.adapter.ItemPurchaseAdapter
import cvb.com.br.petshop.ui.adapter.ProductAdapter
import javax.inject.Inject

class PetShopFragmentFactory @Inject constructor(
    private val glide: RequestManager,
    private val productAdapter: ProductAdapter,
    private val itemPurchaseAdapter: ItemPurchaseAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className) {
            ListProductFragment::class.java.name -> { ListProductFragment(productAdapter) }
            ListPurchaseInProgressFragment::class.java.name -> { ListPurchaseInProgressFragment(itemPurchaseAdapter) }
            ProductDetailFragment::class.java.name -> { ProductDetailFragment(glide) }
            else -> { super.instantiate(classLoader, className) }
        }
    }

}