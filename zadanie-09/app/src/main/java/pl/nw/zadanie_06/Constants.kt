package pl.nw.zadanie_06

import com.google.firebase.database.FirebaseDatabase

object Constants {
    val COLLECTION = mapOf(
        "cart" to "carts",
        "category" to "categories",
        "product" to "products",
    )
    val DATABASE_URI = System.getenv("DATABASE_URI")
        ?: "https://android-06-f6858-default-rtdb.europe-west1.firebasedatabase.app"
    val REALTIME_DB = FirebaseDatabase.getInstance(DATABASE_URI).reference
    val LOCAL_DB_FILENAME = System.getenv("LOCAL_DB_FILENAME") ?: "local3.db"

    val STRIPE_PUBLISHABLE_KEY = System.getenv("STRIPE_PUBLISHABLE_KEY")
        ?: "pk_test_51MKLi0AmUR8Uxul1QpPrt8w0e6WqVOoMDB4e61jz3AVERIXeWLIKQOWs4FD71bwnvGqxR1UNUq2bEC1lCmX70F1A00Td2dsTVX"
    val STRIPE_SECRET_KEY = System.getenv("STRIPE_SECRET_KEY")
        ?: "sk_test_51MKLi0AmUR8Uxul17nxySwrxcbbeMkRgF1dtsjgiL7r2nBXVSZWCcIsWaPzYBiCSqfE3OOvvIHWfuexmGwxrFUa600sBEIU2s4"
    val STRIPE_CREATE_CUSTOMER_URL =
        System.getenv("STRIPE_CREATE_CUSTOMER_URL") ?: "https://api.stripe.com/v1/customers"
    val STRIPE_PAYMENT_INTENT_URL =
        System.getenv("STRIPE_PAYMENT_INTENT_URL") ?: "https://api.stripe.com/v1/payment_intents"

    val PAYU_SUCCESS_URL =
        System.getenv("PAYU_SUCCESS_URL") ?: ("https://63b953f13329392049f0a4cd.mockapi.io/payment-success")
    val PAYU_FAILURE_URL =
        System.getenv("PAYU_FAILURE_URL") ?: ("https://63b953f13329392049f0a4cd.mockapi.io/payment-failure")
    val PAYU_KEY = System.getenv("PAYU_KEY") ?: "gtKFFx"
    val PAYU_SALT = System.getenv("PAYU_SALT") ?: "4R38IvwiV57FwVpsgOvTXBdLE4tHUXFW"

}