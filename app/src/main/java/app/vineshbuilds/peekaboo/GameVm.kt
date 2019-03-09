package app.vineshbuilds.peekaboo

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class GameVm(private var level: Int, private val onLevelCompleted: () -> Unit): ViewModel() {
    val scoreString = ObservableField<String>("0")
    val levelString = ObservableField<String>(level.toString())
    val tiles = mutableListOf<TileVm>()
    private var score = 0
    private var currentLevelScore = 0
    private val disposables = CompositeDisposable()

    init {
        createTiles()
        scheduleToCloseTiles(2000)
    }

    private fun createTiles() {
        tiles.clear()
        for (i in 1..level) {
            tiles.add(TileVm(i.toString()))
            tiles.add(TileVm(i.toString()))
        }
        tiles.shuffle()
    }

    private fun closeNonFoundTiles() {
        disposables.clear()
        tiles.filter { it.isOpen() }.forEach { it.close() }
    }

    fun openTile(tile: TileVm) {
        if (getOpenTiles().size >= 2) closeNonFoundTiles()
        tile.open()
        check()
        scheduleToCloseTiles()
    }

    private fun scheduleToCloseTiles(delay: Long = 1500) {
        disposables.addAll(
            Observable.timer(delay, TimeUnit.MILLISECONDS).doOnComplete(this::closeNonFoundTiles).subscribe()
        )
    }

    private fun check() {
        getOpenTiles().let {
            if (it.size == 2 && it[0].display == it[1].display) {
                it[0].markAsFound()
                it[1].markAsFound()
                incrementScore()
                closeNonFoundTiles()
            }
        }
        if (isLevelComplete()) {
            currentLevelScore = 0
            onLevelCompleted.invoke()
        }
    }

    private fun getOpenTiles() = tiles.filter { it.isOpen() }

    private fun isLevelComplete() = currentLevelScore == (level * level)

    private fun incrementScore() {
        score += level
        currentLevelScore += level
        scoreString.set(score.toString())
    }

    fun levelUp() {
        disposables.clear()
        level++
        levelString.set(level.toString())
        createTiles()
    }

    fun onPause() {
        closeNonFoundTiles()
        disposables.clear()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
