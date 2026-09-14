package com.duoc.bancoxyz.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.duoc.bancoxyz.model.ClienteEntity;
import com.duoc.bancoxyz.model.CuentaEntity;
import com.duoc.bancoxyz.model.TipoCuenta;
import com.duoc.bancoxyz.model.TipoTransaccion;
import com.duoc.bancoxyz.model.TransaccionEntity;
import com.duoc.bancoxyz.repository.ClienteRepository;
import com.duoc.bancoxyz.repository.CuentaRepository;
import com.duoc.bancoxyz.repository.TransaccionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public DataSeeder(ClienteRepository clienteRepository,
                      CuentaRepository cuentaRepository,
                      TransaccionRepository transaccionRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() > 0) {
            log.info("La base ya tiene clientes. Se omite la carga inicial.");
            return;
        }

        log.info("Cargando datos de prueba BancoXYZ...");

        ClienteEntity alice = guardarCliente("Alice Brown", 35, "alice.brown@bancoxyz.cl", "11.111.111-1");
        ClienteEntity bob = guardarCliente("Bob Johnson", 40, "bob.johnson@bancoxyz.cl", "12.222.222-2");
        ClienteEntity charlie = guardarCliente("Charlie Green", 45, "charlie.green@bancoxyz.cl", "13.333.333-3");
        ClienteEntity diana = guardarCliente("Diana Prince", 30, "diana.prince@bancoxyz.cl", "14.444.444-4");
        ClienteEntity jane = guardarCliente("Jane Smith", 28, "jane.smith@bancoxyz.cl", "15.555.555-5");
        ClienteEntity john = guardarCliente("John Doe", 42, "john.doe@bancoxyz.cl", "16.666.666-6");
        ClienteEntity steve = guardarCliente("Steve Rogers", 38, "steve.rogers@bancoxyz.cl", "17.777.777-7");

        // Cuentas con numeros alineados a los CSV originales (101-120), datos corregidos
        CuentaEntity c101 = guardarCuenta("101", TipoCuenta.AHORRO, "8000", alice);
        CuentaEntity c102 = guardarCuenta("102", TipoCuenta.HIPOTECA, "12000", alice);
        CuentaEntity c103 = guardarCuenta("103", TipoCuenta.PRESTAMO, "7000", jane);
        CuentaEntity c104 = guardarCuenta("104", TipoCuenta.AHORRO, "10000", steve);
        CuentaEntity c105 = guardarCuenta("105", TipoCuenta.PRESTAMO, "10000", diana);
        CuentaEntity c106 = guardarCuenta("106", TipoCuenta.PRESTAMO, "12000", jane);
        CuentaEntity c107 = guardarCuenta("107", TipoCuenta.AHORRO, "5000", alice);
        CuentaEntity c108 = guardarCuenta("108", TipoCuenta.PRESTAMO, "12000", jane);
        CuentaEntity c109 = guardarCuenta("109", TipoCuenta.HIPOTECA, "7000", charlie);
        CuentaEntity c110 = guardarCuenta("110", TipoCuenta.AHORRO, "8000", jane);
        CuentaEntity c111 = guardarCuenta("111", TipoCuenta.HIPOTECA, "10000", diana);
        CuentaEntity c112 = guardarCuenta("112", TipoCuenta.AHORRO, "10000", steve);
        CuentaEntity c113 = guardarCuenta("113", TipoCuenta.HIPOTECA, "10000", charlie);
        CuentaEntity c114 = guardarCuenta("114", TipoCuenta.AHORRO, "5000", bob);
        CuentaEntity c116 = guardarCuenta("116", TipoCuenta.PRESTAMO, "12000", diana);
        CuentaEntity c117 = guardarCuenta("117", TipoCuenta.AHORRO, "12000", steve);
        CuentaEntity c118 = guardarCuenta("118", TipoCuenta.PRESTAMO, "8000", john);
        CuentaEntity c119 = guardarCuenta("119", TipoCuenta.HIPOTECA, "10000", john);
        CuentaEntity c120 = guardarCuenta("120", TipoCuenta.AHORRO, "7000", john);

        // Movimientos de 2024 inspirados en cuentas_anuales.csv / transacciones.csv (montos y fechas validos)
        registrar(c101, "2024-01-07", TipoTransaccion.DEPOSITO, "2500", "Ingreso mensual");
        registrar(c101, "2024-02-19", TipoTransaccion.COMPRA, "2000", "Compra en tienda");
        registrar(c101, "2024-03-22", TipoTransaccion.RETIRO, "1000", "Retiro parcial");
        registrar(c101, "2024-06-12", TipoTransaccion.DEPOSITO, "2000", "Ingreso extra");
        registrar(c101, "2024-09-25", TipoTransaccion.DEPOSITO, "2500", "Ingreso mensual");

        registrar(c102, "2024-02-08", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");
        registrar(c102, "2024-04-19", TipoTransaccion.DEPOSITO, "2000", "Ingreso extra");
        registrar(c102, "2024-09-24", TipoTransaccion.RETIRO, "1500", "Retiro parcial");
        registrar(c102, "2024-11-05", TipoTransaccion.PAGO, "2500", "Pago de cuota");

        registrar(c103, "2024-03-08", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");
        registrar(c103, "2024-05-09", TipoTransaccion.RETIRO, "1500", "Retiro parcial");
        registrar(c103, "2024-07-12", TipoTransaccion.COMPRA, "2000", "Compra en tienda");
        registrar(c103, "2024-10-29", TipoTransaccion.COMPRA, "2500", "Ingreso navideno aplicado a compra");

        registrar(c104, "2024-01-11", TipoTransaccion.RETIRO, "1000", "Compra en tienda");
        registrar(c104, "2024-03-01", TipoTransaccion.DEPOSITO, "2500", "Ingreso extra");
        registrar(c104, "2024-07-19", TipoTransaccion.RETIRO, "1000", "Compra en tienda");
        registrar(c104, "2024-11-07", TipoTransaccion.COMPRA, "1000", "Ingreso navideno");

        registrar(c105, "2024-01-17", TipoTransaccion.DEPOSITO, "1000", "Ingreso mensual");
        registrar(c105, "2024-04-01", TipoTransaccion.DEPOSITO, "1000", "Ingreso extra");
        registrar(c105, "2024-06-21", TipoTransaccion.COMPRA, "3000", "Compra en tienda");
        registrar(c105, "2024-12-01", TipoTransaccion.DEPOSITO, "2000", "Ingreso mensual");

        registrar(c106, "2024-02-12", TipoTransaccion.DEPOSITO, "2000", "Ingreso extra");
        registrar(c106, "2024-04-06", TipoTransaccion.RETIRO, "2500", "Compra en tienda");
        registrar(c106, "2024-07-11", TipoTransaccion.COMPRA, "1500", "Ingreso navideno");
        registrar(c106, "2024-08-27", TipoTransaccion.DEPOSITO, "2500", "Ingreso mensual");

        registrar(c107, "2024-01-28", TipoTransaccion.RETIRO, "1500", "Retiro parcial");
        registrar(c107, "2024-06-22", TipoTransaccion.RETIRO, "1500", "Ingreso navideno");
        registrar(c107, "2024-11-16", TipoTransaccion.DEPOSITO, "1500", "Ingreso extra");
        registrar(c107, "2024-11-30", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");

        registrar(c108, "2024-02-15", TipoTransaccion.COMPRA, "3000", "Compra en tienda");
        registrar(c108, "2024-05-14", TipoTransaccion.DEPOSITO, "3000", "Ingreso extra");
        registrar(c108, "2024-07-19", TipoTransaccion.RETIRO, "2000", "Retiro parcial");
        registrar(c108, "2024-12-22", TipoTransaccion.DEPOSITO, "1000", "Ingreso extra");

        registrar(c109, "2024-03-18", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");
        registrar(c109, "2024-05-02", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");
        registrar(c109, "2024-11-09", TipoTransaccion.RETIRO, "2000", "Compra en tienda");
        registrar(c109, "2024-12-04", TipoTransaccion.RETIRO, "1000", "Compra en tienda");

        registrar(c110, "2024-02-14", TipoTransaccion.DEPOSITO, "2500", "Ingreso navideno");
        registrar(c110, "2024-04-26", TipoTransaccion.COMPRA, "3000", "Ingreso mensual");
        registrar(c110, "2024-07-24", TipoTransaccion.RETIRO, "1500", "Retiro parcial");
        registrar(c110, "2024-11-13", TipoTransaccion.COMPRA, "2500", "Retiro parcial");

        registrar(c111, "2024-03-10", TipoTransaccion.DEPOSITO, "2000", "Apertura complementaria");
        registrar(c111, "2024-04-26", TipoTransaccion.RETIRO, "2000", "Ingreso navideno");
        registrar(c111, "2024-07-19", TipoTransaccion.COMPRA, "1500", "Compra en tienda");
        registrar(c111, "2024-09-16", TipoTransaccion.COMPRA, "2500", "Ingreso mensual");

        registrar(c112, "2024-04-01", TipoTransaccion.RETIRO, "3000", "Retiro parcial");
        registrar(c112, "2024-06-12", TipoTransaccion.DEPOSITO, "3000", "Ingreso navideno");
        registrar(c112, "2024-08-25", TipoTransaccion.COMPRA, "2500", "Ingreso navideno");
        registrar(c112, "2024-12-16", TipoTransaccion.COMPRA, "1000", "Ingreso mensual");

        registrar(c113, "2024-01-11", TipoTransaccion.DEPOSITO, "3000", "Retiro parcial regularizado");
        registrar(c113, "2024-01-24", TipoTransaccion.COMPRA, "2500", "Compra en tienda");
        registrar(c113, "2024-05-02", TipoTransaccion.RETIRO, "1000", "Retiro parcial");
        registrar(c113, "2024-12-04", TipoTransaccion.DEPOSITO, "2000", "Ingreso extra");

        registrar(c114, "2024-02-13", TipoTransaccion.RETIRO, "1500", "Ingreso mensual");
        registrar(c114, "2024-04-23", TipoTransaccion.RETIRO, "2000", "Compra en tienda");
        registrar(c114, "2024-07-08", TipoTransaccion.COMPRA, "500", "Compra menor");
        registrar(c114, "2024-09-30", TipoTransaccion.DEPOSITO, "1000", "Ingreso mensual");

        registrar(c116, "2024-02-15", TipoTransaccion.DEPOSITO, "3000", "Ingreso extra");
        registrar(c116, "2024-06-25", TipoTransaccion.RETIRO, "2000", "Retiro parcial");
        registrar(c116, "2024-11-22", TipoTransaccion.COMPRA, "2500", "Compra en tienda");
        registrar(c116, "2024-12-24", TipoTransaccion.COMPRA, "2500", "Compra en tienda");

        registrar(c117, "2024-01-11", TipoTransaccion.DEPOSITO, "1500", "Ingreso extra");
        registrar(c117, "2024-05-18", TipoTransaccion.DEPOSITO, "1000", "Ingreso mensual");
        registrar(c117, "2024-08-26", TipoTransaccion.DEPOSITO, "3000", "Ingreso mensual");
        registrar(c117, "2024-11-17", TipoTransaccion.DEPOSITO, "1500", "Retiro parcial regularizado");

        registrar(c118, "2024-01-30", TipoTransaccion.RETIRO, "2500", "Ingreso mensual");
        registrar(c118, "2024-04-27", TipoTransaccion.RETIRO, "2000", "Ingreso mensual");
        registrar(c118, "2024-08-15", TipoTransaccion.RETIRO, "1500", "Ingreso extra");
        registrar(c118, "2024-12-09", TipoTransaccion.RETIRO, "1000", "Retiro de fin de anio");

        registrar(c119, "2024-03-24", TipoTransaccion.COMPRA, "1500", "Compra en tienda");
        registrar(c119, "2024-05-21", TipoTransaccion.RETIRO, "1500", "Retiro parcial");
        registrar(c119, "2024-07-25", TipoTransaccion.RETIRO, "1500", "Ingreso extra");
        registrar(c119, "2024-10-28", TipoTransaccion.RETIRO, "2000", "Ingreso extra");

        registrar(c120, "2024-01-13", TipoTransaccion.DEPOSITO, "1000", "Apertura");
        registrar(c120, "2024-03-09", TipoTransaccion.RETIRO, "2000", "Compra en tienda");
        registrar(c120, "2024-08-18", TipoTransaccion.DEPOSITO, "1500", "Compra en tienda");
        registrar(c120, "2024-10-27", TipoTransaccion.RETIRO, "1500", "Ingreso extra");

        log.info("Carga inicial lista: {} clientes, {} cuentas, {} transacciones",
                clienteRepository.count(), cuentaRepository.count(), transaccionRepository.count());
    }

    private ClienteEntity guardarCliente(String nombre, int edad, String email, String rut) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNombre(nombre);
        cliente.setEdad(edad);
        cliente.setEmail(email);
        cliente.setRut(rut);
        return clienteRepository.save(cliente);
    }

    private CuentaEntity guardarCuenta(String numero, TipoCuenta tipo, String saldo, ClienteEntity cliente) {
        CuentaEntity cuenta = new CuentaEntity();
        cuenta.setNumeroCuenta(numero);
        cuenta.setTipoCuenta(tipo);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.setActiva(true);
        cuenta.setCliente(cliente);
        return cuentaRepository.save(cuenta);
    }

    private void registrar(CuentaEntity cuenta, String fecha, TipoTransaccion tipo, String monto, String descripcion) {
        BigDecimal valor = new BigDecimal(monto);
        TransaccionEntity tx = new TransaccionEntity();
        tx.setFecha(LocalDate.parse(fecha));
        tx.setTipo(tipo);
        tx.setMonto(valor);
        tx.setDescripcion(descripcion);
        tx.setCuenta(cuenta);

        if (tipo == TipoTransaccion.DEPOSITO) {
            cuenta.setSaldo(cuenta.getSaldo().add(valor));
        } else {
            cuenta.setSaldo(cuenta.getSaldo().subtract(valor));
        }

        cuentaRepository.save(cuenta);
        transaccionRepository.save(tx);
    }

}
