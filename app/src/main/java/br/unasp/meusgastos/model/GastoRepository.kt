package br.unasp.meusgastos.model

interface GastoRepository {
    fun listar(): List<Gasto>
    fun adicionar(descricao: String, valor: Double, categoria: Categoria)
    fun remover(id: Long)
}

class GastoRepositoryMemoria : GastoRepository {
    private val gastos = mutableListOf<Gasto>()
    private var proximoId = 1L

    override fun listar(): List<Gasto> = gastos.toList()

    override fun adicionar(descricao: String, valor: Double, categoria: Categoria) {
        gastos.add(Gasto(proximoId++, descricao, valor, categoria))
    }

    override fun remover(id: Long) {
        gastos.removeAll { it.id == id }
    }
}
