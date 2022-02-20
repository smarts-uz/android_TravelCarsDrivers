package uz.qwerty.travelcarsdrivers.presentation.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.databinding.ActivityCourseBinding
import uz.qwerty.travelcarsdrivers.domain.models.NewCurrencyResponse
import uz.qwerty.travelcarsdrivers.presentation.ui.base.BaseActivity
import uz.qwerty.travelcarsdrivers.presentation.ui.common.course.CourseViewModel
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.gone
import uz.qwerty.travelcarsdrivers.presentation.ui.extensions.visible
import uz.qwerty.travelcarsdrivers.presentation.ui.state.*

@AndroidEntryPoint
class CourseActivity : BaseActivity<ActivityCourseBinding>() {


    private val vm: CourseViewModel by viewModels()

    override fun bindingActivity(): ActivityCourseBinding {
        return ActivityCourseBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        loadObserver()
        loadView()
    }

    private fun loadView() {
        //vm.getAllCourse()
        vm.getCourse()
    }

    private fun loadObserver() {
        vm.currencyNewLiveData.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                render(it)
            }
        }


//        vm.currencyLiveData.observe(this, {
//            binding.progressBar.gone()
//            courseAdapter.submitList(it)
//        })
//        vm.loadingLiveData.observe(this, {
//            binding.progressBar.visible()
//        })
//        vm.connectLiveData.observe(this, {
//            if (it) showErrorMessage(
//                getString(R.string.warning),
//                getString(R.string.no_connection_icon)
//            )
//        })
//        vm.errorLiveData.observe(this, {
//            showErrorMessage(getString(R.string.warning), it!!)
//        })

    }

//    private fun render(viewState: ViewState) {
//
//        if (viewState !is Loading) {
//            //binding.swipeRefreshLayout.isRefreshing = false
//            binding.progressBar.gone()
//        }
//        //MVI
//        when (viewState) {
//            is Fail -> {
//                showErrorMessage(getString(R.string.warning), viewState.exception.message!!)
//            }
//            is Loading -> {
//                //binding.swipeRefreshLayout.isRefreshing = true
//                binding.progressBar.visible()
//            }
//            is ServerError -> {
//                showErrorMessage(getString(R.string.warning), viewState.errorMessage)
//            }
//            is Success<*> -> {
//                val list = viewState.data as List<CurrencyItem>
//                courseAdapter.submitList(list)
//            }
//            else -> {}
//        }
//    }

    private fun render(viewState: ViewState) {

        if (viewState !is Loading) {
            //binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.gone()
        }
        //MVI
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
                val list = viewState.data as List<NewCurrencyResponse>
                val newList = ArrayList<NewCurrencyResponse>()
                for (i in list.indices) {
                    if (list[i].code == "USD" ||
                        list[i].code == "EUR" ||
                        list[i].code == "RUB"
                    ) {
                        val newCurrencyResponse = list[i]
                        newList.addAll(listOf(newCurrencyResponse))
                    }
                }

            }
            else -> {}
        }
    }

}