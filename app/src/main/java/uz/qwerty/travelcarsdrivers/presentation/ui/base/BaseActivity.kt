package uz.qwerty.travelcarsdrivers.presentation.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.transparentStatusBar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewbinding.ViewBinding
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.showToast

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    lateinit var binding: T
    private var baseActivityStopped = false
    private var baseMessageDialog: Dialog? = null


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

    fun showErrorMessage(title: String = "", txt: String, callback: (() -> Unit)? = null) {

        if (!baseActivityStopped) {
            if (baseMessageDialog == null) {

                loadDialog()
                val statusImageview =
                    baseMessageDialog?.findViewById<AppCompatImageView>(R.id.status_imageview)
                statusImageview?.setImageResource(R.drawable.ic_warning)

                val dialogTv = baseMessageDialog?.findViewById<TextView>(R.id.titletextview)
                dialogTv?.text =
                    if (title.isNotEmpty()) title else resources.getString(R.string.warning)
                val errorDescription =
                    baseMessageDialog?.findViewById<TextView>(R.id.error_description_textview)
                errorDescription?.text = txt
                val okButton = baseMessageDialog?.findViewById<Button>(R.id.ok_button)
                okButton?.setOnClickListener {
                    callback?.invoke()
                    baseMessageDialog?.dismiss()
                    baseMessageDialog = null
                }
                baseMessageDialog?.show()
            }
        } else {
            showToast(txt)
        }

    }

    private fun loadDialog() {
        baseMessageDialog = Dialog(this)
        baseMessageDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        baseMessageDialog?.setCancelable(false)
        baseMessageDialog?.setContentView(R.layout.base_dialog_layout)
    }

    override fun onStop() {
        super.onStop()
        baseActivityStopped = true
    }

    override fun onResume() {
        super.onResume()
        baseActivityStopped = false

    }
}