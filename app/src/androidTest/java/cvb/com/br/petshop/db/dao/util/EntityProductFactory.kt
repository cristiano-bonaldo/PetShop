package cvb.com.br.petshop.db.dao.util

import cvb.com.br.petshop.db.entity.EntityProduct

object EntityProductFactory {

    fun createProduct(id: Long) = EntityProduct(
        id = id,
        description = "Produto-Teste",
        weight = "1Kg",
        quantity = 5,
        amount = 15.35,
        imageUrl = "https://petshop/imagem_url_teste",
        createdAt = 1698002831000
    )
}