package pl.edu.uj.android

import io.ktor.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import pl.edu.uj.android.models.Categories
import pl.edu.uj.android.models.Products


fun Application.initDB() {
    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
    createTables()
}


private fun createTables()  = transaction {
   SchemaUtils.create(Products, Categories)
}