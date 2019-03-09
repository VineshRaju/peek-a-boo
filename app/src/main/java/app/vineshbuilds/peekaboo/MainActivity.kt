package app.vineshbuilds.peekaboo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import app.vineshbuilds.peekaboo.databinding.ActivityMainBinding
import app.vineshbuilds.peekaboo.databinding.ItemTilesBinding

class MainActivity : AppCompatActivity() {
    private lateinit var gameVm: GameVm
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        gameVm = ViewModelProviders.of(this, GameVmFactory(3, this::onLevelComplete)).get(GameVm::class.java)
        binding.vm = gameVm
        populateTiles()
    }

    override fun onPause() {
        super.onPause()
        gameVm.onPause()
    }

    private fun populateTiles() {
        binding.tiles.removeAllViews()
        gameVm.tiles.forEach { tileVm ->
            val tileView =
                DataBindingUtil.inflate<ItemTilesBinding>(layoutInflater, R.layout.item_tiles, binding.tiles, false)
                    .apply {
                        vm = tileVm
                        onTap = { gameVm.openTile(it) }
                    }.root
            binding.tiles.addView(tileView)
        }
    }

    private fun onLevelComplete() {
        gameVm.levelUp()
        populateTiles()
    }
}
