# Session-Based vs JWT

> Video: https://drive.google.com/file/d/1gNoh27gpk9pa5VzTmvzRSq_DErdGpny8/view?usp=sharing

Comparación entre autenticación por sesiones y por JSON Web Tokens.

HTTP no tiene memoria. Cada petición llega como si fuera la primera.

La solución siempre es la misma. El usuario se autentica una vez y el servidor le entrega una credencial que el navegador presenta después en cada petición. Lo que separa a los dos métodos es qué contiene esa credencial.

En session-based es un identificador que no significa nada por sí mismo. Llega `JSESSIONID=abc123` y el servidor lo busca en su tabla de sesiones para saber quién eres.

En JWT es la información misma, firmada. El servidor no guarda nada porque el token ya trae todo. Verifica la firma con su clave y si cuadra se cree el contenido.

Uno guarda y consulta. El otro firma y verifica.

De ahí salen los dos conceptos. Stateful es que el servidor recuerde algo entre peticiones, que es lo que pasa con sesiones. Stateless es que no recuerde nada, el caso de JWT.

Conviene aclarar que stateless no significa que no haya estado. El estado se mudó al cliente. El token es el estado viajando de ida y vuelta.

Esa mudanza es todo el intercambio del que trata este trabajo. Un servidor que no guarda nada tampoco puede borrar nada.

## El trilema

Uno quiere tres cosas a la vez y no puede tenerlas todas. Seguridad, buena experiencia de usuario y rendimiento.

Si aprieto la seguridad con expiraciones cortas desconecto a la gente a media tarea. Si alargo las sesiones para que sea cómodo amplío la ventana en que una credencial robada sirve. Si elimino la consulta al servidor para ganar rendimiento pierdo la capacidad de revocar, porque ya no hay nada que consultar ni nada que borrar.

Session-based se para del lado del control y paga con escalabilidad. JWT del lado del rendimiento y paga con revocación.

## Cómo funciona cada uno

En sesiones el login valida la contraseña con algún método de encriptación, crea una sesión en el servidor y devuelve una cookie con su identificador. De ahí en adelante cada petición implica buscar ese identificador en la tabla de sesiones. Ese es el costo del método y también su poder, porque si esa entrada se borra la sesión muere en ese instante.

En JWT el login valida igual pero en vez de guardar genera un token firmado. Después cada petición solo verifica la firma. No hay tabla que consultar.

Un JWT tiene tres partes separadas por puntos. El header con el algoritmo, el payload con los datos y la firma.

Una advertencia que se pasa por alto seguido. El payload va firmado pero no cifrado. Cualquiera puede pegarlo en jwt.io y leerlo entero. La firma protege contra modificación, no contra lectura. Ahí nunca deberían ir datos sensibles.

## Seguridad

Los dos comparten el riesgo principal, que es el robo de la cookie. Da igual si adentro va un identificador o un token, quien la tenga suplanta al usuario.

Ambas aplicaciones lo mitigan igual. La cookie va `HttpOnly` para que JavaScript no pueda leerla, lo que corta el vector clásico de XSS, y `SameSite` contra CSRF. En producción faltaría `Secure` para forzar HTTPS.

Las sesiones son vulnerables a fijación de sesión. Se resuelve regenerando el identificador al hacer login, que es lo que Spring Security ya hace solo. También consumen memoria de forma lineal con los usuarios conectados.

JWT tiene dos problemas que las sesiones no tienen. La clave secreta es un punto único de fallo, porque si se filtra cualquiera fabrica tokens válidos para cualquier usuario con cualquier rol. Y un token emitido no se puede invalidar antes de que expire.

## Escalabilidad

Aquí JWT gana sin discusión.

Con sesiones, si el usuario entra por el servidor A su sesión queda en la memoria de A. Cuando la siguiente petición cae en B el 401 es inmediato, porque B no conoce ese identificador.

Las salidas conocidas son tres. Sticky sessions, un almacén compartido tipo Redis, o replicación entre nodos. Ninguna es gratis.

