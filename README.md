# Central de Emergencias — RabbitMQ (DSY1107 EA2)

Dos aplicaciones Spring Boot independientes que simulan una central de emergencias:

- **central-emergencias-producer** (puerto HTTP 8080): recibe `POST /emergencias` y publica el evento en RabbitMQ.
- **central-emergencias-consumer** (sin HTTP): escucha tres colas (`emergencias.policia`, `emergencias.bomberos`, `emergencias.ambulancia`) vinculadas a un **fanout exchange** (`emergencias.exchange`), por lo que cada evento llega a las tres.

La cola de ambulancia es la extensión opcional de profundización: se agregó sin modificar el Producer.

## Prerrequisitos

- Java 17+ (se probó con JDK 21, target 17)
- Maven 3.9+
- Docker y Docker Compose

## 1. Levantar RabbitMQ

```bash
cd central-emergencias
docker compose up -d
```

- AMQP: `localhost:5672`
- Consola de administración: http://localhost:15672 (usuario/clave: `guest` / `guest`)

Verifica que el contenedor esté `Up` con `docker compose ps` antes de continuar.

## 2. Levantar el Consumer

El Consumer es el dueño de la topología: al arrancar crea el exchange, las 3 colas (durables) y sus bindings.

```bash
cd central-emergencias-consumer
mvn spring-boot:run
```

En la consola de RabbitMQ (pestaña *Exchanges*/*Queues*) deberías ver `emergencias.exchange` (fanout) y las colas `emergencias.policia`, `emergencias.bomberos`, `emergencias.ambulancia`, cada una bindeada al exchange.

## 3. Levantar el Producer

```bash
cd central-emergencias-producer
mvn spring-boot:run
```

## Checkpoint 1 · Funcionamiento normal

1. Con RabbitMQ, Consumer y Producer arriba, enviar (Bruno o `curl`):

   ```bash
   curl -X POST http://localhost:8080/emergencias \
     -H "Content-Type: application/json" \
     -d '{"tipo":"INCENDIO","ubicacion":"Quilicura","descripcion":"Incendio en una bodega"}'
   ```

2. En la consola del Consumer deben aparecer las tres líneas:

   ```
   POLICÍA recibió: INCENDIO | Quilicura | Incendio en una bodega
   BOMBEROS recibió: INCENDIO | Quilicura | Incendio en una bodega
   AMBULANCIA recibió: INCENDIO | Quilicura | Incendio en una bodega
   ```

3. En RabbitMQ Management, mostrar el exchange `emergencias.exchange`, las dos (o tres) colas y sus bindings.

## Checkpoint 2 · Consumidor desconectado

1. Detener **solo** el proceso del Consumer (Ctrl+C). Dejar RabbitMQ y el Producer encendidos.
2. En RabbitMQ Management → *Queues and Streams*, confirmar que las colas existen y están en 0 mensajes.
3. Enviar los tres POST de prueba (están en la colección Bruno):

   | tipo | ubicacion | descripcion |
   |---|---|---|
   | ROBO | Santiago Centro | Robo en un local comercial |
   | INCENDIO | Maipú | Incendio en una vivienda |
   | ACCIDENTE | Providencia | Colisión vehicular |

4. Refrescar *Queues and Streams*: cada cola debe mostrar **3 mensajes Ready**.
5. Reiniciar el Consumer (`mvn spring-boot:run` otra vez). Debe consumir los 9 mensajes acumulados (3 por cola) e imprimirlos en consola.
6. Verificar en Management que las colas vuelven a 0 mensajes pendientes.

> Nota: esto demuestra que el broker retiene mensajes mientras no hay consumidor activo. No demuestra persistencia ante un reinicio del broker (eso requeriría colas/mensajes persistentes adicionales, fuera del alcance de este desafío).

## Estructura del proyecto

```
central-emergencias/
├── docker-compose.yml
├── README.md
├── DEFENSA.md
├── bruno/                           (colección Bruno: abrir esta carpeta como colección)
├── central-emergencias-producer/   (Spring Web + Spring AMQP, puerto 8080)
└── central-emergencias-consumer/   (Spring AMQP, sin servidor HTTP)
```

### Usar la colección Bruno

En Bruno: *Open Collection* → seleccionar la carpeta `central-emergencias/bruno`. Incluye el entorno **Local** (`baseUrl = http://localhost:8080`) y los 4 requests de los checkpoints, en orden.

## Extensión opcional: Ambulancia

`AmbulanciaConsumer` escucha `emergencias.ambulancia`, bindeada al mismo fanout exchange. El Producer no se modificó: al ser fanout, basta con declarar la cola y el binding en el Consumer para que reciba copia de cada evento.
