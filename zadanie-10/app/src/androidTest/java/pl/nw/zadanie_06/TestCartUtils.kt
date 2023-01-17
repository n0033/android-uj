package pl.nw.zadanie_06.utils

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pl.nw.zadanie_06.common.local_db.LocalDatabase
import pl.nw.zadanie_06.models.data.CartItem
import pl.nw.zadanie_06.models.data.Category
import pl.nw.zadanie_06.models.data.Product

class TestCartUtils {

    lateinit var category: Category
    lateinit var db: LocalDatabase
    lateinit var product: Product
    lateinit var userId: String

    // fixtures and mocks
    @Before
    fun setUp() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        userId = "testUserId"
        category = Category(name="test-category")
        product = Product(
            name = "test product", description = "test", price = 1.23, categoryId = category.uid
        )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createEmptyCart() = runTest {
        val result = CartUtils.createEmptyCart(db, userId)
        assertEquals(result.items.cartItemList, arrayListOf<CartItem>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeProductFromEmptyCart() = runTest {
        CartUtils.removeFromCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProduct() = runTest {
        CartUtils.addToCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>(CartItem(1, product.uid)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addTwoDifferentProducts() = runTest {
        val secondProduct = Product(
            name = "test-product-2",
            description = "test",
            price = 3.21,
            categoryId = "dummy-category"
        )
        CartUtils.addToCart(db, product, userId)
        CartUtils.addToCart(db, secondProduct, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList.sortedBy { it.productId }, arrayListOf(
            CartItem(1, product.uid), CartItem(1, secondProduct.uid)
        ).sortedBy { it.productId })

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addTwoSameProducts() = runTest {
        CartUtils.addToCart(db, product, userId)
        CartUtils.addToCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(
            testCart!!.items.cartItemList, arrayListOf(CartItem(2, product.uid))
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addAndRemoveProduct() = runTest {
        CartUtils.addToCart(db, product, userId)
        CartUtils.removeFromCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>())
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun singleProductQuantity() = runTest {
        CartUtils.addToCart(db, product, userId)
        CartUtils.addToCart(db, product, userId)
        CartUtils.removeFromCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf(CartItem(1, product.uid)))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun twoProductsQuantity() = runTest {
        val secondProduct = Product(
            name = "test-product-2",
            description = "test",
            price = 3.21,
            categoryId = "dummy-category"
        )
        for (i in 0..2) {
            CartUtils.addToCart(db, product, userId)
        }
        for (i in 0..1) {
            CartUtils.addToCart(db, secondProduct, userId)
        }
        CartUtils.removeFromCart(db, product, userId)
        CartUtils.removeFromCart(db, secondProduct, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList.sortedBy { it.productId }, arrayListOf(
            CartItem(2, product.uid), CartItem(1, secondProduct.uid)
        ).sortedBy { it.productId })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flushEmptyCart() = runTest {
        CartUtils.createEmptyCart(db, userId)
        CartUtils.flushCart(db, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flushNonExistentCart() = runTest {
        CartUtils.flushCart(db, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToNonExistentCart() = runTest {
        CartUtils.addToCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf(CartItem(1, product.uid)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeProductFromNonExistentCart() = runTest {
        CartUtils.removeFromCart(db, product, userId)
        val testCart = db.cartDao().findCartByUserId(userId)
        assertEquals(testCart!!.items.cartItemList, arrayListOf<CartItem>())
    }


}
