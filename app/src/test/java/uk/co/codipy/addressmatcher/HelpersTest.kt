package uk.co.codipy.addressmatcher

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class HelpersTest {
    @Test
    fun toMap() {
        val sample1ToManyMap = "{\"S\" : [\"Chesterfield\", \"Sheffield\"], \"SE\" : [\"London\"]}"
        val jsonObj = JSONObject(sample1ToManyMap)
        val map = jsonObj.toMap()
        println(map["S"]!!::class)
        assertEquals(listOf("Chesterfield", "Sheffield"), map["S"])
        assertEquals(listOf("London"), map["SE"])
    }
}