# Meetime Challenge - Integração com HubSpot :computer:

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

<details>
  <summary>Spring Boot</summary>
</br>
  <p>O motivo é bem simples, não tenho o conhecimento do outro framework sugerido, 
  e como trabalho com Spring Boot há alguns anos, era a decisão mais assertiva dado o timebox curto para entrega do desafio.
  </p>
</details>

<details>
  <summary>Open Feign</summary>
</br>
  <p>Precisava de um client HTTP para comunicar com o HubSpot, o OpenFeign é simples, declarativo e fácil de usar, não é tão verboso quanto o RestTemplate e facilita os testes.
  </p>
</details>

<details>
  <summary>Spring Bean Validation</summary>
</br>
  <p>Uma necessidade para validar DTOs de entrada, e no caso do desafio, era necessário, pois precisamos validar o contato que vamos criar.
  </p>
</details>

<details>
  <summary>Thymeleaf</summary>
</br>
  <p>Aqui um ponto que talvez não era necessário, mas... Como não temos um frontend, e eu queria um fluxo que o access_token não ficasse exposto na tela do
    navegador, então simulei um session_id usando UUID nessa troca do code pelo access_token com HubSpot. O token por si só é salvo em um ConcurrentHashMap(também
    vou contar o porquê logo abaixo, mas basicamente era para "simular" um Redis) e
    é recuperado pelo endpoint do access_token ao informar o session_id.
  </p>
</details>

<details>
  <summary>Swagger</summary>
</br>
  <p>Acredito que esse seja autoexplicativo, mas é um requisito que julgo indispensável em qualquer projeto que lida com APIs Rest visando a documentação dos endpoints.
  </p>
</details>

