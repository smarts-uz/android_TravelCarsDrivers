package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
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
        //vm.getAllCourse()
        vm.getCourse()
    }

    private fun loadObserver() {

        vm.courseLiveData.observe(this, { event ->
            event.getContentIfNotHandled()?.let {
                render(it)
            }
        })

//        vm.currencyLiveData.observe(this, {
//            courseAdapter.submitList(it)
//        })
//        vm.currencyErrorLiveData.observe(this, { err ->
//            showToast("Fail $err")
//        })
    }

    private fun render(viewState: ViewState) {

        if (viewState !is Loading) {
            //binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.gone()
        }

        when (viewState) {
            is Fail -> {
            }
            is Loading -> {
                //binding.swipeRefreshLayout.isRefreshing = true
                binding.progressBar.visible()
            }
            is ServerError -> {

            }
            is Success<*> -> {
                val list = viewState.data as List<CurrencyItem>
                courseAdapter.submitList(list)
            }
            else -> {}
        }
    }

}