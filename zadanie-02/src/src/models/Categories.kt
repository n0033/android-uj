package pl.edu.uj.android.models


import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable() {
    val title = varchar("title", 50) // Column<String>
    val description = varchar("description", 1024) // Column<String>
    val fieldOne = varchar("field_one", 512)
    val fieldTwo = varchar("field_two", 512)
}

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(Categories)

    var title by Categories.title
    var description by Categories.description
    var fieldOne by Categories.fieldOne
    var fieldTwo by Categories.fieldTwo

    fun toCategory() = Category(id.value, title, description, fieldOne, fieldTwo)
}


data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val fieldOne: String,
    val fieldTwo: String
)