# My Training 🏋️‍♂️

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React Native](https://img.shields.io/badge/React%20Native-latest-blue.svg)](https://reactnative.dev/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

> Aplicativo completo para planejamento e acompanhamento de treinos, promovendo regularidade, organização e motivação na prática de atividades físicas.

## 📋 Sobre o Projeto

My Training é uma solução moderna para gerenciamento de treinos pessoais, permitindo que usuários acompanhem suas atividades físicas, estabeleçam desafios e monitorem sua evolução ao longo do tempo. O sistema suporta diversos tipos de treino, como corrida, musculação e ciclismo.

### ✨ Funcionalidades Principais

- **Gestão de Treinos**: Registro completo de corridas, musculação e ciclismo
- **Exercícios Detalhados**: Acompanhamento de séries, repetições e cargas
- **Desafios Pessoais**: Crie e monitore suas metas de treino
- **Relatórios**: Visualize sua evolução semanal e mensal
- **Autenticação Segura**: Sistema robusto com JWT e controle de acesso

## 🚀 Tecnologias

### Backend
- **Java 21+**
- **Spring Boot 3.x**
- **Spring Security** com JWT
- **Spring Data JPA**
- **Flyway** para migração de banco de dados
- **Swagger Documentação**
- **PostgreSQL/MySQL** (banco de dados relacional)
- **Maven** para gerenciamento de dependências

### Frontend
- **React Native**
- **React Navigation**
- **Axios** para comunicação com API
- **Hooks personalizados**

## 📐 Arquitetura

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:
```
Backend (API REST)
├── Controller     → Endpoints REST
├── Service        → Lógica de negócio
├── Repository     → Acesso aos dados
├── Model          → Entidades do domínio
├── DTO            → Objetos de transferência
└── Configuration  → Segurança e JWT
```

### Principais Entidades

- **Usuario**: Gerenciamento de contas e autenticação
- **Treino**: Registro de atividades físicas (corrida, musculação, ciclismo)
- **Exercicio**: Detalhes de exercícios individuais (séries, repetições, carga)
- **Desafio**: Metas e objetivos pessoais


## 📁 Estrutura do Projeto

### Backend
```
rastreadores-exercicios-backend/
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
rastreador-exercicios-app/
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

## 📊 Funcionalidades 

- [ ] Filtros avançados de treinos por data e tipo
- [ ] Estatísticas detalhadas (melhor tempo, distância total)
- [ ] Ranking pessoal de desempenho



---

⭐ Se este projeto te ajudou, considere dar uma estrela!
