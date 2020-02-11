package com.example.padamlight.utils

import com.example.padamlight.models.Suggestion
import java.util.*

object Tools {

	fun formatHashmapToList(hashmap: HashMap<String, Suggestion>): List<String> {
		val list: MutableList<String> = ArrayList()
		for (key in hashmap.keys) {
			list.add(key)
		}
		return list
	}

}