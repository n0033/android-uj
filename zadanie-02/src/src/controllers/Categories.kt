package pl.edu.uj.android.controllers


import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import pl.edu.uj.android.models.Category
import pl.edu.uj.android.services.CategoryService

fun Route.categories(){
    val categoryService = CategoryService()

    get("") {
        val products = categoryService.getAll()
        call.respond(products)
    }

    get("/{id}") {
        val categoryId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        val categoryEntity = categoryService.get(categoryId) ?: throw NotFoundException()
        call.respond(categoryEntity.toCategory())
    }

    post("") {
        val categoryInRequest = call.receive<Category>()
        val categoryEntity = categoryService.create(categoryInRequest)
        call.respond(categoryEntity.toCategory())
    }

    put("/{id}") {
        val categoryId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        val updateData = call.receive<Category>()
        val categoryEntity = categoryService.update(categoryId, updateData) ?: throw NotFoundException()
        call.respond(categoryEntity.toCategory())

    }

    delete("/{id}") {
        val categoryId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        categoryService.delete(categoryId) ?: throw NotFoundException()
        call.response.status(HttpStatusCode.NoContent)
    }
}
