# bancoxyzBackend

API REST Spring Boot para BancoXYZ. Expone CRUD de **clientes**, **cuentas** y **transacciones**.

```
Cliente 1 --- * Cuenta 1 --- * Transaccion
```

- `TipoCuenta`: `AHORRO`, `PRESTAMO`, `HIPOTECA`
- `TipoTransaccion`: `DEPOSITO`, `RETIRO`, `COMPRA`, `PAGO`

Al arrancar, `DataSeeder` carga 7 clientes (nombres del CSV de intereses), cuentas `101`-`120` y movimientos de 2024. Si la tabla `clientes` ya tiene filas, no vuelve a insertar.

## Requisitos

- Java 21
- Maven 3.9+

## Arranque local (H2)

```bash
mvn spring-boot:run
```

H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
JDBC URL: `jdbc:h2:mem:bancoxyz` / user `sa` / password vacio.

## Oracle Cloud

```bash
export SPRING_PROFILES_ACTIVE=oracle
export ORACLE_WALLET_DIR=/ruta/al/wallet
export ORACLE_TNS_ALIAS=nombredb_tp
export DB_USERNAME=ADMIN
export DB_PASSWORD='tu-password'
mvn spring-boot:run
```

Misma forma de conexion que gestionPedidos (`jdbc:oracle:thin:@ALIAS?TNS_ADMIN=wallet`).

Desactivar semilla:

```bash
export APP_SEED_ENABLED=false
```

## Endpoints

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Cliente por id |
| GET | `/api/clientes/{id}/detalle` | Cliente + cuentas + transacciones |
| GET | `/api/clientes/{id}/cuentas` | Cuentas del cliente |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |
| GET | `/api/cuentas` | Listar cuentas |
| GET | `/api/cuentas/{id}` | Cuenta por id |
| GET | `/api/cuentas/{id}/detalle` | Cuenta + cliente + movimientos |
| GET | `/api/cuentas/{id}/transacciones` | Movimientos de la cuenta |
| GET | `/api/cuentas/numero/{numero}` | Buscar por numero |
| GET | `/api/cuentas/tipo/{tipo}` | Filtrar por tipo |
| POST | `/api/cuentas` | Crear cuenta |
| PUT | `/api/cuentas/{id}` | Actualizar cuenta |
| DELETE | `/api/cuentas/{id}` | Eliminar cuenta |
| GET | `/api/transacciones` | Listar transacciones |
| GET | `/api/transacciones/{id}` | Transaccion por id |
| GET | `/api/transacciones/tipo/{tipo}` | Filtrar por tipo |
| GET | `/api/transacciones/fecha/{yyyy-MM-dd}` | Filtrar por fecha |
| POST | `/api/transacciones` | Crear (actualiza saldo) |
| PUT | `/api/transacciones/{id}` | Actualizar (recalcula saldo) |
| DELETE | `/api/transacciones/{id}` | Eliminar (revierte saldo) |

Ejemplo de alta de cliente:

```json
{
  "nombre": "Pedro Soto",
  "edad": 33,
  "email": "pedro.soto@bancoxyz.cl",
  "rut": "18.888.888-8"
}
```

Ejemplo de cuenta:

```json
{
  "numeroCuenta": "130",
  "tipoCuenta": "AHORRO",
  "saldo": 5000,
  "activa": true,
  "clienteId": 1
}
```

Ejemplo de transaccion:

```json
{
  "fecha": "2024-06-15",
  "tipo": "DEPOSITO",
  "monto": 1500,
  "descripcion": "Ingreso extra",
  "cuentaId": 1
}
```

`DEPOSITO` suma al saldo. `RETIRO`, `COMPRA` y `PAGO` restan y validan saldo suficiente.
