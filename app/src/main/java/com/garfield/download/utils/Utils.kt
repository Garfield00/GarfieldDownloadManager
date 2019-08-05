package com.garfield.gallery.utils

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description
 */

object Utils {
    /*判断数组是否为空*/
    fun isStringArrayEmpty(stringArray: Array<String>?): Boolean {
        return stringArray == null || stringArray.isEmpty()
    }

    /*判断数组是否不为空*/
    fun isStringArrayNotEmpty(stringArray: Array<String>?): Boolean {
        return !isStringArrayEmpty(stringArray)
    }

    /*判断list是否为空*/
    fun isListEmpty(list: List<*>?): Boolean {
        return null == list || list.isEmpty()
    }

    fun getListSize(list: List<*>?): Int {
        return list?.size ?: 0
    }

}
