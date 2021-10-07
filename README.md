# Projeto j-exchange

Bem-vindo o j-exchange. Este é um projeto de estudo que demonstra o uso de endpoints
REST reativos junto com integração com serviço externo e banco de dados também em 
modo reativo.

## Execução

### Executando a aplicação localmente

Você vai precisar apenas do Java 11 instalado e configurado.

Para execução local, crie um arquivo chamando `.env` (ponto no início) no diretório
raiz do projeto com o seguinte conteúdo (substituindo `XXXX` pela chave do serviço
exchangesratesapi.io):

```shell script
APP_EXCHANGESRATESAPI_ACCESS_KEY=XXXX
```

Depois, basta o comando abaixo:

```shell script
./mvnw compile quarkus:dev
```

### Executando com Docker

Para execução com Docker, execute os comandos abaixo, lembrando de substituir `XXXX` 
pela chave do serviço exchangesratesapi.io:

```shell script
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/j-exchange-jvm .
docker run -i --rm -e APP_EXCHANGESRATESAPI_ACCESS_KEY=XXXX -p 8080:8080 quarkus/j-ex
change-jvm
```

### Log estruturado

É possível ativar o log estruturado em JSON acrescentando a variável ambiente (assim
como é feito com `APP_EXCHANGESRATESAPI_ACCESS_KEY` acima):

```shell script
QUARKUS_LOG_CONSOLE_JSON=true
```

## Features

Uma documentação básica dos endpoints está disponível em http://localhost:8080/swagger-ui

O projeto dispõe de apenas dois endpoints:

1. Conversão de moeda. Ex: http://localhost:8080/exchange/convert?baseCurrency=USD&targetCurrency=BRL&amount=1.0&userId=user
2. Consulta de transações por usuário: Ex: http://localhost:8080/transactions/user

Apenas quatro moedas estão disponíveis: BRL, EUR, JPY e USD

## Tecnologias

A linguagem utilizada neste projero é Kotlin junto com o framework Quarkus. O objetivo
é estudar o uso de endpoints reativos com Quarkus, incluindo a comunicação com serviços
externos e banco de dados. Por isso o endpoint de conversão realiza esse ciclo completo
de consultar um serviço externo, persistir e devolver a resposta no modelo SmallRye 
Mutiny adotado pelo Quarkus.

Também foram incluídas algumas extensões para tracing e logging.

Quarkus foi uma boa escolha por facilitar muito todas essas integrações. De maneira 
simples é possível desenvolver uma aplicação reativa. A documentação é bastante fácil
e o próprio framework ajuda a evitar códigos de IO com bloqueio.

Kotlin também é uma excelente escolha como linguagem para rodar na JVM, bastante concisa
e poderosa.

## Camadas

O código é dividido em 3 camadas principais além de uma camada de suporte: 

1. `domain`: classes do domínio da aplicação. Pretende-se que o código (não as anotações)
   não tenha dependência com o framework, mas deve conter toda a estrutura básica da 
   aplicação (exceção dos repositórios explicada abaixo). As dependências em forma de 
   interface são supridas pelas outras camadas através da injeção de dependência, mas 
   `domain` não acessa diretamente nenhuma outra camada
2. `client`: todo tipo de acesso externo à aplicação, neste caso, a comunicação com o
   serviço de taxas de conversão
3. `api`: a camada de acesso do mundo externo para a aplicação, basicamente os endpoints
   e classes DTO que acessam o domínio para carregar e processar informações
4. `infra`: não é exatamente uma camada mas serve como suporte para as outras para
   configurações, conversores, etc.

Sobre os `Repository`... seria interessante que houvesse apenas uma interface em `domain`
e a implementação deveria ficar em `client`. Porém, isso traria uma complexidade 
desnecessária. Numa eventual troca de framework, seria necessário refazer o repositório.
Ainda assim em termos de custo/benefício parece valer a pena manter assim.