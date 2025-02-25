package com.example.addressmatcher

import org.json.JSONArray
import org.json.JSONObject

/**
 * I found this and modified it to suit. A month later I've forgotten how
 * it works and despite having tests to remind me, Kotlin is still another
 * language to me.
 */
fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate {
                Pair(it.toString(), value[it])
            }
            // We'd cast map to JSONObject(map).toMap() if recursing.
            map.values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}
