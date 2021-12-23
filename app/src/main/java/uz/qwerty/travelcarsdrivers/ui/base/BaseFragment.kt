package uz.qwerty.travelcarsdrivers.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.readystatesoftware.chuck.internal.ui.MainActivity
import uz.qwerty.travelcarsdrivers.ui.extensions.hideKeyboard


/**
 * Created by Abdurashidov Shahzod on 23/12/21 17:34.
 * company
 * shahzod9933@gmail.com
 */

abstract class BaseScreen<T : ViewBinding>() : Fragment() {

    protected val TAG: String = "TAG"


    protected var _binding: T? = null
    protected lateinit var binding: T

    companion object {
        val DATA_ID = "_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingScreen(inflater, container)
        binding = _binding as T
        return binding.root
    }

    abstract fun bindingScreen(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreate(view, savedInstanceState)
    }

    abstract fun onViewCreate(view: View, savedInstanceState: Bundle?)

//    fun <T : ViewModel> buildViewModel(
//        @NonNull owner: ViewModelStoreOwner = this,
//        viewModel: Class<T>
//    ): T {
//        return ViewModelProvider(owner, providerFactory)[viewModel]
//    }

    inline fun <T : ViewBinding> Fragment.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) = lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        hideKeyboard()
    }


    fun getBaseString(resId: Int): String {

        return resources.getString(resId)

    }

    fun getBaseColor(resId: Int): Int {

        return resources.getColor(resId)

    }

    fun getBaseActivity(run: (MainActivity) -> (Unit)) {

        (activity as? MainActivity).let {
            it?.let { it1 ->
                run(it1)
            }
        }
    }


    fun getMainActivity(run: (MainActivity) -> (Unit)) {

        (activity as? MainActivity).let {
            it?.let { it1 ->
                run(it1)
            }
        }
    }

    fun shareLink(link: String) {
        getBaseActivity {
            val sharedText = "Blabla:\n$link"

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
            sendIntent.type = "text/plain"
            it.startActivity(sendIntent)
        }
    }

    fun shareLocalLink(link: Uri) {
        getBaseActivity {
            val sharedText = "blabla"

            Log.d("asdqwe", "${link}")
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
            sendIntent.putExtra(Intent.EXTRA_STREAM, link)
            sendIntent.type = "application/pdf"
            it.startActivity(sendIntent)
        }
    }
}


abstract class BaseFragment<out T : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreate(view, savedInstanceState)
    }

    abstract fun onViewCreate(view: View, savedInstanceState: Bundle?)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflaterBinding(inflater)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract val inflaterBinding: (LayoutInflater) -> ViewBinding

}
