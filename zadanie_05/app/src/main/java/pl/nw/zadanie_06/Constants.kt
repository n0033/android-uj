package pl.nw.zadanie_06

import com.google.firebase.database.FirebaseDatabase

object Constants {
    val DATABASE_URI = System.getenv("DATABASE_URI")
        ?: "https://android-06-f6858-default-rtdb.europe-west1.firebasedatabase.app"
    val DB = FirebaseDatabase.getInstance(DATABASE_URI).reference

    val COLLECTION = mapOf(
        "cart" to "carts",
        "category" to "categories",
        "product" to "products",
    )
}