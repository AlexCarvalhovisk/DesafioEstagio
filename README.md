# Desafio de Estágio — API de Tarefas

API REST desenvolvida como solução para um desafio técnico de estágio, com o objetivo de permitir o **cadastro e a consulta de tarefas** por meio de endpoints REST.

O projeto foi desenvolvido utilizando **Java, Spring Boot, Spring Data JPA e banco de dados H2**, com uma estrutura simples e objetiva, focada nos requisitos apresentados no desafio.

---

## 🚀 Tecnologias utilizadas

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **H2 Database**
* **Maven**

---

## 📋 Funcionalidades

A API permite:

* Cadastrar uma nova tarefa;
* Listar todas as tarefas;
* Buscar tarefas pelo responsável;
* Buscar tarefas por data de entrega;
* Listar somente tarefas pendentes;
* Filtrar tarefas pendentes por responsável;
* Persistir os dados utilizando o banco H2.

---

## 🏗️ Estrutura do projeto

A aplicação foi organizada em três pacotes principais:

```text
src
└── main
    └── java
        └── br.com.alexcarvalho.DesafioEstagio
            ├── controller
            │   └── TarefaController.java
            │
            ├── entity
            │   └── Tarefa.java
            │
            └── repository
                └── TarefaRepository.java
```

### Entity

A classe `Tarefa` representa a entidade persistida no banco de dados.

Possui os seguintes atributos:

| Campo         | Tipo        | Descrição                            |
| ------------- | ----------- | ------------------------------------ |
| `id`          | `Long`      | Identificador gerado automaticamente |
| `titulo`      | `String`    | Título da tarefa                     |
| `descricao`   | `String`    | Descrição da tarefa                  |
| `responsavel` | `String`    | Responsável pela tarefa              |
| `dataEntrega` | `LocalDate` | Data de entrega                      |
| `concluida`   | `boolean`   | Indica se a tarefa foi concluída     |

O campo `id` utiliza geração automática através do JPA, enquanto `LocalDate` foi utilizado para representar a data de entrega sem informação de horário.

### Repository

A interface `TarefaRepository` estende `JpaRepository`, permitindo utilizar os recursos de persistência do Spring Data JPA.

Também foram utilizadas **query methods**, permitindo criar consultas através da nomenclatura dos métodos, sem a necessidade de escrever SQL manualmente.

Entre as consultas implementadas estão:

* Busca por responsável ignorando maiúsculas e minúsculas;
* Busca por data de entrega;
* Busca por tarefas não concluídas;
* Busca por tarefas não concluídas de determinado responsável.

### Controller

O `TarefaController` disponibiliza os endpoints REST da aplicação através do caminho base:

```text
/tarefas
```

O Controller recebe as requisições HTTP e utiliza o `TarefaRepository` para realizar as operações de persistência e consulta.

---

# 🔗 Endpoints

## 1. Cadastrar tarefa

### `POST /tarefas`

Cadastra uma nova tarefa no banco H2.

### Exemplo de requisição

```json
{
    "titulo": "Implementar login",
    "descricao": "Criar autenticação inicial do sistema",
    "responsavel": "Lucas",
    "dataEntrega": "2026-09-10",
    "concluida": false
}
```

### Exemplo de resposta

```json
{
    "id": 1,
    "titulo": "Implementar login",
    "descricao": "Criar autenticação inicial do sistema",
    "responsavel": "Lucas",
    "dataEntrega": "2026-09-10",
    "concluida": false
}
```

A API retorna **HTTP 201 Created** após o cadastro.

---

## 2. Listar todas as tarefas

### `GET /tarefas`

Retorna todas as tarefas cadastradas.

### Exemplo

```text
GET http://localhost:8080/tarefas
```

---

## 3. Buscar tarefas por responsável

### `GET /tarefas?responsavel={nome}`

Retorna tarefas cujo responsável contenha o texto informado.

A busca é **case-insensitive**, portanto diferentes combinações de letras maiúsculas e minúsculas retornam os mesmos resultados.

### Exemplos

```text
GET /tarefas?responsavel=Lucas
```

```text
GET /tarefas?responsavel=lucas
```

```text
GET /tarefas?responsavel=LUCAS
```

As três consultas podem encontrar tarefas atribuídas a `Lucas`.

Também é possível utilizar apenas parte do nome, devido ao uso de `ContainingIgnoreCase`.

---

## 4. Buscar tarefas por data de entrega

### `GET /tarefas/data?data={data}`

Retorna as tarefas que possuem a data de entrega informada.

### Exemplo

```text
GET /tarefas/data?data=2026-09-10
```

A data deve seguir o formato:

```text
yyyy-MM-dd
```

Exemplo:

```text
2026-09-10
```

