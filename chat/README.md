# Man-Chat 💬

Una aplicación de chat en tiempo real construida con **Spring Boot**, **WebSocket**, **STOMP** y **Apache Kafka**. Los mensajes se transmiten instantáneamente entre clientes conectados y se procesan a través de una cola de mensajes robusta.

## 🎯 Características

- ✅ **Chat en tiempo real** - Comunicación bidireccional con WebSocket/STOMP
- ✅ **Múltiples usuarios** - Cada cliente puede conectarse con un nombre de usuario único
- ✅ **Integración con Kafka** - Los mensajes se procesan a través de Apache Kafka
- ✅ **Interfaz oscura moderna** - UI responsiva y minimalista
- ✅ **Timestamps** - Cada mensaje incluye hora de envío
- ✅ **Diferenciación de mensajes** - Estilo visual diferente para mensajes propios vs recibidos

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                     FRONTEND (JavaScript)               │
│  - HTML5 + CSS3                                         │
│  - SockJS + STOMP para WebSocket                        │
│  - Gestión de usuarios y mensajes                       │
└────────────────┬────────────────────────────────────────┘
                 │ WebSocket (/ws)
                 ↓
┌─────────────────────────────────────────────────────────┐
│            SPRING BOOT (Backend)                        │
│  ┌──────────────────────────────────────────────────┐  │
│  │ WSChatsController                                │  │
│  │ @MessageMapping("/chat1")                        │  │
│  └──────────────┬───────────────────────────────────┘  │
│                 │                                       │
│  ┌──────────────▼───────────────────────────────────┐  │
│  │ ProducerService (Kafka)                          │  │
│  │ - Envía mensajes al topic "canal-de-la-mancha"  │  │
│  └──────────────┬───────────────────────────────────┘  │
│                 │                                       │
│  ┌──────────────▼───────────────────────────────────┐  │
│  │ ConsumerListener (Kafka)                         │  │
│  │ - Consume mensajes del topic                     │  │
│  └──────────────┬───────────────────────────────────┘  │
│                 │                                       │
│  ┌──────────────▼───────────────────────────────────┐  │
│  │ WebSocketService                                 │  │
│  │ - Reenvía mensajes a clientes conectados        │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────┬────────────────────────────────────────┘
                  │ /server/canal-de-la-mancha
                  ↓
┌─────────────────────────────────────────────────────────┐
│              APACHE KAFKA                               │
│  Topic: "canal-de-la-mancha" (2 particiones, 1 replica)│
└─────────────────────────────────────────────────────────┘
```

## 📋 Prerrequisitos

- **Java 17+**
- **Maven 3.6+**
- **Apache Kafka** (recomendado ejecutar con Docker)
- **Node.js** (opcional, solo si deseas usar npm)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone <tu-repo>
cd chat
```

### 2. Iniciar Apache Kafka (con Docker)

```bash
docker run -d \
  --name kafka \
  -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  confluentinc/cp-kafka:7.0.0
```

O usa `docker-compose.yml`:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.0.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.0.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
      - "29092:29092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

### 3. Compilar e iniciar la aplicación

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/chat/
│   │   ├── ChatApplication.java               # Clase principal Spring Boot
│   │   ├── config/
│   │   │   ├── kafka/
│   │   │   │   ├── consumerKafka/
│   │   │   │   │   └── ConsumerAdminConfig.java
│   │   │   │   ├── listeners/
│   │   │   │   │   └── ConsumerListener.java  # Escucha mensajes de Kafka
│   │   │   │   └── producerKafka/
│   │   │   │       ├── ProducerAdminConfig.java
│   │   │   │       └── ProducerFactoryConfig.java
│   │   │   └── webSocketConfig/
│   │   │       └── WebSocketConfig.java       # Configuración STOMP
│   │   ├── controller/
│   │   │   └── WSChatsController.java         # Controlador WebSocket
│   │   ├── dto/
│   │   │   └── ClientMessage.java             # Record del mensaje
│   │   └── service/
│   │       ├── ProducerService.java           # Envío a Kafka
│   │       └── WebSocketService.java          # Broadcast a clientes
│   └── resources/
│       ├── application.properties              # Configuración de la app
│       └── static/
│           ├── app.js                         # Lógica del frontend
│           ├── index.html                     # Estructura HTML
│           └── style.css                      # Estilos
```

## 🔧 Configuración

### `application.properties`

```properties
spring.application.name=chat

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3

spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=chat-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.auto-offset-reset=earliest
```

## 💻 Uso

1. **Abre el navegador** en `http://localhost:8080`
2. **Ingresa tu nombre de usuario** y haz clic en "Unirse"
3. **Escribe mensajes** y presiona Enter o click en "Enviar"
4. **Abre otra ventana/pestaña** con otro usuario para probar en tiempo real

## 📊 Flujo de Mensajes

1. Cliente A envía un mensaje vía WebSocket
2. `WSChatsController` recibe el mensaje en `/app/chat1`
3. `ProducerService` envía el mensaje a Kafka (`canal-de-la-mancha`)
4. `ConsumerListener` consume el mensaje desde Kafka
5. `WebSocketService` reenvía el mensaje a todos los clientes suscritos
6. Todos los clientes reciben el mensaje en `/server/canal-de-la-mancha`

## 🎨 Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| Spring Boot | 3.x | Framework backend |
| Apache Kafka | 7.0.0 | Cola de mensajes |
| WebSocket/STOMP | - | Comunicación en tiempo real |
| SockJS | 1.x | Fallback WebSocket |
| HTML5/CSS3 | - | Frontend |
| JavaScript (Vanilla) | ES6+ | Lógica cliente |
| Maven | 3.6+ | Gestor de dependencias |

## 🐛 Troubleshooting

### La conexión WebSocket falla
- Verifica que Kafka esté corriendo en `localhost:9092`
- Asegúrate de que el puerto 8080 no esté en uso
- Revisa la consola del navegador (F12) para errores

### Los mensajes no aparecen
- Abre la consola del navegador (F12)
- Verifica que el WebSocket esté conectado (logs en verde)
- Comprueba que hayas ingresado un nombre de usuario

### Kafka no funciona
```bash
# Verifica que Kafka está corriendo
docker ps | grep kafka

# Reinicia los contenedores
docker-compose down
docker-compose up -d
```

## 🚀 Mejoras Futuras

- [ ] Persistencia de mensajes en base de datos
- [ ] Autenticación y autorización
- [ ] Salas/canales privados
- [ ] Notificaciones de escritura en tiempo real
- [ ] Emojis y reacciones
- [ ] Historial de mensajes
- [ ] Usuarios en línea

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver `LICENSE` para más detalles.

## 👤 Autor

Creado por **[Tu Nombre]**

## 📞 Contacto

Para preguntas o sugerencias, abre un issue en GitHub.

---

**Hecho con ❤️ usando Spring Boot y Kafka**