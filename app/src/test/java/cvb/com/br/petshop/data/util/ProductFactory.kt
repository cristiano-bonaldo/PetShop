package cvb.com.br.petshop.data.util

import cvb.com.br.petshop.domain.model.Product

object ProductFactory {

    fun createProduct(id: Long) = Product(
        id = id,
        description = "Produto-Teste",
        weight = "1Kg",
        quantity = 5,
        amount = 15.35,
        imageUrl = "https://petshop/imagem_url_teste",
    )
}