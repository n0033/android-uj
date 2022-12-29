package pl.nw.zadanie_06

import com.google.firebase.database.FirebaseDatabase

object Constants {
    val DATABASE_URI = System.getenv("DATABASE_URI")
        ?: "https://android-06-f6858-default-rtdb.europe-west1.firebasedatabase.app"
    val REALTIME_DB = FirebaseDatabase.getInstance(DATABASE_URI).reference
    val LOCAL_DB_FILENAME = System.getenv("LOCAL_DB_FILENAME") ?: "local.db"

    val STRIPE_PUBLISHABLE_KEY = System.getenv("STRIPE_PUBLISHABLE_KEY")
        ?: "pk_test_51MKLi0AmUR8Uxul1QpPrt8w0e6WqVOoMDB4e61jz3AVERIXeWLIKQOWs4FD71bwnvGqxR1UNUq2bEC1lCmX70F1A00Td2dsTVX"

    val COLLECTION = mapOf(
        "cart" to "carts",
        "category" to "categories",
        "product" to "products",
    )
}