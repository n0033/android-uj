package pl.edu.uj.android

import io.ktor.routing.*
import pl.edu.uj.android.controllers.categories
import pl.edu.uj.android.controllers.products

fun Routing.apiRoute() {
    route("/v1") {
        categories()
        products()
    }
}