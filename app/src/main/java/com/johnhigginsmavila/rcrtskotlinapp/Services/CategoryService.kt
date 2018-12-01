package com.johnhigginsmavila.rcrtskotlinapp.Services

import com.johnhigginsmavila.rcrtskotlinapp.Model.Category

object CategoryService {
    val categories: List<Category> = listOf(
        Category("Fire", "Fire related Reports."),
        Category("Garbage", "Garbage Related Reports."),
        Category("Theft","Theft Related Reports."),
        Category("Riot", "Riot Related Reports.")
    )

    val categoriesArray = arrayOf("Fire", "Garbage", "Theft", "Riot")
}