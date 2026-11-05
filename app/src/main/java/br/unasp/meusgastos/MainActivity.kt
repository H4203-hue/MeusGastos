package br.unasp.meusgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.unasp.meusgastos.model.GastoRepositoryDemo
import br.unasp.meusgastos.ui.TelaGastos
import br.unasp.meusgastos.ui.TemaMeusGastos
import br.unasp.meusgastos.viewmodel.GastosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: GastosViewModel = viewModel(factory = gastosFactory)
            TemaMeusGastos { TelaGastos(vm = vm) }
        }
    }
}

private val gastosFactory = viewModelFactory {
    initializer { GastosViewModel(GastoRepositoryDemo()) }
}
