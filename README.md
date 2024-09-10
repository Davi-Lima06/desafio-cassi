# DESAFIO CASSI

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.3.3
- Maven

## Aplicação requesitos
- ter java 17 + instalado
- maven instalado

## Rodando a aplicação
- vá na raiz do projeto e digite: mvn spring-boot:run
- abra a url no seu navegador: http://localhost:8080

## Parando a aplicação
- no terminal aperte Ctrl + C para parar a aplicação

## Documentação javadoc
- vá até a raiz do projeto e digite o comando: mvn javadoc:javadoc , obs: vai dar alguns erros mas vai gerar o arquivo mesmo assim.
- será gerado um arquivo no caminho: target/site/apidocs/index.html
- abra esse arquivo no seu navegador

## Descrição

O desafio consiste em implementar uma API REST em Java para um sistema de cadastro de produtos. A API permitirá a criação, atualização, leitura e exclusão de produtos, além de aplicar uma lógica de cálculo sobre o preço final do produto com base em certas regras.

## Requisitos Funcionais

### 1. Cadastro de Produto
- O produto deve ter os seguintes atributos:
  - `id`
  - `nome`
  - `descrição`
  - `preço base`
  - `categoria` (Ex.: Eletrônicos, Alimentos, Vestuário)
  - `data de cadastro`

### 2. Endpoints CRUD

- **POST** `/produtos`: Para criar um novo produto.
- **GET** `/produtos`: Para listar todos os produtos.
- **GET** `/produtos/{id}`: Para obter os detalhes de um produto específico.
- **PUT** `/produtos/{id}`: Para atualizar os dados de um produto.
- **DELETE** `/produtos/{id}`: Para excluir um produto.

### 3. Cálculo de Preço Final

- Implementar o endpoint **GET** `/produtos/{id}/preco-final` que retorna o preço final do produto.
- O preço final deve ser calculado com base na categoria do produto, aplicando as seguintes regras:
  - **Eletrônicos**: Adicionar 10% de taxa sobre o preço base.
  - **Alimentos**: Adicionar 5% de taxa sobre o preço base.
  - **Vestuário**: Descontar 5% do preço base.

### 4. Filtros e Ordenação

- O endpoint **GET** `/produtos` deve aceitar filtros opcionais por:
  - `nome`
  - `descrição`
  - `categoria`
  
- Além disso, deve permitir a ordenação dos produtos por:
  - `preço base`
  - `data de cadastro`
  
- A pesquisa por nome ou descrição deve ser flexível, considerando parte da palavra para ser considerada verdadeira a condição.

## Requisitos Técnicos

1. Desenvolver utilizando **JRE 17**.
2. Utilizar **Spring Boot** para construção da API.
3. Utilizar **JPA/Hibernate** com o banco de dados relacional **H2**.
4. Documentar a API utilizando **Swagger**.
5. Implementar **testes unitários**.

## Avaliação

Os seguintes critérios serão considerados para a avaliação:

1. **Qualidade do Código**: Estrutura e organização do código, uso de boas práticas e legibilidade.
2. **Lógica de Negócio**: Corretude e eficiência na implementação das regras de cálculo.
3. **Design da API**: Clareza e consistência na definição dos endpoints.
4. **Documentação**: Clareza e completude na documentação da API e do código.
5. **Testes**: Qualidade e abrangência dos testes unitários.

---
