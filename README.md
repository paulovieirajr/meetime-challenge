# Meetime Challenge - Integração com HubSpot :computer:

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=Spring-Boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=Docker&logoColor=white)
![HubSpot](https://img.shields.io/badge/HubSpot-FF7A59.svg?style=for-the-badge&logo=HubSpot&logoColor=white)
![Render](https://img.shields.io/badge/Render-000000.svg?style=for-the-badge&logo=Render&logoColor=white)

## Índice

- [Sobre a aplicação](#sobre-a-aplicação)
- [Objetivos](#objetivos)
- [Observações](#observações)
- [Diagrama de sequência](#diagrama-de-sequência)
- [Motivações](#motivações)
  - [Spring Boot](#spring-boot)
  - [Open Feign](#open-feign)
  - [Spring Bean Validation](#spring-bean-validation)
  - [Thymeleaf](#thymeleaf)
  - [Swagger](#swagger)
- [Melhorias](#melhorias)
- [Como executar a aplicação](#como-executar-a-aplicação)
  - [Render](#render)
  - [Local](#local)
    - [Docker Compose](#docker-compose)
    - [Intellij IDEA](#intellij-idea)


## Sobre a aplicação

Construída em Java 21 com Spring Boot, foi utilizado OpenFeign para conectar com o HubSpot. A aplicação conta com logs semânticos, na qual é possível identificar cada fluxo com
clareza, testes unitários com JUnit 5 e Mockito. A aplicação conta com configurações para variáveis de ambientes personalizadas, facilitando a alteração das mesma de uma forma centralizada. Foi criado uma "simulação" de Redis para salvar, recuperar, remover e renovar token, como se trata de um desafio, implementar um Redis tornaria o processo mais complicado. Também foi criado um Scheduler, para a cada 5 minutos olhar para o nosso "Redis Simulado" e remover tokens expirados. Para o requisito webhook foi necessário realizar o deploy da aplicação de forma pública. Eu escolhi a Render para isso, eles fornecem uma máquina com 0.1 CPU e 512MB de RAM, suficiente para o propósito do desafio.

## Objetivos

1. Geração da Authorization URL:
    - Endpoint responsável por gerar e retornar a URL de autorização para iniciar o fluxo OAuth com o HubSpot.

2. Processamento do Callback OAuth:
    - Endpoint recebe o código de autorização fornecido pelo HubSpot e realiza a troca pelo token de acesso.

3. Criação de Contatos:
    - Endpoint que faz a criação de um Contato no CRM através da API. O endpoint deve respeitar as políticas de rate limit definidas pela API.

4. Recebimento de Webhook para Criação de Contatos:
    - Endpoint que escuta e processa eventos do tipo "contact.creation", enviados pelo webhook do HubSpot.

## Observações

Abaixo, irei descrever como foi o processo para desenvolver esse desafio, eu acabei optando por alguns passos a mais por uma questão intuitiva, na qual por exemplo, onde criei uma pequena página só
para simular um sessionId que é necessário para a troca com o token, embora o desafio não determinasse isso, eu achei de bom grado, vou explicar os motivos durante essa documentação.

## Diagrama de sequência

![image](https://github.com/user-attachments/assets/064f4000-38b0-4350-92e4-527719529f26)

## Motivações

Vou explicar nessa seção os motivos das libs utilizadas, esse é um requisito do desafio.

### Spring Boot
<details>
  <summary>Detalhes</summary>
</br>
  <p>O motivo é bem simples, como trabalho com Spring Boot há alguns anos, era a decisão mais assertiva dado o timebox curto para entrega do desafio.
  </p>
</details>

### Open Feign
<details>
  <summary>Detalhes</summary>
</br>
  <p>Precisava de um client HTTP para comunicar com o HubSpot, o OpenFeign é simples, declarativo e fácil de usar, não é tão verboso quanto o RestTemplate e facilita os testes.
  </p>
</details>

### Spring Bean Validation
<details>
  <summary>Detalhes</summary>
</br>
  <p>Uma necessidade para validar DTOs de entrada, e no caso do desafio, era necessário, pois precisamos validar o contato que vamos criar.
  </p>
</details>

### Thymeleaf
<details>
  <summary>Detalhes</summary>
</br>
  <p>Aqui um ponto que talvez não era necessário, mas... Como não temos um frontend, e eu queria um fluxo que o access_token não ficasse exposto na tela do
    navegador, então simulei um session_id usando UUID nessa troca do code pelo access_token com HubSpot. O token por si só é salvo em um ConcurrentHashMap(também
    vou contar o porquê logo abaixo, mas basicamente era para "simular" um Redis) e
    é recuperado pelo endpoint do access_token ao informar o session_id.
  </p>
</details>

### Swagger
<details>
  <summary>Detalhes</summary>
</br>
  <p>Acredito que esse seja autoexplicativo, mas é um requisito que julgo indispensável em qualquer projeto que lida com APIs Rest visando a documentação dos endpoints.
  </p>
</details>

## Melhorias

A aplicação cumpre o propósito de integrar com o HubSpot, mas um ponto de melhora que eu enxergo é ter um frontend para que possa ser feito um redirecionamento após o fluxo OAuth2. Sobre o token, na minha visão um Redis encaixaria bem nesse cenário, eu "simulei" usando um ConcurrentHashMap, isso é possível ver na classe [OAuthSessionStore](https://github.com/paulovieirajr/meetime-challenge/blob/main/src/main/java/com/github/paulovieirajr/meetime/token/OAuthSessionStore.java), onde implementei não só o
salvamento do token, mas também a questão da expiração e aproveitei também para implementar a chamada ao HubSpot para o refresh_token quando o access_token estiver expirado.

Um outro ponto, usar Resilience4J para implementar Circuit Breaker e Retry, visto que estamos nos comunicando com uma API externa.

Sobre o deploy, procurei algo simples e de graça, apenas para o propósito do desafio, claro que para um cenário real e produtivo essa opção não seria válida, por inúmeras razões.

## Como executar a aplicação

### Render

:heavy_exclamation_mark: Aqui vai um disclaimer, o Render por padrão desativa máquinas free sem tráfego, porém, é reativada ao ser acionada, basta aguardar alguns minutos até que o serviço seja reativado.

<details>
<summary>Pode ser que essa tela fique carregando até que o serviço fique disponível</summary>
  
![image](https://github.com/user-attachments/assets/b8175c94-acb4-4f53-bc3b-4d47afe8be51)

</details>

Para testar via Render é bem simples, pode ser via [Swagger](https://meetime-challenge-ybpb.onrender.com/swagger-ui/index.html) ou a [Collection Postman](https://github.com/user-attachments/files/19968674/Meetime.-.HubSpot.postman_collection.json).

Eu vou demonstrar via Swagger:

<details>
<summary>1. Recuperar a Authorization URL</summary>

![image](https://github.com/user-attachments/assets/aa4509b0-bb23-43d0-9b5a-f955a221b4e0)

</details>

<details>
<summary>2. Colar esse Authorization URL no navegador e iniciar o fluxo OAuth com o HubSpot, ao final copiar o SessionID que é gerado</summary>

Um ponto importante, caso não tenha uma conta no HubSpot, deverá ser criada para realizar o fluxo, eu realizei tanto o teste usando uma conta fictícia quando a minha conta de teste associada ao app.

![image](https://github.com/user-attachments/assets/58c296e5-3eec-4380-9aef-b801d0453d1f)

</details>

<details>
<summary>3. Eu usei uma conta de teste no passo anterior, para conectar no app não pode ser uma conta de dev</summary>

![image](https://github.com/user-attachments/assets/eb3f9bc2-6934-491b-b554-ab4429e05b14)

</details>

<details>
<summary>4. Copiar o SessionID</summary>

![image](https://github.com/user-attachments/assets/cca4855a-03a2-4ea2-a719-35c7dbcfc030)

</details>

<details>
<summary>5. Pegar o access-token para criar o contato</summary>

![image](https://github.com/user-attachments/assets/7e370332-8250-4c4f-9425-39869e5303f5)

</details>

<details>
<summary>6. Criar um contato informando o access-token</summary>
<br>
É possível criar um contato com a seguinte estrutura, onde apenas email e firstname são obrigatórios:

```json
{
  "email": "string",
  "firstname": "string",
  "lastname": "string",
  "phone": "string",
  "company": "string",
  "website": "string",
  "lifecyclestage": "string"
}
```

Como a Swagger UI tem um bug quando declaramos o header como Authorization, devemos informar o access-token no cadeado, caso contrário não vai funcionar.
![image](https://github.com/user-attachments/assets/a362d5aa-c1ef-4075-bceb-63fd39e5e3f2)

)

</details>

<details>
<summary>7. Contato criado com sucesso</summary>

Caso necessário, também é possível visualizar o contato criado pela UI da plataforma do HubSpot.

![image](https://github.com/user-attachments/assets/84457187-3fb3-45cc-967a-fc54564ffee1)

</details>

<details>
<summary>8. Log no Render com o Webhook acionado após o contato ser criado, repare no id do passo anterior</summary>

Aqui, eu só recupero o evento que o HubSpot envia e realizo um log simples para evidência.

![image](https://github.com/user-attachments/assets/002ccd15-9455-4d89-a73e-0fc264e0e6a9)

</details>

### Local

:heavy_exclamation_mark: Importante ressaltar que o passo 4 do desafio acaba não sendo contemplado de forma local, já que o HubSpot exige um endpoint com o protocolo HTTPS.

Eu vou descrever duas formas, mas em ambas é necessária três variáveis de ambientes que serão enviada via email num arquivo ```.env```:

- HUBSPOT_CLIENT_ID
- HUBSPOT_CLIENT_SECRET
- HUBSPOT_SCOPES

#### Docker Compose

O projeto já contempla um ```docker-compose.yml``` para facilitar o teste local. Os requisitos são bem simples:

- Ter o docker e o docker-compose instalado na máquina
- Colocar o arquivo ```.env``` enviado via email na raiz do projeto.

Tendo isso, basta rodar:

```bash
docker compose up -d
```

Depois, verifique os logs com o comando:

```bash
docker logs meetime-challenge -f
```

Os passos para a criação de contato já foi descrito no passo anterior, acho redundante descrevê-los novamente. 
Basicamente, use o [Swagger Local](http://localhost:8080/swagger-ui/index.html) ou a [Collection Postman](https://github.com/user-attachments/files/19968674/Meetime.-.HubSpot.postman_collection.json).

#### Intellij IDEA

Eu recomendo usar o docker por ser mais prático, porém fica aqui também via Intellij IDEA.

Requisitos:

- Java 21
- Intellij Community Edition

</details>

<details>
<summary>1. Após clonar o projeto, abrir no Intellij</summary>

Buscar o três pontinhos no canto superior e clicar em ```Project Structure```

![image](https://github.com/user-attachments/assets/0f999352-a833-44f1-ba23-c4f1b21fc897)

</details>

<details>
<summary>2. Configurar o Java 21</summary>

![image](https://github.com/user-attachments/assets/20e91fb0-a7b5-483f-a486-21d13831fa74)

</details>

<details>
<summary>3. Configurar as variáveis de ambiente</summary>

Utilizar a seguinte string, substituindo apenas os placeholders ```XXXXXX``` para as três variáveis de ambiente necessárias:

```plaintext
HUBSPOT_CLIENT_ID=XXXXXX;HUBSPOT_CLIENT_SECRET=XXXXXX;HUBSPOT_SCOPES=XXXXXX;SPRING_PROFILES_ACTIVE=LOCAL
```
Para acessar essa configuração, clique ```Shift + Shift``` e escreva ```Run Debug``` e clique em ```Edit Configurations```:

![image](https://github.com/user-attachments/assets/342052a5-deff-4980-abb2-3680c1ebac4e)

Configure a aplicação, da seguinte forma, e colocando a string com as variáveis de ambiente em ```Environment variables```:

![image](https://github.com/user-attachments/assets/caa2a214-d9cf-4804-b66e-5dad26d8fb1e)

</details>



