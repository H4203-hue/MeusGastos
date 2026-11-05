# Arquitetura do Meus Gastos

## 1. E se os dados viessem de uma API?

Eu adicionaria um `GastoRepositoryApi.kt` no pacote `model` e trocaria a instância criada pela fábrica em `MainActivity.kt`. Para uma API real, eu também adicionaria a permissão de internet no manifesto e, se necessário, uma dependência HTTP no Gradle. Se o contrato atual precisar passar a ser assíncrono, eu ajustaria `GastoRepository.kt` e `GastosViewModel.kt` para carregar os dados fora da thread principal e tratar falhas. `TelaGastos.kt` pode continuar igual enquanto receber os mesmos dados e eventos. `Gasto.kt` e `Categoria.kt` também ficam iguais se o repositório traduzir a resposta da API para os tipos do app.

A troca de memória para demonstração foi feita na fábrica em `MainActivity.kt`, com o mesmo contrato. O ViewModel e os arquivos da UI ficaram iguais nessa etapa.

## 2. Por que calcular o total?

O total é a soma dos gastos do filtro atual. Se eu guardar a soma e só atualizá-la quando adicionar algo, remover um gasto de R$ 5,00 de uma lista de R$ 30,00 deixa o resumo em R$ 30,00 quando deveria mostrar R$ 25,00. A soma e a quantidade são calculadas a partir da lista visível.

## 3. O que ganhei com o formulário independente?

Consegui criar um preview com a descrição “Café”, o valor “abc” e a mensagem de erro já visível. Passei esses valores direto ao formulário, com ações vazias. Um formulário que recebesse o ViewModel inteiro exigiria criar e preparar esse objeto só para desenhar o mesmo estado.
