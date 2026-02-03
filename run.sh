#!/bin/bash

# Script para iniciar o projeto de forma fácil

echo "=========================================="
echo "Sistema de Checklist de Empilhadeiras"
echo "Iniciando Backend..."
echo "=========================================="

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado. Por favor, instale o Java 21."
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java 21 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Por favor, instale o Maven 3.9.12 ou superior."
    exit 1
fi

# Verificar se o PostgreSQL está rodando
if ! nc -z localhost 5432 2>/dev/null; then
    echo "⚠️  PostgreSQL não está rodando na porta 5432."
    echo "Iniciando PostgreSQL via Docker..."
    
    if command -v docker &> /dev/null; then
        docker-compose up -d postgres
        echo "⏳ Aguardando PostgreSQL inicializar..."
        sleep 10
    else
        echo "❌ Docker não encontrado. Por favor, inicie o PostgreSQL manualmente."
        exit 1
    fi
fi

echo ""
echo "✅ Pré-requisitos verificados!"
echo ""

# Compilar o projeto
echo "📦 Compilando o projeto..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Erro ao compilar o projeto."
    exit 1
fi

echo ""
echo "✅ Compilação concluída!"
echo ""

# Executar a aplicação
echo "🚀 Iniciando a aplicação..."
echo ""
echo "A API estará disponível em: http://localhost:8080/api"
echo "Para parar a aplicação, pressione Ctrl+C"
echo ""
echo "Usuário padrão:"
echo "  RE: ADMIN"
echo "  Senha: admin123"
echo ""
echo "=========================================="
echo ""

mvn spring-boot:run
