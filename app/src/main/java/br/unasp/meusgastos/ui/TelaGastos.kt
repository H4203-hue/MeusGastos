package br.unasp.meusgastos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unasp.meusgastos.model.Categoria
import br.unasp.meusgastos.model.Gasto
import br.unasp.meusgastos.viewmodel.GastosViewModel
import java.text.NumberFormat
import java.util.Locale

private val Azul = Color(0xFF1C3757)
private val AzulSuave = Color(0xFF536E8A)
private val AzulClaro = Color(0xFFA8BED4)
private val Fundo = Color(0xFFF1F5F9)
private val FundoCampo = Color(0xFFF5F7FA)
private val FundoEtiqueta = Color(0xFFE5EDF6)
private val Laranja = Color(0xFFD45C39)
private val Erro = Color(0xFFAE4226)
private val FormatoReal = Locale.forLanguageTag("pt-BR")

@Composable
fun TemaMeusGastos(conteudo: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Azul,
            background = Fundo,
            surface = Color.White,
            onSurface = Azul,
            error = Erro
        ),
        content = conteudo
    )
}

@Composable
fun TelaGastos(vm: GastosViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gastos = vm.gastosVisiveis

    Surface(modifier = modifier.fillMaxSize(), color = Fundo, contentColor = Azul) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.widthIn(max = 680.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("Meus Gastos", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    FormularioGasto(
                        descricao = vm.descricao,
                        valorTexto = vm.valorTexto,
                        categoria = vm.categoriaSelecionada,
                        erro = vm.erro,
                        aoDigitarDescricao = vm::aoDigitarDescricao,
                        aoDigitarValor = vm::aoDigitarValor,
                        aoSelecionarCategoria = vm::aoSelecionarCategoria,
                        aoAdicionar = vm::adicionar
                    )
                    FiltroCategorias(selecionada = vm.filtro, aoFiltrar = vm::aoFiltrar)
                    ResumoTotal(total = vm.total, quantidade = vm.quantidade)
                }
            }
            items(gastos, key = { it.id }) { gasto ->
                LinhaGasto(
                    gasto = gasto,
                    aoRemover = { vm.remover(gasto.id) },
                    modifier = Modifier.widthIn(max = 680.dp)
                )
            }
        }
    }
}

@Composable
private fun FormularioGasto(
    descricao: String,
    valorTexto: String,
    categoria: Categoria,
    erro: String?,
    aoDigitarDescricao: (String) -> Unit,
    aoDigitarValor: (String) -> Unit,
    aoSelecionarCategoria: (Categoria) -> Unit,
    aoAdicionar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoGasto(
                valor = descricao,
                aoDigitar = aoDigitarDescricao,
                rotulo = "Descrição do gasto",
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CampoGasto(
                    valor = valorTexto,
                    aoDigitar = aoDigitarValor,
                    rotulo = "Valor do gasto",
                    exemplo = "0,00",
                    teclado = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = aoAdicionar,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul)
                ) {
                    Text("Adicionar", fontWeight = FontWeight.Bold)
                }
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Categoria.entries.forEach { opcao ->
                    Etiqueta(opcao.rotulo, opcao == categoria) { aoSelecionarCategoria(opcao) }
                }
            }
            erro?.let { Text(it, color = Erro, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
private fun CampoGasto(
    valor: String,
    aoDigitar: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    exemplo: String = rotulo,
    teclado: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = valor,
        onValueChange = aoDigitar,
        modifier = modifier.semantics { contentDescription = rotulo },
        placeholder = { Text(exemplo) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        shape = RoundedCornerShape(topStart = 9.dp, topEnd = 9.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FundoCampo,
            unfocusedContainerColor = FundoCampo,
            unfocusedIndicatorColor = Color(0xFFCAD8E8),
            unfocusedPlaceholderColor = AzulSuave
        )
    )
}

@Composable
private fun FiltroCategorias(selecionada: Categoria?, aoFiltrar: (Categoria?) -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()).selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Etiqueta("Todas", selecionada == null) { aoFiltrar(null) }
        Categoria.entries.forEach { categoria ->
            Etiqueta(categoria.rotulo, selecionada == categoria) { aoFiltrar(categoria) }
        }
    }
}

@Composable
private fun ResumoTotal(total: Double, quantidade: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Azul,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Total", color = AzulClaro, fontSize = 14.sp)
                Text(emReais(total), fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Text("$quantidade gasto(s)", color = AzulClaro, fontSize = 14.sp)
        }
    }
}

@Composable
private fun LinhaGasto(gasto: Gasto, aoRemover: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(gasto.descricao, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(gasto.categoria.rotulo, color = AzulSuave, fontSize = 13.sp)
            }
            Text(emReais(gasto.valor), fontSize = 16.sp)
            TextButton(onClick = aoRemover, contentPadding = PaddingValues(horizontal = 8.dp)) {
                Text("remover", color = Erro, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun Etiqueta(rotulo: String, selecionada: Boolean, aoClicar: () -> Unit) {
    Text(
        text = rotulo,
        color = if (selecionada) Color.White else AzulSuave,
        fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Medium,
        fontSize = 13.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selecionada) Laranja else FundoEtiqueta)
            .selectable(selected = selecionada, role = Role.RadioButton, onClick = aoClicar)
            .padding(horizontal = 11.dp, vertical = 8.dp)
    )
}

private fun emReais(valor: Double): String =
    NumberFormat.getCurrencyInstance(FormatoReal).format(valor)

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun ResumoTotalPreview() {
    TemaMeusGastos { ResumoTotal(total = 201.70, quantidade = 4) }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun LinhaGastoPreview() {
    TemaMeusGastos {
        LinhaGasto(Gasto(1, "Almoço no bandejão", 18.50, Categoria.ALIMENTACAO), {})
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun FormularioComErroPreview() {
    TemaMeusGastos {
        FormularioGasto(
            descricao = "Café",
            valorTexto = "abc",
            categoria = Categoria.ALIMENTACAO,
            erro = "O valor precisa ser um número",
            aoDigitarDescricao = {},
            aoDigitarValor = {},
            aoSelecionarCategoria = {},
            aoAdicionar = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun FiltroCategoriasPreview() {
    TemaMeusGastos { FiltroCategorias(selecionada = null, aoFiltrar = {}) }
}

@Preview(showBackground = true)
@Composable
private fun EtiquetaPreview() {
    TemaMeusGastos { Etiqueta("Alimentação", selecionada = true, aoClicar = {}) }
}
