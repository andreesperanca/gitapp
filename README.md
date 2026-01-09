Visão geral do app

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/334efd64-7d5e-4bea-a472-51e583393a19" />

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/3a8ea25c-3c70-4c22-a01e-281666220864" />

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/0e7b77dd-3d0a-424b-8013-a822fffe15dd" />

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/648f6288-eb4a-44e5-a01f-c42c9f4addb5" />

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/2b6c769f-6ae2-4df6-beae-75ebdbe2b750" />

<img width="720" height="1280" alt="image" src="https://github.com/user-attachments/assets/b92f4b79-efe9-47fc-9c48-a43e8740cbd8" />









Este aplicativo foi desenvolvido como uma atividade de estudo, com o objetivo de aprofundar conhecimentos em desenvolvimento Android, boas práticas de arquitetura, organização de código e uso de bibliotecas modernas.
O app consiste em consumir api do Git, permitindo buscar usuários, e repositórios (informações gerais, pull requests e issues).

O projeto segue os princípios da Clean Architecture, com separação clara de responsabilidades entre as camadas:

Presentation: Camada responsável pela UI (Jetpack Compose) e gerenciamento de estado (ViewModel)
Domain: Contém regras de negócio e UseCases
Data: Responsável pelo acesso a dados (API, cache, repositórios)

Padrões utilizados:
MVVM (Model-View-ViewModel)
MVI (Model-View-Intent)


Decisões técnicas
Kotlin como linguagem principal
Jetpack Compose para construção da UI declarativa
Paging 3 para paginação eficiente de dados
Coroutines + Flow para programação assíncrona e reativa
Koin para injeção de dependência
Retrofit para consumo de APIs REST
StateFlow/SharedFlow para gerenciamento de estado e eventos
Submodularização por camadas

Como rodar o projeto
1. Clone o repositório: git clone https://github.com/andreesperanca/gitapp.git
2. Abra o projeto no Android Studio
3. no gradle.properties coloque a sua key Git em network_key
4. Execute o app em um emulador ou dispositivo físico

Limitações conhecidas
Tratamento básico de erros de rede
Quantidade grande de boilerplate por conta da submodularização

Próximos passos
Criar módulo de buildlogic para diminuir o boilerplate de arquivos gradle
Melhorar cobertura de testes (unitários e instrumentados)
