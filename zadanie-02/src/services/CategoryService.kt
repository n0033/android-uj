package pl.edu.uj.android.services

import org.jetbrains.exposed.sql.transactions.transaction
import pl.edu.uj.android.models.Category
import pl.edu.uj.android.models.CategoryEntity

class CategoryService {

    fun get(id: Int): CategoryEntity? = transaction {
        CategoryEntity.findById(id)
    }

    fun getAll(): Iterable<Category> = transaction {
        CategoryEntity.all().map(CategoryEntity::toCategory)
    }

    fun create(product: Category): CategoryEntity = transaction {
        CategoryEntity.new {
            this.title = product.title
            this.description = product.description
            this.fieldOne = product.fieldOne
            this.fieldTwo = product.fieldTwo
        }
    }

    fun update(id: Int, product: Category) = transaction {
        val dbProduct = CategoryEntity.findById(id) ?: return@transaction null
        dbProduct.title = product.title
        dbProduct.description = product.description
        dbProduct.fieldOne = product.fieldOne
        dbProduct.fieldTwo = product.fieldTwo
        return@transaction dbProduct
    }

    fun delete(id: Int) = transaction {
        CategoryEntity.findById(id)?.delete()
    }

}