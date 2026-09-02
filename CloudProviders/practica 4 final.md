# Práctica Comparativa de Proveedores de Nube con PoC

## 1. Introducción y Contexto

La computación en la nube ha consolidado el modelo predominante para el despliegue de sistemas de software a escala global. Sin embargo, la oferta de proveedores es amplia y heterogénea; cada plataforma presenta diferencias significativas en costos, modelos de seguridad, rendimiento, experiencia de desarrollo, ecosistema de servicios gestionados y compatibilidad con lenguajes de programación.

La selección de un proveedor de nube no es una decisión meramente comercial: condiciona la arquitectura, el presupuesto operativo, la postura de seguridad y la velocidad de entrega del equipo de desarrollo. Por ello, el ingeniero en sistemas debe ser capaz de evaluar objetivamente las alternativas disponibles antes de comprometer recursos y esfuerzo técnico.

Esta práctica tiene como propósito que el estudiante realice un **análisis comparativo riguroso combinado con PoC** en los principales proveedores de infraestructura en la nube, aplicando criterios técnicos y de negocio relevantes para la industria del software.

---

## 2. Objetivos

1. **Identificar y describir** los servicios fundamentales de computación, almacenamiento, bases de datos y serverless en los principales proveedores de nube.
2. **Comparar objetivamente** proveedores de nube bajo criterios técnicos y económicos: precio, seguridad, velocidad, experiencia de usuario para desarrolladores, integración con lenguajes de programación, y disponibilidad de herramientas gestionadas.
3. **Implementar pruebas de concepto (PoC)** en múltiples plataformas de nube utilizando un stack tecnológico de libre elección, documentando el proceso, la curva de aprendizaje y los resultados obtenidos.
4. **Mapear servicios equivalentes** entre plataformas y evaluar su comportamiento real frente a la documentación teórica.
5. **Fundamentar una recomendación técnica** para un escenario de despliegue real, basada tanto en investigación documental como en evidencia empírica obtenida de los PoCs.

---

## 3. Caso de Estudio: Despliegue de UniMarket CUNOC en la Nube

### 3.1 Planteamiento del Problema

El proyecto **UniMarket CUNOC** ha sido desarrollado y ahora debe desplegarse en un entorno de producción en la nube. El equipo de desarrollo no ha definido aún qué proveedor utilizará, y se requiere una evaluación comparativa —técnica y empírica— antes de tomar la decisión.

El sistema requiere como mínimo los siguientes componentes de infraestructura:

- Instancias de computación para hospedar los microservicios.
- Almacenamiento de objetos para archivos estáticos (imágenes de productos, documentos).
- Base de datos gestionada para la persistencia de datos por servicio.
- Funciones serverless para tareas eventuales (procesamiento de notificaciones, validaciones).
- Redes, balanceo de carga y políticas de seguridad.

### 3.2 Proveedores a Evaluar

Se deberá comparar y realizar PoC obligatoriamente en los siguientes tres proveedores:

1. **Amazon Web Services (AWS)**
2. **Google Cloud Platform (GCP)**
3. **Microsoft Azure**

Además, se deberá incluir un **cuarto proveedor de libre elección** (por ejemplo: DigitalOcean, IBM Cloud, Oracle Cloud Infrastructure, Alibaba Cloud, Hetzner Cloud, Vultr, Linode/Akamai, u otro). La elección del cuarto proveedor deberá justificarse en el informe.

---

## 4. Desarrollo de la Práctica

Se deberá realizar las siguientes actividades de forma individual, documentando cada una de ellas en un informe técnico.

### 4.1 Mapeo de Servicios Equivalentes

Para cada uno de los cuatro proveedores evaluados, se deberá identificar el servicio equivalente a las siguientes capacidades de AWS. Se deberá presentar una tabla comparativa que incluya: nombre del servicio en cada plataforma, tipo de servicio (IaaS, PaaS, SaaS, FaaS), y una breve descripción funcional.

| Capacidad / Servicio de Referencia (AWS) | Descripción Mínima |
|---|---|
| **EC2** (Compute) | Instancias de máquinas virtuales escalables bajo demanda. |
| **S3** (Object Storage) | Almacenamiento de objetos con alta durabilidad y disponibilidad. |
| **RDS** (Managed Relational Database) | Bases de datos relacionales gestionadas (PostgreSQL, MySQL, etc.). |
| **Lambda** (Serverless / FaaS) | Ejecución de código en respuesta a eventos sin aprovisionar servidores. |
| **VPC** (Networking) | Red privada virtual con control de subnets, tablas de ruteo y firewalls. |
| **IAM** (Identity and Access Management) | Gestión de identidades, roles y permisos de acceso a recursos. |
| **CloudWatch / CloudTrail** (Observabilidad) | Monitoreo, logging y trazabilidad de operaciones en la nube. |

