package uz.qwerty.travelcarsdrivers.util


/**
 * Created by Abdurashidov Shahzod on 02/01/22 16:15.
 * company QQBank
 *
 */

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}