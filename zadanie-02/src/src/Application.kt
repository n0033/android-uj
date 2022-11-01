package pl.edu.uj.android

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.*
import pl.edu.uj.android.models.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }
    initDB()


    routing {

        apiRoute()

        get("/") {
            call.respond(mapOf("content" to "Hello World!"))
        }
    }
}

