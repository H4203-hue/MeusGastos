# Experimentos de arquitetura

Cada alteração foi isolada em uma cópia do projeto. O código da entrega usa as implementações corrigidas. As verificações abaixo distinguem os resultados de teste e compilação do comportamento previsto na interface.

## 1. Criar o ViewModel no lugar de pedir ao Android

**Previsão.** Se a tela criar um ViewModel novo, o formulário e a lista passam a ler o estado da nova instância. Uma recriação pode fazer os gastos e o texto digitado sumirem.

**Verificação.** A variante usou `GastosViewModel()` no parâmetro padrão de `TelaGastos`. O teste da regra adicionou dois itens e digitou “R” na primeira instância. A segunda instância começou com zero itens e descrição vazia, enquanto a primeira manteve os dois itens e o texto. O efeito ao digitar na tela é uma previsão, pois o teste recriou a instância diretamente, sem executar um emulador.

**Mensagem.** Nenhum erro de compilação. O construtor é válido.

**Correção.** Obter o ViewModel com `viewModel()`, que reutiliza a instância do escopo da Activity, e passar essa instância à tela.

## 2. Trocar a lista observável por uma lista comum

**Previsão.** A despesa entra no repositório, mas a mudança de uma lista Kotlin comum não avisa o Compose. A lista pode continuar antiga até outra alteração observável provocar uma recomposição que leia os itens novamente. Digitar uma letra só revela a mudança se a recomposição reler a lista.

**Verificação.** Com `mutableListOf<Gasto>()`, a leitura de `vm.gastos` deixou de ser registrada pelo observador de snapshots do Compose. O teste de observabilidade falhou.

**Mensagem do teste.** `java.lang.AssertionError: A leitura da lista precisa ser observável pelo Compose`

**Correção.** Usar `mutableStateListOf<Gasto>()` no ViewModel.

## 3. Guardar o total e atualizá-lo só ao adicionar

**Previsão.** A lista muda ao remover um item, mas o total continua com a soma anterior. A troca de filtro também pode deixar o total antigo.

**Verificação.** Três gastos somavam R$ 30,00. Após remover o gasto de R$ 5,00, o teste esperava R$ 25,00 e recebeu R$ 30,00.

**Mensagem do teste.** `java.lang.AssertionError: expected:<25.0> but was:<30.0>`

**Correção.** Calcular o total com `gastosVisiveis.sumOf { it.valor }`.

## 4. Depender da classe concreta

**Previsão.** Ao restringir o parâmetro do ViewModel a `GastoRepositoryMemoria`, a fábrica perde a opção de injetar `GastoRepositoryDemo`, embora os dois ofereçam as mesmas operações.

**Verificação.** A compilação da Activity falhou no ponto em que ela passa o repositório de demonstração para o ViewModel.

**Mensagem do compilador.** `Argument type mismatch: actual type is 'GastoRepositoryDemo', but 'GastoRepositoryMemoria' was expected.`

**Correção.** Declarar o parâmetro como `GastoRepository` e escolher a implementação na Activity.

## 5. Voltar com a validação para o botão

**Previsão.** O botão pode barrar uma descrição em branco na primeira tela. Outra tela que chame `adicionar()` diretamente deixa passar a mesma entrada, a menos que repita a regra.

**Verificação.** Ao mover a condição para o botão, o teste chamou `adicionar()` com descrição vazia e valor válido. O ViewModel aceitou o gasto e `erro` ficou `null`.

**Mensagem do teste.** `java.lang.AssertionError: expected:<Informe a descrição do gasto> but was:<null>`

**Correção.** Manter a validação em `GastosViewModel.adicionar()` e chamar a mesma ação de qualquer tela.

## Verificações do tutorial

- Ao preencher a descrição e deixar o valor em branco, aparece “O valor precisa ser um número”. A decisão fica em `GastosViewModel.kt`.
- A demonstração abre com quatro gastos e soma R$ 201,70. A troca na etapa 6 mudou zero arquivos da pasta `ui/` e zero linhas do ViewModel.
