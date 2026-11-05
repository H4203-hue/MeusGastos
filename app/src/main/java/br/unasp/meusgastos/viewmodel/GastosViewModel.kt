package br.unasp.meusgastos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.unasp.meusgastos.model.Categoria
import br.unasp.meusgastos.model.Gasto
import br.unasp.meusgastos.model.GastoRepository
import br.unasp.meusgastos.model.GastoRepositoryMemoria

class GastosViewModel(
    private val repositorio: GastoRepository = GastoRepositoryMemoria()
) : ViewModel() {
    // O que a tela precisa saber
    var descricao by mutableStateOf("")
        private set
    var valorTexto by mutableStateOf("")
        private set
    var categoriaSelecionada by mutableStateOf(Categoria.ALIMENTACAO)
        private set
    var erro: String? by mutableStateOf(null)
        private set
    var filtro: Categoria? by mutableStateOf(null)
        private set

    private val _gastos = mutableStateListOf<Gasto>()
    val gastos: List<Gasto> get() = _gastos.toList()
    val gastosVisiveis: List<Gasto>
        get() = filtro?.let { categoria -> gastos.filter { it.categoria == categoria } } ?: gastos
    val total: Double get() = gastosVisiveis.sumOf { it.valor }
    val quantidade: Int get() = gastosVisiveis.size

    // O que a tela pode pedir
    init {
        sincronizar()
    }

    fun aoDigitarDescricao(novo: String) {
        descricao = novo
        erro = null
    }

    fun aoDigitarValor(novo: String) {
        valorTexto = novo
        erro = null
    }

    fun aoSelecionarCategoria(nova: Categoria) {
        categoriaSelecionada = nova
    }

    fun aoFiltrar(categoria: Categoria?) {
        filtro = categoria
    }

    fun adicionar() {
        val valor = valorTexto.trim().replace(',', '.').toDoubleOrNull()
        when {
            descricao.isBlank() -> erro = "Informe a descrição do gasto"
            valor == null || !valor.isFinite() -> erro = "O valor precisa ser um número"
            valor <= 0.0 -> erro = "O valor precisa ser maior que zero"
            else -> {
                repositorio.adicionar(descricao.trim(), valor, categoriaSelecionada)
                sincronizar()
                descricao = ""
                valorTexto = ""
                erro = null
            }
        }
    }

    fun remover(id: Long) {
        repositorio.remover(id)
        sincronizar()
    }

    private fun sincronizar() {
        _gastos.clear()
        _gastos.addAll(repositorio.listar())
    }
}
