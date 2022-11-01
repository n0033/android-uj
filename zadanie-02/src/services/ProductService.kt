package pl.edu.uj.android.services

import org.jetbrains.exposed.sql.transactions.transaction
import pl.edu.uj.android.models.Product
import pl.edu.uj.android.models.ProductEntity

class ProductService {

    fun get(id: Int): ProductEntity? = transaction {
        ProductEntity.findById(id)
    }

    fun getAll(): Iterable<Product> = transaction {
        ProductEntity.all().map(ProductEntity::toProduct)
    }

    fun create(product: Product): ProductEntity = transaction {
        ProductEntity.new {
            this.title = product.title
            this.description = product.description
            this.price = product.price
            this.categoryId = product.categoryId
        }
    }

    fun update(id: Int, product: Product) = transaction {
        val dbProduct = ProductEntity.findById(id) ?: return@transaction null
        dbProduct.title = product.title
        dbProduct.description = product.description
        dbProduct.price = product.price
        dbProduct.categoryId = product.categoryId
        return@transaction dbProduct
    }

    fun delete(id: Int) = transaction {
       ProductEntity.findById(id)?.delete()
    }

}