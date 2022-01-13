package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import aglibs.loading.skeleton.layout.SkeletonRelativeLayout
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.databinding.ActivityCourseBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.CourseAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.gone
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.showToast
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.visible
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*

@AndroidEntryPoint
class CourseActivity : BaseActivity<ActivityCourseBinding>() {


    private val courseAdapter by lazy { CourseAdapter() }
    private val vm: CourseViewModel by viewModels()

    override fun bindingActivity(): ActivityCourseBinding {
        return ActivityCourseBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
        loadView()
    }

    private fun loadView() {
        binding.rv.adapter = courseAdapter
        vm.getAllCourse()
        //vm.getCourse()
    }

    private fun loadObserver() {
/*

        vm.courseLiveData.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                render(it)
            }
        })
 */

        vm.currencyLiveData.observe(this, {
            binding.progressBar.gone()
            binding.skelton.gone()
            courseAdapter.submitList(it)
        })
        vm.loadingLiveData.observe(this, {
            binding.progressBar.visible()
        })
        vm.connectLiveData.observe(this, {
            if (it) showErrorMessage(
                getString(R.string.warning),
                getString(R.string.no_connection_icon)
            )
        })
        vm.errorLiveData.observe(this, {
            showErrorMessage(getString(R.string.warning), it!!)
        })

    }

    private fun render(viewState: ViewState) {

        if (viewState !is Loading) {
            //binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.gone()
        }

        when (viewState) {
            is Fail -> {
                showErrorMessage(getString(R.string.warning), viewState.exception.message!!)
            }
            is Loading -> {
                //binding.swipeRefreshLayout.isRefreshing = true
                binding.progressBar.visible()
            }
            is ServerError -> {
                showErrorMessage(getString(R.string.warning), viewState.errorMessage)
            }
            is Success<*> -> {
                val list = viewState.data as List<CurrencyItem>
                courseAdapter.submitList(list)
            }
            else -> {}
        }
    }

}