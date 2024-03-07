# Chat TCP (Protocolo de Control de Transmisión) - David

Este proyecto consiste en un simple chat de texto basado en el protocolo TCP (Protocolo de Control de Transmisión) implementado en Java. El chat consta de dos partes principales: el cliente y el servidor. Los usuarios pueden conectarse al servidor desde diferentes clientes y enviar mensajes de texto a otros usuarios conectados al mismo servidor.

## Contenido del proyecto

El proyecto está dividido en varias clases Java:

1. **ChatClient**: Esta clase representa el cliente del chat. Permite a los usuarios conectarse al servidor, enviar mensajes y recibir mensajes de otros usuarios.

2. **ChatServer**: Esta clase representa el servidor del chat. Escucha las conexiones de los clientes, gestiona las salas de chat y distribuye los mensajes a los usuarios correspondientes.

3. **User**: Esta clase representa a un usuario del chat. Cada usuario tiene un nombre de usuario único y está asociado con un socket de red. Los usuarios pueden unirse y salir de las salas de chat.

4. **Room**: Esta clase representa una sala de chat. Cada sala tiene un nombre único y una lista de usuarios que están actualmente dentro de ella. Los mensajes enviados en una sala de chat son distribuidos a todos los usuarios dentro de esa sala.

## Instrucciones de uso

1. Compilar el proyecto y ejecutar el servidor de chat.

2. Ejecutar uno o varios clientes de chat.

3. Los usuarios pueden ingresar un nombre de usuario y comenzar a enviar mensajes en el chat.

## Funcionalidades

- Los usuarios pueden enviar mensajes de texto a todos los usuarios conectados al mismo servidor.
- Los usuarios pueden crear nuevas salas de chat.
- Los usuarios pueden unirse a salas de chat existentes.
- Los usuarios pueden salir de las salas de chat.
- Los usuarios pueden listar todas las salas de chat disponibles.
- Los usuarios pueden eliminar salas de chat existentes.

## Requisitos

- Java Development Kit (JDK) instalado en el sistema.
