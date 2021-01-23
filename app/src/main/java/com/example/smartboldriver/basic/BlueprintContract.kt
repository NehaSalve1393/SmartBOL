package com.example.smartboldriver.basic

open class BlueprintContract {
    interface Presenter<in T> {

        fun attach(view: T)
    }

    interface View {

        fun showProgress()

        fun hideProgress()
    }

}