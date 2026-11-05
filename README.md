# Meus Gastos

App Android de controle de gastos em Kotlin e Jetpack Compose, com arquitetura MVVM. É possível cadastrar um gasto, filtrar por categoria, remover itens e ver a quantidade e o total do filtro em reais. Os gastos ficam em memória. A demonstração começa com quatro despesas que somam R$ 201,70.

## Como abrir

1. Extraia o ZIP e abra a pasta `MeusGastos` no Android Studio, pelo menu **File → Open**.
2. Use o JDK 17 do Android Studio ou configure um JDK 17 instalado e sincronize o Gradle. O projeto usa o SDK Android 36.
3. Selecione um emulador ou celular com Android 6.0 (API 23) ou superior e clique em **Run**.

Para instalar a versão de demonstração no celular, o ZIP também inclui o arquivo `MeusGastos-debug.apk`.

No terminal, use `./gradlew :app:assembleDebug :app:testDebugUnitTest`. No Windows, use `gradlew.bat :app:assembleDebug :app:testDebugUnitTest`.

## Onde está cada parte

| Arquivo ou pasta | Responsabilidade |
| --- | --- |
| `model/` | Categorias, despesas e repositórios em memória e de demonstração |
| `viewmodel/` | Formulário, validação, filtro e resumo calculado |
| `ui/TelaGastos.kt` | Tela, componentes sem estado e cinco previews |
| `MainActivity.kt` | Escolhe o repositório e conecta o ViewModel à tela |

Os pacotes ficam em `app/src/main/java/br/unasp/meusgastos/`. Os previews ficam em `ui/TelaGastos.kt` e podem ser vistos pelo modo **Split** ou **Design** no Android Studio.

Para iniciar com a lista vazia, use `GastoRepositoryMemoria()` na fábrica em `MainActivity.kt` e ajuste o import. Para consultar o histórico, rode `git log --reverse --oneline` na pasta do projeto.

- [ARQUITETURA.md](ARQUITETURA.md)
- [ESTRAGOS.md](ESTRAGOS.md)
- [DIARIO_DE_IA.md](DIARIO_DE_IA.md)
