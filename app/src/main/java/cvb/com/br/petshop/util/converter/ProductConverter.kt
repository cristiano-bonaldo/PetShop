package cvb.com.br.petshop.util.converter

import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.db.entity.EntityProduct
import cvb.com.br.petshop.network.model.ApiProduct

object ProductConverter {

    fun ApiProduct.toProduct(): Product {
        val price = this.amount.replace(',', '.').toDouble()

        return Product(
            this.id.toLong(),
            this.description,
            this.weight,
            this.quantity,
            price,
            this.imageUrl
        )
    }

    fun EntityProduct.toProduct(): Product {
        return Product(
            this.id,
            this.description,
            this.weight,
            this.quantity,
            this.amount,
            this.imageUrl
        )
    }

    fun Product.toEntityProduct(): EntityProduct {
        return EntityProduct(
            this.id,
            this.description,
            this.weight,
            this.quantity,
            this.amount,
            this.imageUrl
        )
    }
}