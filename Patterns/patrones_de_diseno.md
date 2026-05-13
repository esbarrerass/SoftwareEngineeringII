# Patrones de Diseño

- Autor: Esteban Barrera Sanabria

En el presente documento se describen algunos patrones de diseño en la programación en Python, de la siguiente manera:

1. Dos patrones creacionales (Singleton y Factory Method).
2. Un patrón estructural (Adapter).
3. Un patrón de comportamiento (Observer).
4. Una implementación combinando un creacional con un estructural (Factory Method + Adapter).
5. Una implementación combinando los 3 tipos (Factory Method + Adapter + Observer).

## 1. Singleton (Creacional)

Refiere a una sola instancia global de una clase para que cada "nueva instancia" tenga el mismo punto global de acceso.

```python
class Singleton:
    _instancia = None

    def __new__(cls):
        if cls._instancia is None:
            cls._instancia = super().__new__(cls)
        return cls._instancia


a = Singleton()
b = Singleton()
print(a is b)  # True
```

## 2. Factory Method (Creacional)

La subclase decide qué objeto crear, sin importar mucho la interfaz predefinida.

```python
class Animal:
    def hablar(self): pass

class Perro(Animal):
    def hablar(self): return "Woof"

class Gato(Animal):
    def hablar(self): return "Meow"

def factory(tipo):
    animales = {"perro": Perro, "gato": Gato}
    return animales[tipo]()


animal = factory("perro")
print(animal.hablar())  # Woof
```

Aqui el `factory()` decide qué clase instanciar según un parámetro, el código que crea el objeto está centralizado ahí, y quien llama solo dice `"perro"` sin saber nada de `Perro()` ni `Gato()`.

Si por ejemplo mañana se agrega Pájaro, solo se toca el diccionario, nada más.

## 3. Adapter (Estructural)

Este patrón permite que dos interfaces incompatibles trabajen juntas.

```python
class SensorViejo:
    def leer_fahrenheit(self):
        return 98.6

class AdaptertoCelsius:
    def __init__(self, sensor):
        self.sensor = sensor

    def leer_temperatura(self):
        f = self.sensor.leer_fahrenheit()
        return (f - 32) * 5 / 9


sensor = AdaptertoCelsius(SensorViejo())
print(sensor.leer_temperatura())  # 37.0
```

`SensorViejo` habla fahrenheit, y si la app espera celsius son incompatibles. `AdapterCelsius` se mete en el medio al recibir el sensor viejo en su constructor y expone el método `leer_temperatura()` que la app sí entiende.

La conversión ocurre adentro, invisible para quien lo usa.

Nunca se toca `SensorViejo`.

## 4. Observer (Comportamiento)

El observer nos dice que cuando un objeto cambia, notifica automáticamente a sus suscriptores.

```python
class Evento:
    def __init__(self):
        self._suscriptores = []

    def suscribir(self, fn):
        self._suscriptores.append(fn)

    def notificar(self, dato):
        for fn in self._suscriptores:
            fn(dato)


evento = Evento()
evento.suscribir(lambda x: print(f"Suscriptor 1: {x}"))
evento.suscribir(lambda x: print(f"Suscriptor 2: {x}"))

evento.notificar("nuevo dato")

# Suscriptor 1: nuevo dato
# Suscriptor 2: nuevo dato
```

Evento no sabe quién lo escucha ni cuántos son, solo guarda funciones en una lista y las llama todas cuando algo pasa.

Los suscriptores se registran desde afuera con `suscribir()`, lo que desacopla al emisor del receptor: se puede agregar o quitar suscriptores sin tocar la clase `Evento`.

## 5. Adapter + Factory Method

Sistema de pagos que unifica dos APIs de pago populares pero incompatibles bajo una sola interfaz.

