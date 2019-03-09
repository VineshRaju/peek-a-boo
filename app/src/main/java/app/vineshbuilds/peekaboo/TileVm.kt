package app.vineshbuilds.peekaboo

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import app.vineshbuilds.peekaboo.TileVm.TileState.*

class TileVm(val display: String) : ViewModel() {
    val shouldShow = ObservableBoolean(true)
    private var state: TileState = Open
        set(value) {
            field = value
            shouldShow.set(value != Close)
        }

    fun open() {
        state = Open
    }

    fun close() {
        state = Close
    }

    fun isOpen() = state == Open

    fun markAsFound() {
        state = Found
    }

    sealed class TileState {
        object Open : TileState()
        object Close : TileState()
        object Found : TileState()
    }
}
