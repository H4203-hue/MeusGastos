# Meus Gastos

App Android de controle de gastos em Kotlin e Jetpack Compose, com arquitetura MVVM. É possível cadastrar um gasto, filtrar por categoria, remover itens e ver a quantidade e o total do filtro em reais. Os gastos ficam em memória. A demonstração começa com quatro despesas que somam R$ 201,70.

## Como abrir

1. Use o Android Studio Narwhal 3 Feature Drop (2025.1.3) ou uma versão posterior compatível com o Android Gradle Plugin 8.13. Extraia o ZIP e abra, em **File → Open**, a pasta interna `MeusGastos` que contém `settings.gradle.kts`.
2. Em **Tools → SDK Manager → SDK Platforms**, instale **Android 16.0 (API 36)** se ele ainda não estiver instalado.
3. Se a IDE pedir o **Gradle JDK**, selecione o JDK embutido do Android Studio (**jbr**, Java 17 ou 21). Depois rode **File → Sync Project with Gradle Files**. A primeira sincronização baixa o Gradle e as dependências.
4. Na barra superior, selecione a configuração **app** e um emulador no **Device Manager**, ou conecte um celular com depuração USB. Clique em **Run ▶**. O dispositivo precisa usar Android 6.0 (API 23) ou superior.

Se você já abriu uma versão anterior do projeto e trocou as dependências na mesma pasta, rode **Build → Clean Project** antes de executar novamente.

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
