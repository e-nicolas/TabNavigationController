package io.github.enicolas.tabnavigationcontrollerexample

import java.io.Serializable

internal class TabControllerHistory: Serializable {

	/**
	 * Variables
	 */
	private val stack: ArrayList<Int> = ArrayList()
	val isNotEmpty: Boolean
		get() = stack.isNotEmpty()

	val size: Int
	get() = stack.size

	/**
	 * Add a new tabIndex to stack
	 */
	fun push(entry: Int) {
		stack.add(entry)
	}

	/**
	 * Remove the last tabIndex from stack and return the previous one
	 */
	fun popPrevious(): Int {
		var entry = -1
		if (isNotEmpty) {
			stack.removeAt(stack.size - 1)
			entry = stack[stack.size-1]
		}
		return entry
	}

	/**
	 * Remove all tabIndex from stack
	 */
	fun clear() {
		stack.clear()
	}
}
