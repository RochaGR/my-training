# My Training 🏋️‍♂️

**Aplicativo completo para planejamento e acompanhamento de treinos, promovendo regularidade, organização e motivação na prática de atividades físicas.**

## 📋 Sobre o Projeto

My Training é uma solução moderna para gerenciamento de treinos pessoais, permitindo que usuários acompanhem suas atividades físicas, estabeleçam desafios e monitorem sua evolução ao longo do tempo. O sistema suporta diversos tipos de treino, como corrida, musculação e ciclismo.

### 🎯 Visão Geral

- **Propósito**: Facilitar o planejamento e o acompanhamento de treinos, promovendo a regularidade, organização e motivação na prática de atividades físicas, seja na academia, em casa ou ao ar livre.
- **Público-alvo**: Usuários individuais que desejam acompanhar exercícios e metas pessoais.
- **Requisitos principais**: Registro e acompanhamento de treinos e desafios pessoais.

### ✨ Funcionalidades Principais

- **Gestão de Treinos**: Registro completo de corridas, musculação e ciclismo
- **Exercícios Detalhados**: Acompanhamento de séries, repetições e cargas
- **Desafios Pessoais**: Crie e monitore suas metas de treino
- **Relatórios**: Visualize sua evolução semanal e mensal
- **Autenticação Segura**: Sistema robusto com JWT e controle de acesso

- <img width="642" height="533" alt="image" src="https://github.com/user-attachments/assets/d2e38274-6421-43c2-b74b-7ce3cf1d4d83" />


## 📦 Escopo do Projeto

### Escopo Mínimo

- ✅ Cadastro e autenticação de usuários
- ✅ CRUD completo para treinos (corrida, musculação e ciclismo)
- ✅ CRUD para desafios pessoais
- ✅ Registro de exercícios individuais associados a treinos
- ✅ Geração de relatórios simples (evolução semanal e mensal)

### Escopo Opcional

- [ ] Filtro e pesquisa de treinos por data e tipo
- [ ] Estatísticas avançadas (melhor tempo, distância total, ranking pessoal)

## 📐 Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:
```

├─ controller/          # Controladores REST
│  ├─ dto/              # DTOs de entrada e saída
│  ├─ model/            # Entidades JPA
│  ├─ repository/       # Repositórios
│  ├─ service/          # Lógica de negócio
│  ├─ configuration/    # Configurações de segurança
│  └─ util/             # Utilitários
├─ src/main/resources/
│  ├─ application.yml   # Configurações
│  └─ db/migration/     # Scripts Flyway

```

### Principais Entidades

- **Usuario**: Gerenciamento de contas e autenticação
- **Treino**: Registro de atividades físicas (corrida, musculação, ciclismo)
- **Exercicio**: Detalhes de exercícios individuais (séries, repetições, carga)
- **Desafio**: Metas e objetivos pessoais

## 📁 Estrutura do Projeto

### Backend
```

├─ src/main/java/com/senai/projeto/mytraining/
│  ├─ controller/       # Controladores REST
│  ├─ dto/              # DTOs de entrada e saída
│  ├─ model/            # Entidades JPA
│  ├─ repository/       # Repositórios
│  ├─ service/          # Lógica de negócio
│  ├─ configuration/    # Configurações de segurança
│  └─ util/             # Utilitários
├─ src/main/resources/
│  ├─ application.yml   # Configurações
│  └─ db/migration/     # Scripts Flyway
└─ src/test/            # Testes unitários
```

### Frontend
```

├─ src/
│  ├─ api/              # Serviços de API
│  ├─ components/       # Componentes reutilizáveis
│  ├─ screens/          # Telas do app
│  ├─ navigation/       # Configuração de rotas
│  ├─ hooks/            # Hooks personalizados
│  └─ assets/           # Imagens e ícones
└─ package.json
```

## 🔐 Autenticação

O sistema utiliza JWT (JSON Web Tokens) para autenticação. Para acessar endpoints protegidos:

1. Faça login através do endpoint `/api/auth/login`
2. Utilize o token retornado no header `Authorization: Bearer {token}`
3. O sistema suporta diferentes níveis de acesso através de roles

## 📚 Documentação da API

Após iniciar o backend, acesse a documentação Swagger em:
```
http://localhost:8080/swagger-ui.html
```

---
