package uz.qwerty.travelcarsdrivers.presentation.ui.base

import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.transparentStatusBar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    lateinit var binding: T



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.transparentStatusBar()
        _binding = bindingActivity()
        binding = _binding as T
        setContentView(binding.root)
        onCreated(savedInstanceState)
    }

    abstract fun bindingActivity(): T

    abstract fun onCreated(savedInstanceState: Bundle?)



}