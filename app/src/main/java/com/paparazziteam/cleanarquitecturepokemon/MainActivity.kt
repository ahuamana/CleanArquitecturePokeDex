package com.paparazziteam.cleanarquitecturepokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paparazziteam.cleanarquitecturepokemon.databinding.ActivityMainBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}