# Borrador — Preguntas para la defensa breve

Respuestas pensadas para esta implementación concreta (Central de Emergencias). Úsalas como base y ajústalas con tus propias palabras antes de la demostración.

## 1. ¿Qué diferencia hay entre Producer, Exchange, Queue y Consumer?

- **Producer**: quien publica el mensaje. Acá es `EmergenciaController`/`EmergenciaProducer`, que recibe el POST HTTP y lo envía a RabbitMQ. No sabe quién va a leer el mensaje ni cuántos consumidores hay.
- **Exchange**: el componente de RabbitMQ que decide a qué cola(s) enviar cada mensaje según su tipo y routing key. Acá es `emergencias.exchange`, de tipo **fanout**.
- **Queue**: el buffer donde el broker almacena los mensajes hasta que alguien los consuma. Acá hay tres: `emergencias.policia`, `emergencias.bomberos`, `emergencias.ambulancia`.
- **Consumer**: quien se suscribe a una cola y procesa los mensajes. Acá son `PoliciaConsumer`, `BomberosConsumer` y `AmbulanciaConsumer`, cada uno con su propio `@RabbitListener`.

El producer nunca habla directamente con el consumer: siempre pasa por el exchange y la cola, lo que los desacopla completamente.

## 2. ¿Por qué Fanout entrega el evento a ambas colas (y a las tres)?

Un exchange **fanout** ignora la routing key y entrega una copia idéntica del mensaje a **todas** las colas que estén bindeadas a él, sin ningún filtro. Como `emergencias.policia`, `emergencias.bomberos` y `emergencias.ambulancia` están las tres bindeadas a `emergencias.exchange`, cada emergencia publicada llega íntegra a las tres, simulando que todas las instituciones se entran al mismo tiempo del mismo incidente.

## 3. ¿Qué sucede cuando el Consumer está apagado y por qué?

El Producer sigue aceptando y publicando normalmente, porque solo depende de que el exchange exista en RabbitMQ, no de que haya un consumidor escuchando. Como las colas son **durables** y el broker sigue corriendo, los mensajes se van acumulando en cada cola con estado "Ready" (pendientes de entrega). Nada se pierde: en cuanto el Consumer vuelve a conectarse, RabbitMQ le entrega todos los mensajes acumulados en orden. Esto es justamente lo que desacopla a Producer y Consumer: uno puede fallar o reiniciarse sin que el otro se vea afectado.

## 4. ¿Por qué los dos @RabbitListener no necesitan dos puertos HTTP?

Porque `@RabbitListener` no escucha HTTP: escucha el protocolo **AMQP** sobre el puerto 5672 del broker. Los listeners no exponen ningún endpoint propio ni reciben conexiones entrantes directas; es RabbitMQ quien les entrega (push) los mensajes de su cola a través de la conexión AMQP ya abierta. Por eso el Consumer ni siquiera incluye `spring-boot-starter-web`: no necesita un servidor HTTP para nada, y por eso ambos `@RabbitListener` conviven en la misma aplicación sin conflicto de puertos.

## 5. ¿Qué ventaja tiene separar la configuración de RabbitMQ de la lógica de negocio?

- Los nombres de exchange/colas quedan centralizados en una sola clase (`RabbitNames`) en vez de repetidos como strings "mágicos" en cada controlador o listener — si cambia un nombre, se edita en un solo lugar.
- `RabbitConfig` concentra la declaración de topología (exchange, colas, bindings, converter), así que controladores y listeners solo se preocupan de la lógica de negocio (qué hacer con una emergencia), no de cómo está armado el broker.
- Facilita testear o reemplazar la lógica de negocio sin tocar la infraestructura de mensajería, y viceversa.
- Hace explícito y auditable qué recursos de RabbitMQ crea la aplicación, en vez de que queden implícitos dentro de métodos de negocio.

## 6. ¿Qué cambiarías para que cada institución recibiera solo ciertos tipos de emergencia?

Cambiaría el exchange de **fanout** a **topic** (o direct). Con topic, la routing key del mensaje publicado podría ser algo como `emergencia.incendio` o `emergencia.robo`, y cada cola se bindearía con un patrón (`emergencia.incendio.#` para Bomberos, `emergencia.robo.#` para Policía, etc.), de modo que el exchange solo enruta el mensaje a las colas cuyo patrón de binding coincide con la routing key, en vez de replicarlo a todas. Esto requeriría que el Producer, al publicar, incluya ahora el `tipo` como parte de la routing key, y ajustar los bindings en el `RabbitConfig` del Consumer.
