#!/bin/bash
: << 'FIN'
Instalación automatica del despliegue Local (Host); con mongoDB dockerize y aplicación API en Host local.
FIN

echo "Inicio de Instalación..."
##ingresado a la carpeta fondos
cd fondos

echo "Copiando archivos de configuración..."
cp .env.example .env
cp docker-compose.yml.host docker-compose.yml
cp src/main/resources/application-dev.properties.host src/main/resources/application-dev.properties

echo "Desplegando..."
docker-compose up -d && ./mvnw clean install && ./mvnw spring-boot:run

echo "Instalación Finalizada..."
