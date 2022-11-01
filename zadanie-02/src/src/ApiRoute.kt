package pl.edu.uj.android

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import pl.edu.uj.android.controllers.categories
import pl.edu.uj.android.controllers.products

fun Routing.v1Route() {
    route("/v1") {
        route("/category") {
            install(CORS){
                host("0.0.0.0:5000")
                host("0.0.0.0:3000")
            }
            categories()
        }

        route("/product"){
            install(CORS){
                host("0.0.0.0:5000")
                host("0.0.0.0:3000")
            }
            products()
        }
    }
}