```python
# APIs incompatibles
class PayPal:
    def enviar_pago(self, monto):
        return f"PayPal: pagando ${monto}"

class Stripe:
    def charge(self, amount_cents):
        return f"Stripe: cobrando {amount_cents} centavos"


# Interfaz común
class ProcesadorPago:
    def pagar(self, monto): pass


# Adapters
class AdapterPayPal(ProcesadorPago):
    def __init__(self):
        self._api = PayPal()

    def pagar(self, monto):
        return self._api.enviar_pago(monto)


class AdapterStripe(ProcesadorPago):
    def __init__(self):
        self._api = Stripe()

    def pagar(self, monto):
        return self._api.charge(monto * 100)


# Factory:
def factory_pago(proveedor) -> ProcesadorPago:
    opciones = {
        "paypal": AdapterPayPal,
        "stripe": AdapterStripe,
    }
    return opciones[proveedor]()


proveedor = "stripe" 
procesador = factory_pago(proveedor)
print(procesador.pagar(50))  # Stripe: cobrando 5000 centavos
```

El `factory_pago()` centraliza la decisión de qué adaptador instanciar, pues quien llama solo pasa un string y recibe algo que sabe hacer `.pagar()`.

Ahora, los adapters hacen la traducción a cada API externa, si se agrega MercadoPago, se crea `AdapterMercadoPago` y se agrega al diccionario (solamente).

## 6. Singleton + Adapter + Observer

Se construye un sistema de notificaciones de sensores IoT:

- **Singleton**: Un único gestor central de sensores
- **Adapter**: Unifica sensores con distintas unidades
- **Observer**: Notifica a los suscriptores cuando llega una lectura

```python
# ADAPTER: sensores con interfaces distintas

class SensorTemperatura:
    def leer_celsius(self):
        return 22.0

class SensorHumedad:
    def get_humidity_percent(self):
        return 65.0


class AdapterSensor:
    def __init__(self, sensor, metodo, escala=1, offset=0):
        self._sensor = sensor
        self._metodo = metodo
        self._escala = escala
        self._offset = offset

    def leer(self):
        valor = getattr(self._sensor, self._metodo)()
        return valor * self._escala + self._offset


# OBSERVER: sistema de eventos

class GestorEventos:
    def __init__(self):
        self._suscriptores = []

    def suscribir(self, fn):
        self._suscriptores.append(fn)

    def emitir(self, nombre, valor):
        for fn in self._suscriptores:
            fn(nombre, valor)


# SINGLETON: gestor central (solo uno en toda la app)

class GestorSensores:
    _instancia = None

    def __new__(cls):
        if cls._instancia is None:
            cls._instancia = super().__new__(cls)
            cls._instancia._sensores = {}
            cls._instancia._eventos = GestorEventos()
        return cls._instancia

    def registrar(self, nombre, adapter):
        self._sensores[nombre] = adapter

    def on(self, fn):
        self._eventos.suscribir(fn)

    def leer_todos(self):
        for nombre, adapter in self._sensores.items():
            valor = adapter.leer()
            self._eventos.emitir(nombre, valor)


gestor = GestorSensores()

gestor.registrar("temperatura", AdapterSensor(SensorTemperatura(), "leer_celsius"))
gestor.registrar("humedad",     AdapterSensor(SensorHumedad(), "get_humidity_percent"))

gestor.on(lambda nombre, valor: print(f"[LOG]   {nombre}: {valor}"))
gestor.on(lambda nombre, valor: print(f"[ALERTA] {nombre} alto!") if valor > 60 else None)

gestor.leer_todos()
# [LOG]    temperatura: 22.0
# [LOG]    humedad: 65.0
# [ALERTA] humedad alto!

otro_gestor = GestorSensores()
print(gestor is otro_gestor)  # True
```

- Singleton: `GestorSensores()` llamado dos veces devuelve el mismo objeto, pues un solo gestor maneja todos los sensores de la app.

- Adapter: `AdapterSensor` recibe cualquier sensor y lo normaliza a `.leer()`, sin importar que uno tenga `leer_celsius()` y otro `get_humidity_percent()`. El gestor nunca sabe la diferencia.

- Observer: cuando se llama `leer_todos()`, el gestor emite un evento por cada lectura y todos los suscriptores reaccionan automáticamente, el log y la alerta son independientes entre sí y del gestor.