Se podrán agregar servicios adicionales si se consideran relevantes para el caso de estudio, siempre documentando el porqué de su inclusión.

### 4.2 Comparativa por Dimensiones Técnicas

Para cada proveedor, se deberá analizar y documentar los siguientes criterios de comparación:

#### 4.2.1 Precio y Modelo de Costos
- Se deberá investigar el modelo de precios de cada proveedor para los servicios mapeados.
- Se deberá identificar si existe capa gratuita (free tier) y sus límites.
- Se deberá estimar un costo mensual aproximado para el despliegue del caso de estudio (configuración de entrada: 2 instancias de computación pequeñas, 1 base de datos gestionada, 1 bucket de almacenamiento, 1 millón de ejecuciones serverless). Los cálculos deberán mostrar la fuente y la fecha de consulta.
- Se deberá mencionar factores ocultos o costos indirectos (transferencia de datos, egress, storage de backups, soporte técnico).

#### 4.2.2 Seguridad
- Se deberá describir las capacidades de seguridad ofrecidas por cada proveedor: cifrado en tránsito y en reposo, gestión de identidades (IAM), compliance (certificaciones ISO, SOC, HIPAA si aplica), firewalls y grupos de seguridad, y detección de amenazas.
- Se deberá evaluar si la seguridad es responsabilidad compartida y cómo se documenta ese modelo en cada plataforma.

#### 4.2.3 Velocidad y Rendimiento
- Se deberá investigar la latencia promedio de red, la disponibilidad de regiones y zonas de disponibilidad geográficamente cercanas a Guatemala o Latinoamérica.
- Se deberá mencionar opciones de CDN y edge computing si están disponibles.
- Se deberá contrastar el tiempo de arranque (cold start) de las funciones serverless entre proveedores, si existen datos públicos comparativos.

#### 4.2.4 Experiencia de Usuario para Desarrolladores (UX/DevEx)
- Se deberá evaluar la calidad de la consola web, la curva de aprendizaje, la calidad de la documentación técnica, la existencia de SDKs y CLI, y la comunidad de soporte.
- Se deberá documentar si la plataforma facilita o dificulta el trabajo diario de un ingeniero de software.

#### 4.2.5 Integración con Lenguajes de Programación y Frameworks
- Se deberá identificar qué lenguajes de programación son soportados nativamente por los servicios serverless (Lambda y equivalentes) de cada proveedor.
- Se deberá evaluar la calidad de los SDKs oficiales para lenguajes comunes (Node.js, Python, Java, Go, .NET).
- Se deberá mencionar si existen restricciones de runtime, cold starts por lenguaje, o integraciones preferidas con ciertos ecosistemas.

#### 4.2.6 Disponibilidad de Herramientas y Ecosistema
- Se deberá evaluar la amplitud del catálogo de servicios adicionales: machine learning, IoT, análisis de datos, DevOps nativo, registros de contenedores, orquestación Kubernetes gestionada, etc.
- Se deberá mencionar si el ecosistema de terceros (marketplace, integraciones, comunidad open source) es robusto o limitado.

### 4.3 Pruebas de Concepto (PoC)

Se deberá implementar una **prueba de concepto funcional** en cada uno de los cuatro proveedores evaluados. El objetivo del PoC no es desplegar el sistema completo, sino evidenciar empíricamente cómo se comporta cada plataforma frente a un escenario real de uso.

#### 4.3.1 Requisitos del PoC

- **Stack tecnológico de libre elección:** El estudiante deberá seleccionar el lenguaje de programación y framework de su preferencia. Se deberá justificar la elección del stack en el informe.
- **Servicios mínimos a desplegar por proveedor:** En cada una de las cuatro plataformas se deberá desplegar al menos:
  1. **Un servicio de computación** (instancia virtual, contenedor, o función serverless) que ejecute una API REST mínima con dos endpoints: uno de salud (`/health`) y uno que persista y recupere un dato simple (por ejemplo, un registro de producto o usuario).
  2. **Un servicio de base de datos o almacenamiento** (base de datos gestionada, base de datos serverless, o almacenamiento de objetos) que sea utilizado por la API desplegada.
- **Evidencia de funcionamiento:** Se deberá demostrar que cada despliegue es accesible y operativo. Se deberán incluir capturas de pantalla, logs, o grabaciones de pantalla que certifiquen el funcionamiento.
- **Documentación del proceso:** Para cada proveedor se deberá documentar: tiempo invertido en el despliegue, dificultades encontradas, calidad de la documentación consultada, y costo real incurridos (si aplica).

#### 4.3.2 Comparativa Empírica

