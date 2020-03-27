package com.csrapp.csr.data

class AptitudeCategoryRepository private constructor(private val aptitudeCategoryDao: AptitudeCategoryDao) {

    fun getAllCategories() = aptitudeCategoryDao.getAllCategories()

    companion object {
        private var instance: AptitudeCategoryRepository? = null

        fun getInstance(aptitudeCategoryDao: AptitudeCategoryDao) = instance ?: synchronized(this) {
            instance ?: AptitudeCategoryRepository(aptitudeCategoryDao).also {
                instance = it
            }
        }
    }
}