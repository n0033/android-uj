package pl.nw.zadanie_06.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.nw.zadanie_06.models.data.Address
import pl.nw.zadanie_06.models.data.CartItem
import pl.nw.zadanie_06.models.data.CartItemList
import java.time.LocalDateTime

class TestConverters {
    private val converter = Converters()


    @Test
    fun cartItemListToJson() {
        val cartItemList = CartItemList(cartItemList = arrayListOf(CartItem(1, "essa")))
        val json = converter.fromCartItemListToJSON(cartItemList)
        assertEquals(json, "{\"cartItemList\":[{\"quantity\":1,\"productId\":\"essa\"}]}")
    }

    @Test
    fun jsonToCartItemList() {
        val cartItemList = CartItemList(cartItemList = arrayListOf(CartItem(1, "essa")))
        val result =
            converter.fromJSONToCartItemList("{\"cartItemList\":[{\"quantity\":1,\"productId\":\"essa\"}]}")
        assertEquals(cartItemList, result)
    }

    @Test
    fun twoWayCartItemList() {
        val cartItemList = CartItemList(cartItemList = arrayListOf(CartItem(1, "essa")))
        val json = converter.fromCartItemListToJSON(cartItemList)
        val result = converter.fromJSONToCartItemList(json)
        assertEquals(cartItemList, result)
    }

    @Test
    fun addressToJson() {
        val address = Address("line1", "line2", "00-000", "essacity")
        val json = converter.fromAddressToJSON(address)
        assertEquals(
            "{\"line1\":\"line1\",\"line2\":\"line2\",\"postCode\":\"00-000\",\"city\":\"essacity\"}",
            json
        )

    }

    @Test
    fun jsonToAddress() {
        val json = "{\"line1\":\"line1\",\"line2\":\"line2\",\"postCode\":\"00-000\",\"city\":\"essacity\"}"
        val address = Address("line1", "line2", "00-000", "essacity")
        val result = converter.fromJSONtoAddress(json)
        assertEquals(address, result)
    }

    @Test
    fun twoWayAddress() {
        val address = Address("line1", "line2", "00-000", "essacity")
        val json = converter.fromAddressToJSON(address)
        val result = converter.fromJSONtoAddress(json)
        assertEquals(address, result)
    }

    @Test
    fun localDateTimeToString() {
        val timestamp = LocalDateTime.of(2023, 1, 1, 1, 1)
        val result = converter.fromLocalDateTimeToString(timestamp)
        assertEquals("2023-01-01T01:01", result)
    }

    @Test
    fun stringToLocalDateTime() {
        val result = converter.toLocalDateTime("2023-01-01T01:01")
        val expectedTimestamp = LocalDateTime.of(2023, 1, 1, 1, 1)
        assertEquals(expectedTimestamp, result)
    }


}