---

## 5. Listar tarefas pendentes

### `GET /tarefas/pendentes`

Retorna somente as tarefas que ainda não foram concluídas.

Em outras palavras:

```text
concluida = false
```

### Exemplo

```text
GET /tarefas/pendentes
```

---

## 6. Listar tarefas pendentes por responsável

### `GET /tarefas/pendentes?responsavel={nome}`

Combina os filtros de:

* tarefa não concluída;
* responsável contendo o texto informado;
* busca sem diferenciação entre letras maiúsculas e minúsculas.

### Exemplo

```text
GET /tarefas/pendentes?responsavel=Lucas
```

---

# 🧪 Cenário de teste

Para validar a implementação, podem ser cadastradas as seguintes tarefas.

### Tarefa 1

```json
{
    "titulo": "Implementar login",
    "descricao": "Criar autenticação inicial do sistema",
    "responsavel": "Lucas",
    "dataEntrega": "2026-09-10",
    "concluida": false
}
```

### Tarefa 2

```json
{
    "titulo": "Corrigir cadastro",
    "descricao": "Corrigir problemas no cadastro",
    "responsavel": "Maria",
    "dataEntrega": "2026-09-10",
    "concluida": true
}
```

### Tarefa 3

```json
{
    "titulo": "Criar endpoint de produtos",
    "descricao": "Implementar API de produtos",
    "responsavel": "Lucas",
    "dataEntrega": "2026-09-10",
    "concluida": false
}
```

Após o cadastro, alguns testes possíveis são:

| Requisição                                 | Resultado esperado |
| ------------------------------------------ | ------------------ |
| `GET /tarefas`                             | 3 tarefas          |
| `GET /tarefas?responsavel=Lucas`           | 2 tarefas          |
| `GET /tarefas?responsavel=LUCAS`           | 2 tarefas          |
| `GET /tarefas/data?data=2026-09-10`        | 3 tarefas          |
| `GET /tarefas/pendentes`                   | 2 tarefas          |
| `GET /tarefas/pendentes?responsavel=Lucas` | 2 tarefas          |
| `GET /tarefas/pendentes?responsavel=Maria` | Nenhuma tarefa     |

---

# 💾 Banco de dados

O projeto utiliza o **H2 Database**, permitindo executar a aplicação sem a necessidade de configurar um banco de dados externo.

O H2 foi escolhido por ser adequado para testes e desafios técnicos, facilitando a execução e a validação da API.

---

# ▶️ Como executar o projeto

### 1. Clone o repositório

```bash
git clone <URL_DO_REPOSITORIO>
```

### 2. Acesse a pasta do projeto

```bash
cd DesafioEstagio
```

### 3. Execute a aplicação

Utilizando Maven:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Ou execute a classe principal da aplicação diretamente pela IDE.

A API estará disponível em:

```text
http://localhost:8080
```

---

# 📌 Conceitos praticados

Este projeto foi desenvolvido com foco na prática dos seguintes conceitos:

* Desenvolvimento de API REST;
* Spring Boot;
* Injeção de dependência;
* Spring Data JPA;
* Mapeamento de entidades;
* Persistência de dados;
* Query Methods;
* Parâmetros de consulta (`@RequestParam`);
* Requisições `POST` e `GET`;
* Códigos de status HTTP;
* Utilização de `LocalDate`;
* Banco de dados H2;
* Filtros e consultas combinadas.

---

# 👨‍💻 Objetivo do projeto

Este projeto faz parte dos meus estudos e preparação para oportunidades na área de desenvolvimento de software, com foco no ecossistema **Java e Spring Boot**.

A proposta foi desenvolver uma solução simples, funcional e alinhada aos requisitos apresentados no desafio, priorizando a compreensão dos fundamentos de uma API REST com Spring Boot e Spring Data JPA.

---

## 👨‍💻 Autor

**Desconhecido**

Eu como intusiasta em desenvolvimento **backend Java e Spring Boot**, interessado em construção de APIs, integração de sistemas, bancos de dados e evolução contínua no ecossistema Java, busquei aplicar todos os ensinamentos transmitidos para que eu pudesse cada vez mais consolidar o entendimeto nessa tecnologia e praticar o que tenho aprendido diariamente.

### Tecnologias e ferramentas

```text
Java • Spring Boot • Spring Data JPA • Hibernate
H2 • REST API • Maven • Git
```

## 🎓 Agradecimentos

Este projeto foi desenvolvido aplicando os conceitos e boas práticas ensinados pelo **Professor Matheus Leandro Ferreira**, uma referência como professor acadêmico e profissional em desenvolvimento Java de extrema relevância no YouTube.

## 📄 Licença

Projeto desenvolvido para fins de estudo e portfólio.