Con base en la experiencia de los PoCs, se deberá elaborar una sección que compare:

- **Tiempo hasta el primer despliegue funcional** en cada plataforma.
- **Curva de aprendizaje** observada (documentación, CLI, consola web).
- **Dificultades específicas** encontradas en cada proveedor.
- **Costo real** vs. costo estimado teóricamente.
- **Fiabilidad** del servicio desplegado durante la prueba.

### 4.4 Recomendación Fundamentada

Con base en el análisis documental y la evidencia empírica de los PoCs, se deberá:

- Emitir una **recomendación técnica** sobre qué proveedor resulta más adecuado para desplegar el caso de estudio UniMarket CUNOC.
- La recomendación deberá estar fundamentada tanto en datos de la comparativa teórica como en la experiencia práctica de los PoCs.
- Se deberá identificar al menos **una ventaja y una desventaja** de cada proveedor evaluado.
- Se deberá proponer una **arquitectura de despliegue conceptual** en el proveedor recomendado, indicando qué servicios específicos se utilizarían para cada componente del sistema.

### 4.5 Reflexión Crítica

Se deberá responder de manera argumentada:

- ¿Qué tan transferibles son las habilidades aprendidas en un proveedor a otro?
- ¿Qué riesgos conlleva el vendor lock-in en este escenario?
- ¿Bajo qué condiciones sería recomendable una estrategia multi-nube o híbrida para este proyecto?
- ¿Cómo influyó la experiencia práctica (PoC) en la recomendación final frente a lo que se esperaba solo con la investigación teórica?

---

## 5. Entregables

### Documento técnico

- **Informe técnico final (PDF)** que contenga al menos:
  2. **Introducción** al problema de selección de nube y al caso de estudio.
  3. **Mapeo de Servicios:** Tabla comparativa de servicios equivalentes entre los cuatro proveedores.
  4. **Análisis por Dimensiones:** Secciones dedicadas a precio, seguridad, velocidad, UX para desarrolladores, integración con lenguajes, y ecosistema de herramientas.
  5. **Pruebas de Concepto:** Descripción del stack elegido y su justificación, documentación del despliegue en cada proveedor, evidencias de funcionamiento, y comparativa empírica.
  6. **Recomendación:** Justificación técnica del proveedor elegido, arquitectura conceptual de despliegue, ventajas y desventajas de cada plataforma.
  7. **Reflexión Crítica:** Transferibilidad de habilidades, vendor lock-in, estrategias multi-nube, y contraste entre teoría y práctica.
  8. **Referencias** bibliográficas, documentación oficial consultada, y fecha de consulta de precios.

> **Nota:** La elección de las fuentes de información, calculadoras de precios, herramientas de documentación y stack tecnológico para los PoCs es de libre elección. Se deberá justificar en el informe por qué se consideran confiables las fuentes consultadas y por qué se seleccionó el stack técnico.

### Video Explicativo

Se deberá entregar un **video de máximo 15 minutos** en el que el estudiante explique:

1. Los **fundamentos de la computación en la nube** relevantes al caso (IaaS, PaaS, SaaS, FaaS).
2. El **mapeo de servicios equivalentes** entre los cuatro proveedores evaluados.
3. Los **criterios de comparación** aplicados (precio, seguridad, velocidad, UX, integración, ecosistema) y los hallazgos más relevantes.
4. La **experiencia de los PoCs**: stack utilizado, despliegues realizados, dificultades, y comparativa empírica.
5. La **recomendación final** de proveedor para el despliegue de UniMarket CUNOC, argumentada con datos teóricos y evidencia práctica.
6. La **reflexión crítica** sobre vendor lock-in, multi-nube y la diferencia entre teoría y práctica.

El video debe ser claro, estructurado y demostrar comprensión profunda de los temas. Se permite el uso de diapositivas, pantallas compartidas de las consolas de nube, o cualquier recurso visual que apoye la explicación.

---


## Importante

Esta práctica se ha estructurado en **2 semanas** debido a la complejidad inherente de realizar pruebas de concepto en cuatro plataformas de nube distintas. Cada proveedor tiene su propia consola, CLI, modelo de autenticación, y curva de aprendizaje. Una semana resultaría insuficiente para obtener evidencia empírica de calidad en todos los proveedores sin sacrificar el análisis documental.

Se sugiere la siguiente distribución de esfuerzo:

- **Semana 1:** Investigación documental, mapeo de servicios, análisis por dimensiones, y configuración de cuentas e inicio de PoCs en los primeros dos proveedores.
- **Semana 2:** Finalización de PoCs en los cuatro proveedores, comparativa empírica, redacción de la recomendación, reflexión crítica, y producción del video explicativo.