# Práctica: Metodologías Ágiles - Scrum vs. Kanban


---

## 1. Introducción y Contexto

Las metodologías ágiles han transformado la forma en que se gestionan proyectos de software. A diferencia de los enfoques tradicionales predictivos, las metodologías ágiles promueven la adaptabilidad, la entrega continua de valor y la colaboración constante con el cliente.

Esta práctica tiene como propósito que el estudiante aplique de manera práctica los fundamentos de las metodologías ágiles, comparando y utilizando **Scrum** y **Kanban** como marcos de trabajo para la planificación y gestión de un proyecto de software realista.

---

## 2. Objetivos

1. **Comprender y aplicar los principios del Manifiesto Ágil** en un contexto de proyecto real.
2. **Diseñar e implementar un marco de trabajo Scrum** completo, incluyendo roles, ceremonias, artefactos y métricas.
3. **Diseñar e implementar un sistema Kanban** funcional, incluyendo visualización del flujo, límites de trabajo en progreso (WIP) y métricas de flujo.
4. **Redactar historias de usuario** con criterios de aceptación claros y estimarlas utilizando story points.
5. **Comparar críticamente Scrum y Kanban** para determinar en qué contextos cada uno resulta más adecuado.

---

## 3. Caso de Estudio: UniMarket CUNOC

### 3.1 Planteamiento del Problema

La comunidad del CUNOC carece de una plataforma centralizada para el intercambio comercial interno. Estudiantes, docentes y personal administrativo necesitan un medio confiable para publicar productos, buscar ofertas, realizar pedidos y coordinar entregas dentro del campus.

Se ha decidido desarrollar **UniMarket CUNOC**, una plataforma de comercio electrónico interno para la comunidad universitaria. Como parte del equipo de gestión del proyecto, la responsabilidad del estudiante es planificar y estructurar el trabajo necesario para llevar este producto desde la concepción hasta su primer entregable funcional (MVP).

### 3.2 Descripción Arquitectónica Mínima

El sistema se concibe como una arquitectura distribuida basada en microservicios. Cada dominio de negocio operará como un servicio independiente con su propia base de datos, comunicándose con los demás mediante APIs. Un API Gateway centralizará el acceso desde los clientes, y el despliegue se realizará mediante contenedores Docker con un pipeline de integración y entrega continua (CI/CD).

Esta descripción arquitectónica mínima debe tomarse como referencia para que las historias de usuario, tareas y planificación del proyecto reflejen la complejidad real de un sistema distribuido, sin que ello implique que se deba implementar código.

### 3.3 Alcance del MVP

El MVP de UniMarket CUNOC debe contemplar como mínimo las siguientes capacidades del sistema:

- Registro e inicio de sesión de usuarios con suporte de roles.
- Publicación, edición y eliminación de productos por parte de los vendedores.
- Búsqueda y filtrado de productos por parte de los compradores.
- Creación y seguimiento de pedidos con estados definidos.
- Notificaciones a los usuarios sobre eventos relevantes del sistema.

> **Nota:** Esta práctica se centra en la **planificación y gestión ágil** del proyecto. No se debe implementar código ni diseñar.

---

## 4. Desarrollo de la Práctica

Se deberá realizar las siguientes actividades de forma individual, documentando cada una de ellas en un informe técnico estructurado.

### 4.1 Fundamentos Ágiles

- Se deberá resumir los **cuatro valores** y los **doce principios** del Manifiesto Ágil.
- Se deberá explicar cómo cada valor y principio se aplica (o debería aplicarse) en el contexto del proyecto UniMarket CUNOC.
- Se deberá identificar al menos **tres riesgos** que enfrentaría el proyecto si se gestionara con un enfoque tradicional (cascada) en lugar de uno ágil, y argumentar cómo el enfoque ágil mitiga cada uno.

### 4.2 Planificación con Scrum

Se deberá diseñar un marco de trabajo Scrum completo para el desarrollo del MVP de UniMarket CUNOC. Se deberá definir:

- **Roles:** Identificar quiénes ocuparían los roles de Product Owner, Scrum Master y Development Team en este contexto. Justificar las elecciones.
- **Product Backlog:** Crear un Product Backlog con mínimo **15 historias de usuario** que cubran todo el alcance del MVP. Cada historia deberá incluir: descripción en formato "Como [rol], quiero [acción], para que [beneficio]", acceptance criteria en formato Gherkin que sean claros y verificables, y priorización.
- **Estimación:** Asignar story points a cada historia utilizando una escala de libre elección (Fibonacci, potencias de 2, etc.). Justificar el criterio de estimación utilizado.
- **Sprint Planning:** Diseñar la planificación de **dos sprints** de duración fija (la que se considere apropiada). Definir el Sprint Goal de cada uno y asignar las historias correspondientes.
- **Artefactos:** Generar el Sprint Backlog para cada sprint y un **Burndown Chart** proyectado (puede ser manual o generado con herramienta).
- **Ceremonias:** Documentar la estructura propuesta para cada ceremonia de Scrum: Sprint Planning, Daily Standup, Sprint Review y Sprint Retrospective. Incluir duración estimada, participantes y objetivos.

