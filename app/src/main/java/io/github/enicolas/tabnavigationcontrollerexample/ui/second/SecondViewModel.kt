package io.github.enicolas.tabnavigationcontrollerexample.ui.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SecondViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is a random(${Random.nextInt(0, 100)}) Fragment"
    }
    val text: LiveData<String> = _text
}