Con JWT el problema no existe. Cualquier servidor con la clave verifica cualquier token y agregar uno nuevo es copiar la configuración. Por eso domina en microservicios, donde cada servicio valida por su cuenta.

## Revocación

Revocar es invalidar una credencial antes de que caduque sola. Es donde más se separan los dos métodos y fue lo que más me interesó medir.

Cuando un usuario cambia su contraseña lo lógico es que las demás sesiones se cierren, porque si la cambio es que sospecho que alguien la sabe. En sesiones funciona así. El backend invalida las demás sesiones de ese usuario y deja viva solo aquella desde donde se hizo el cambio, para no expulsarte a ti mismo. Lo probé con dos navegadores y el que hizo el cambio siguió en 200 mientras el otro pasó a 401 al instante.

En JWT no pasa nada. Los tokens ya emitidos siguen valiendo porque su firma nunca dependió de la contraseña. El servidor no tiene cómo enterarse porque no guardó ninguna lista contra la cual comparar.

Con el bloqueo por administrador es igual. En sesiones se marca al usuario deshabilitado, se invalidan todas sus sesiones y en el panel se ve el contador caer a cero. En JWT el bloqueo impide futuros logins, pero el token que la persona ya tiene sigue entrando hasta que expire. Ni siquiera hay un contador de sesiones activas que mostrar porque no hay nada que contar.

El logout deja la diferencia todavía más clara. En sesiones destruye la sesión en el servidor, así que la credencial deja de servir aunque alguien tuviera copia de la cookie. En JWT solo borra la cookie del navegador y el token sigue funcionando si alguien lo copió antes.

La única revocación real que tiene JWT por diseño es esperar a que caduque.

Esto no significa que no se pueda revocar, pero sí que cuesta. Las tres soluciones habituales son una lista negra de tokens invalidados, tokens de vida muy corta con refresh tokens, o un número de versión por usuario.

Las tres tienen algo en común que me parece la conclusión más interesante del ejercicio. Todas reintroducen estado en el servidor. Todas obligan a consultar algo en cada petición, que es justo lo que hacía atractivo a JWT. Mientras más control de revocación quieres, más se parece tu solución a una sesión.


## Conclusiones

No hay un ganador. Cada método optimiza una esquina distinta del trilema.

En consumo de memoria la diferencia es de orden. Session-based es O(n), porque cada usuario conectado ocupa una entrada en el servidor y la memoria crece de forma lineal con las sesiones abiertas. JWT es O(1), porque el servidor no guarda nada. Mil usuarios o cien mil le cuestan lo mismo.

Hay que matizar de dónde sale esa ventaja. En esta POC las sesiones viven en la memoria del propio servidor, así que ninguno de los dos consulta la base de datos después del login. La diferencia real no es una consulta de más, sino que uno lee estado y el otro no lee nada. Con las sesiones en Redis esa lectura sí se vuelve una consulta externa por petición y ahí la diferencia se nota de verdad.

JWT gana en escalabilidad horizontal y por eso es la opción natural en microservicios. Session-based gana en control, porque el estado ya está ahí y borrarlo es todo lo que hace falta.

Lo que más me llevo de haber construido las dos es que la revocación en JWT no es gratis. Es un intercambio legítimo, pero conviene tomarlo a conciencia y no asumir que JWT es sesiones pero mejor.

Como regla práctica, si necesitas cerrar accesos de inmediato, como en banca, salud o paneles administrativos, entonces sesiones o JWT con estado encima. Si necesitas escalar sin infraestructura compartida y toleras una ventana de minutos, JWT.

En la práctica muchos sistemas usan los dos. Tokens de vida corta entre servicios y una sesión en el borde para el usuario.

## Referencias

> Singh, A. (2026). *Understanding Authentication in Microservices: Comparing Session-Based and JWT*. International Journal of Creative and Open Research in Engineering and Management https://ijcope.org/wp-content/uploads/2026/04/Understanding-Authentication-in-Microservices-Comparing-Session-Based-and-JWT.pdf
