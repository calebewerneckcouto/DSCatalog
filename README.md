# Projeto DSCatalog
## Descrição do Projeto

O projeto CWCDev é uma aplicação backend Spring Boot que oferece uma plataforma de gestão de produtos e categorias com integração de autenticação e autorização via OAuth2 e JWT para segurança das APIs. O sistema permite o cadastro, atualização, listagem e exclusão de produtos, categorias e usuários. Além disso, há recursos para recuperação de senha e gerenciamento de permissões de acesso através de roles.

## Estrutura do Projeto

### Pacotes e Arquivos Principais

1. **config**: Contém as classes de configuração do projeto.
    - `AppConfig.java`: Configura beans utilizados na aplicação, como o `PasswordEncoder`.
    - `AuthorizationServerConfig.java`: Configurações do servidor de autorização OAuth2, incluindo endpoints de autenticação e a geração de tokens.
    - `ResourceServerConfig.java`: Configura o servidor de recursos, definindo autorizações para acessos aos recursos.

2. **controllers (/resources)**: Contém os controladores REST que expõem os endpoints da API.
    - `AuthResource.java`: Gerencia operações relacionadas à autenticação e recuperação de senha.
    - `CategoryResource.java`: Expõe endpoints para operações CRUD em categorias.
    - `ProductResource.java`: Expõe endpoints para operações CRUD em produtos.
    - `UserResource.java`: Gerencia as operações CRUD de usuários e detalhes do usuário autenticado.

3. **dto**: Data Transfer Objects usados para transferir dados entre sub-sistemas da aplicação.
    - `CategoryDTO.java`, `ProductDTO.java`, `UserDTO.java`, `EmailDTO.java` entre outros objetivos, encapsulam dados da aplicação.

4. **entities**: Representam as entidades na base de dados e são mapeadas com JPA.
    - `Category.java`, `Product.java`, `User.java` etc.

5. **exceptions**: Classes específicas para lidar com exceções personalizadas.
    - `DatabaseException.java`: Trata exceções de banco de dados como violações de integridade.
    - `ResourceNotFoundException.java`: Trata erros de recursos não encontrados.

6. **repositories**: Interfaces para o acesso aos dados, usando o Spring Data JPA.
    - `CategoryRepository.java`, `ProductRepository.java`, `UserRepository.java` etc.

7. **services**: Camada de serviço que contém a lógica de negócios.
    - `AuthService.java`: Serviços de autenticação e recuperação de senha.
    - `CategoryService.java`, `ProductService.java`, `UserService.java`: Serviços para manipulação das entidades.

8. **validation**: Validações customizadas para DTOs de entrada.
    - `UserInsertValid.java`, `UserUpdateValid.java` etc.

### Endpoints do Projeto

1. **Autenticação e Usuários**
    - POST `/auth/recover-token`: Solicita um token para recuperação de senha.
    - PUT `/auth/new-password`: Redefine a senha usando o token de recuperação.
    - GET `/users/me`: Retorna detalhes do usuário autenticado.
    - POST `/users`: Cadastra um novo usuário.
    - PUT `/users/{id}`: Atualiza um usuário existente.
    - DELETE `/users/{id}`: Exclui um usuário.

2. **Produtos**
    - GET `/products`: Lista todos os produtos.
    - GET `/products/{id}`: Detalhes de um produto específico.
    - POST `/products`: Cria um novo produto.
    - PUT `/products/{id}`: Atualiza um produto existente.
    - DELETE `/products/{id}`: Remove um produto.

3. **Categorias**
    - GET `/categories`: Lista todas as categorias.
    - GET `/categories/{id}`: Detalhes de uma categoria específica.
    - POST `/categories`: Cria uma nova categoria.
    - PUT `/categories/{id}`: Atualiza uma categoria existente.
    - DELETE `/categories/{id}`: Remove uma categoria.
