db = db.getSiblingDB("btg_test");
db.createUser(
        {
            user: "btg_test",
            pwd: "password",
            roles: [
                {
                    role: "readWrite",
                    db: "btg_test"
                }
            ]
        }
);

// Crear colecciones
db.createCollection("cliente");
db.createCollection("fondo");

// Insertar datos iniciales de Clientes (Alineado con Cliente.java)
db.cliente.insertMany([
    {
        _id: ObjectId().toString(),
        nombre: "Juan Perez",
        saldoDisponible: 500000.0,
        preferenciaNotificacion: "EMAIL",
        fondosSuscritos: []
    },
    {
        _id: ObjectId().toString(),
        nombre: "Maria Rodriguez",
        saldoDisponible: 1500000.0,
        preferenciaNotificacion: "SMS",
        fondosSuscritos: []
    },
    {
        _id: ObjectId().toString(),
        nombre: "Carlos Gomez",
        saldoDisponible: 100000.0,
        preferenciaNotificacion: "EMAIL",
        fondosSuscritos: []
    }
]);

// Insertar datos iniciales de Fondos (Alineado con Fondo.java)
db.fondo.insertMany([
    {
        _id: "1",
        nombre: "FPV_BTG_PACTUAL_RECAUDADORA",
        montoMinimo: 75000.0,
        categoria: "FPV"
    },
    {
        _id: "2",
        nombre: "FPV_BTG_PACTUAL_ECOPETROL",
        montoMinimo: 125000.0,
        categoria: "FPV"
    },
    {
        _id: "3",
        nombre: "DEUDAPRIVADA",
        montoMinimo: 50000.0,
        categoria: "FIC"
    },
    {
        _id: "4",
        nombre: "FDO-ACCIONES",
        montoMinimo: 250000.0,
        categoria: "FIC"
    },
    {
        _id: "5",
        nombre: "FPV_BTG_PACTUAL_DINAMICA",
        montoMinimo: 100000.0,
        categoria: "FPV"
    }
]);
