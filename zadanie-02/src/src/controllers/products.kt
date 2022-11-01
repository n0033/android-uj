package pl.edu.uj.android.controllers


import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import pl.edu.uj.android.models.*
import pl.edu.uj.android.services.ProductService

fun Route.products(){

    val productService = ProductService()

    get("") {
        val products = productService.getAll()
        call.respond(products)
    }

    get("/{id}") {
        val productId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        val productEntity = productService.get(productId) ?: throw NotFoundException()
        call.respond(productEntity.toProduct())
    }

    post("") {
        val productInRequest = call.receive<Product>()
        val productEntity = productService.create(productInRequest)
        call.respond(productEntity.toProduct())
    }

    put("/{id}") {
        val productId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        val updateData = call.receive<Product>()
        val productEntity = productService.update(productId, updateData) ?: throw NotFoundException()
        call.respond(productEntity.toProduct())

    }

    delete("/{id}") {
        val productId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        productService.delete(productId) ?: throw NotFoundException()
        call.response.status(HttpStatusCode.NoContent)
    }
}
