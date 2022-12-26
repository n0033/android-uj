package pl.nw.zadanie_06

import com.google.firebase.database.FirebaseDatabase

object Constants {
    val DATABASE_URI = System.getenv("DATABASE_URI")
        ?: "https://android-06-f6858-default-rtdb.europe-west1.firebasedatabase.app"
    val REALTIME_DB = FirebaseDatabase.getInstance(DATABASE_URI).reference
    val LOCAL_DB_FILENAME = System.getenv("LOCAL_DB_FILENAME")?: "local.db"

    val COLLECTION = mapOf(
        "cart" to "carts",
        "category" to "categories",
        "product" to "products",
    )
}