### 4.3 Planificación con Kanban

Se deberá diseñar un sistema Kanban para el mismo proyecto UniMarket CUNOC. Se deberá definir:

- **Tablero Kanban:** Diseñar un tablero con las columnas que se consideren necesarias para visualizar el flujo de trabajo del proyecto. Justificar la elección de cada columna.
- **Límites de WIP:** Establecer límites de trabajo en progreso (WIP) para cada columna del tablero. Justificar los valores elegidos.
- **Políticas de flujo:** Definir las reglas o políticas que rigen el paso de una tarjeta de una columna a otra (Definition of Ready, Definition of Done por etapa).
- **Clases de servicio:** Identificar si existen diferentes tipos de trabajo que requieran tratamiento diferenciado (por ejemplo: estándar, urgente, fecha fija) y cómo se visualizarían en el tablero.
- **Métricas de flujo:** Generar un **Diagrama de Flujo Acumulado (CFD)** proyectado y explicar cómo se interpretaría la estabilidad del flujo a partir de él.
- **Ceremonias/proceso Kanban:** Documentar las actividades de gestión propuestas (replanteamiento del tablero, revisiones de servicio, etc.), su frecuencia y objetivos.

### 4.4 Comparativa Scrum vs. Kanban

Se deberá elaborar un análisis comparativo que incluya:

- **Tabla comparativa** de al menos 8 dimensiones (por ejemplo: estructura de roles, duración de ciclos, métricas, cambios durante el ciclo, ceremonias, artefactos, ideal para...).
- **Recomendación fundamentada:** Argumentar cuál de los dos marcos se recomendaría para el proyecto UniMarket CUNOC y por qué. Considerar el contexto de un equipo universitario con tiempo limitado y recursos variables.
- **Híbridos:** Investigar y mencionar si existen enfoques híbridos (como Scrumban) que podrían ser aplicables, y en qué condiciones.

---

## 5. Entrega

### Informe Técnico de Gestión Ágil

Se deberá entregar un documento pdf que contenga:

1. **Introducción** con datos del estudiante, curso, fecha y título de la práctica.
2. **Sección Scrum:** Roles, Product Backlog (mínimo 15 historias), estimación, Sprint Backlog de 2 sprints, Burndown Chart, y documentación de ceremonias.
3. **Sección Kanban:** Diseño del tablero, límites WIP, políticas de flujo, clases de servicio, CFD proyectado, y documentación del proceso.
4. **Sección Comparativa:** Tabla comparativa, recomendación fundamentada y análisis de híbridos.
5. **Conclusiones** personales sobre la aplicabilidad de las metodologías ágiles en proyectos reales.
6. **Referencias** bibliográficas y de herramientas utilizadas.

### Evidencias Digitales

Además del informe pdf, se deberá entregar:

- **Enlace al board de Scrum** creado en la herramienta de libre elección. El board debe estar configurado con el Product Backlog, los dos sprints planificados y las historias estimadas.
- **Enlace al board de Kanban** creado en la misma u otra herramienta. El tablero debe reflejar el diseño propuesto con columnas, WIP limits y tarjetas de trabajo.
- **Capturas de pantalla** de ambos boards incluidas como anexos en el informe PDF.

> **Nota:** La elección de las herramientas digitales para la creación de los boards es de libre elección. Se deberá justificar en el informe por qué se seleccionó cada herramienta y qué ventajas aportan al ejercicio.

### Video Explicativo

Se deberá entregar un **video de máximo 10 minutos** en el que el estudiante explique:

1. Los **fundamentos básicos** de las metodologías ágiles (valores y principios del Manifiesto Ágil).
2. Los **fundamentos de Scrum**: roles, ceremonias, artefactos y cómo se aplicaron en el caso de estudio.
3. Los **fundamentos de Kanban**: visualización del flujo, WIP limits, métricas y cómo se aplicaron en el caso de estudio.
4. La **comparativa Scrum vs. Kanban** desarrollada en la práctica.
5. Cuál de los dos marcos se prefiere para el proyecto UniMarket CUNOC y **por qué**, argumentando con base en el análisis realizado.

El video debe ser claro y demostrar comprensión profunda de los temas.
