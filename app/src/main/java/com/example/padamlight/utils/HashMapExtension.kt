package com.example.padamlight.utils

import com.example.padamlight.data.local.Suggestion

fun HashMap<String, Suggestion>.ToPrettyList() : List<String>{
    val list = ArrayList<String>()
    for (key in this.keys) {
        list.add(key)
    }
    return list

}