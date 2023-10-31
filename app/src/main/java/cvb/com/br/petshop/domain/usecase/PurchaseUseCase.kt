package cvb.com.br.petshop.domain.usecase

import cvb.com.br.petshop.di.PurchaseRepositoryImplementation
import cvb.com.br.petshop.domain.enum.PurchaseStatusEnum
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseUseCase @Inject constructor(
    @PurchaseRepositoryImplementation private val purchaseRepository: PurchaseRepository
) {

    suspend fun getPurchaseInProgress(): Purchase? {
        return purchaseRepository.getPurchaseInProgress()
    }

    suspend fun finishPurchase(purchase: Purchase): Purchase {
        val finishedPurchase = purchase.copy(
            status = PurchaseStatusEnum.PURCHASE_STATUS_CLOSED.status,
            convertedAt = System.currentTimeMillis()
        )

        purchaseRepository.update(finishedPurchase)

        return finishedPurchase
    }

    suspend fun openPurchase(): Purchase? {
        val newPurchase = createPurchase()
        purchaseRepository.insert(newPurchase)

        return purchaseRepository.getPurchaseInProgress()
    }

    private fun createPurchase() = Purchase(
        id = 0,
        status = PurchaseStatusEnum.PURCHASE_STATUS_OPEN.status,
        createAt = System.currentTimeMillis(),
        convertedAt = 0
    )
}

