package com.jj.pelismtv.logic.parent
interface FragmentInterface {

    fun initComponents(){
        configureObservers()
    }

    fun configureObservers ()

}