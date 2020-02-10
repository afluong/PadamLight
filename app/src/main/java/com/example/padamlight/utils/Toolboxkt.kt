package com.example.padamlight.utils

import com.example.padamlight.Suggestion
import com.example.padamlight.Suggestionkt
import java.util.*

object Toolboxkt {
    fun formatHashmapToList(hashmap: HashMap<String, Suggestionkt>): List<String> {
        val list: MutableList<String> = ArrayList()
        for (key in hashmap.keys) {
            list.add(key)
        }
        return list
    }
}
