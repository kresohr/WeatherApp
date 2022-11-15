package com.ikresimir.weatherilicic.source



class Repository {

    /* API_KEY would usually be hidden from VCS by using "Gradle Secret Plugin" or other methods.
     This is just for task demonstration purpose.*/
    val API_KEY="318ce672275d66205dd441034c200da6"

    fun getRemoteData(){

    }

    fun saveToLocalDatabase(){}

    fun getLocalData(){}

    // This function will compare local and remote data, if same,
    fun compareData(){

    }

}