package app.vineshbuilds.peekaboo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameVmFactory(private val level: Int, private val onLevelCompleted: () -> Unit) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameVm(level, onLevelCompleted) as T
    }
}
