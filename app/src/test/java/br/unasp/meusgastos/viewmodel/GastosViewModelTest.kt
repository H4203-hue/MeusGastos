package br.unasp.meusgastos.viewmodel

import androidx.compose.runtime.snapshots.Snapshot
import br.unasp.meusgastos.model.Categoria
import br.unasp.meusgastos.model.GastoRepositoryDemo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GastosViewModelTest {
    @Test
    fun validaCamposEAdicionaValorComVirgula() {
        val vm = GastosViewModel()

        vm.aoDigitarValor("5")
        vm.adicionar()
        assertEquals("Informe a descrição do gasto", vm.erro)
        assertTrue(vm.gastos.isEmpty())

        vm.aoDigitarDescricao("  Café  ")
        for (entrada in listOf("", "abc", "NaN", "Infinity")) {
            vm.aoDigitarValor(entrada)
            vm.adicionar()
            assertEquals("O valor precisa ser um número", vm.erro)
        }

        for (entrada in listOf("0", "-2")) {
            vm.aoDigitarValor(entrada)
            vm.adicionar()
            assertEquals("O valor precisa ser maior que zero", vm.erro)
        }

        vm.aoSelecionarCategoria(Categoria.OUTROS)
        vm.aoDigitarValor("4,50")
        vm.adicionar()

        val gasto = vm.gastos.single()
        assertEquals("Café", gasto.descricao)
        assertEquals(4.50, gasto.valor, 0.001)
        assertEquals(Categoria.OUTROS, gasto.categoria)
        assertEquals("", vm.descricao)
        assertEquals("", vm.valorTexto)
        assertNull(vm.erro)
    }

    @Test
    fun trocarInstanciaDoViewModelReiniciaFormularioELista() {
        val vmOriginal = GastosViewModel()
        adicionar(vmOriginal, "Café", "5")
        adicionar(vmOriginal, "Almoço", "15")
        vmOriginal.aoDigitarDescricao("R")

        val vmNovo = GastosViewModel()
        assertEquals(2, vmOriginal.quantidade)
        assertEquals("R", vmOriginal.descricao)
        assertEquals(0, vmNovo.quantidade)
        assertEquals("", vmNovo.descricao)
    }

    @Test
    fun listaEmiteLeiturasObservaveisPeloCompose() {
        val vm = GastosViewModel()
        var leituras = 0
        Snapshot.observe(readObserver = { leituras++ }, writeObserver = null) {
            assertTrue(vm.gastos.isEmpty())
        }
        assertTrue("A leitura da lista precisa ser observável pelo Compose", leituras > 0)
    }

    @Test
    fun recalculaTotalAposRemoverEFiltrar() {
        val vm = GastosViewModel()
        adicionar(vm, "Café", "5")
        adicionar(vm, "Almoço", "15")
        vm.aoSelecionarCategoria(Categoria.TRANSPORTE)
        adicionar(vm, "Ônibus", "10")
        assertEquals(30.0, vm.total, 0.001)

        vm.remover(vm.gastos.first().id)
        assertEquals(25.0, vm.total, 0.001)

        vm.aoFiltrar(Categoria.TRANSPORTE)
        assertEquals(1, vm.quantidade)
        assertEquals(10.0, vm.total, 0.001)

        vm.aoFiltrar(Categoria.MORADIA)
        assertEquals(0, vm.quantidade)
        assertEquals(0.0, vm.total, 0.001)
    }

    @Test
    fun aceitaRepositorioDemoERemovePeloId() {
        val vm = GastosViewModel(GastoRepositoryDemo())
        assertEquals(4, vm.quantidade)
        assertEquals(201.70, vm.total, 0.001)

        vm.aoSelecionarCategoria(Categoria.LAZER)
        adicionar(vm, "Cinema", "32")
        val primeiroCinema = vm.gastos.first { it.descricao == "Cinema" }
        vm.remover(primeiroCinema.id)
        vm.aoFiltrar(Categoria.LAZER)

        assertEquals(5L, vm.gastosVisiveis.single().id)
        assertEquals(32.0, vm.total, 0.001)
    }

    private fun adicionar(vm: GastosViewModel, descricao: String, valor: String) {
        vm.aoDigitarDescricao(descricao)
        vm.aoDigitarValor(valor)
        vm.adicionar()
    }
}
