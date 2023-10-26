# Proyecto: Sistema de Geolocalización para Gestión de Repartos en Tiempo Real 🌐🚚

Este proyecto consiste en el desarrollo de un sistema de geolocalización en tiempo real para optimizar la gestión de repartos de manera eficiente y precisa. Aquí tienes algunos detalles clave:

## Descripción 📝

El sistema de geolocalización permite a las empresas de logística y reparto realizar un seguimiento en tiempo real de sus vehículos y rutas de entrega. Un sistema de roles, diferenciando entre administrador, repartidor o cliente, cada uno con diferentes funcionalidades y accesos a la información que es relevante para cada usuario. Utiliza tecnologías de geolocalización avanzadas para proporcionar información en tiempo real sobre la ubicación de los vehículos y la entrega de paquetes. Además se ha desarrollado un sistema inteligente que en todo momento muestre al repartidor la ruta más óptima hacia la entrega más cercana que se va actualizando de forma automática.

## Tecnologías Utilizadas 🛠️

El proyecto se ha desarrollado utilizando un conjunto de tecnologías y herramientas, que incluyen:

- 🌐 [Tecnología de Geolocalización] para el seguimiento en tiempo real de vehículos y entregas. Se ha utilizado la API de Google Maps implementada en Android SDK.
- 🌐 [Base de Datos] para almacenar y gestionar datos de ubicación y rutas. Alojada en AWS mediante su servicio RDS. Base de datos auxiliar en Firebase para implementar una comunicación en tiempo real sobre la ubicación representada en los mapas.
- 🚀 [Lenguaje de Programación] para el desarrollo de la lógica del sistema. Tanto la parte Servidor como Cliente han sido desarrolladas en Java en su última versión y mediante el framework SpringBoot. La API alojada en AWS mediante su servicio Elastic Beanstalk.
- 🧪 [Pruebas] para garantizar la precisión y la calidad del sistema. Pruebas realizadas en diferentes dispositivos con diferentes versiones de Android.

## Contribuciones 👥

Este proyecto ha sido un esfuerzo colaborativo, con contribuciones de varios miembros del equipo. Se ha seguido una metodología ágil para el desarrollo y la gestión de versiones.

## Resultados 📈

El sistema de geolocalización ha mejorado significativamente la eficiencia de las operaciones de reparto, reduciendo los tiempos de entrega y aumentando la satisfacción del cliente.
