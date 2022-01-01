package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import uz.qwerty.travelcarsdrivers.databinding.ActivityCourseBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.adapters.CourseAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.showToast

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
    }

    private fun loadObserver() {
        vm.currencyLiveData.observe(this, {
            courseAdapter.submitList(it)
            showToast(it[0].ccyNmUZC)
        })
        vm.currencyErrorLiveData.observe(this, { err ->
            showToast("Fail $err")
        })
    }

}