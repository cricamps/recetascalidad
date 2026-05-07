package com.duoc.recetas.service;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.RecetaRepository;
import com.duoc.recetas.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final RecetaRepository  recetaRepository;
    private final PasswordEncoder   passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           RecetaRepository recetaRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.recetaRepository  = recetaRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        inicializarUsuarios();
        inicializarRecetas();
    }

    private void inicializarUsuarios() {
        crearSiNoExiste("usuario", "usuario123", "ROLE_USER");
        crearSiNoExiste("chef",    "chef123",    "ROLE_USER,ROLE_CHEF");
        crearSiNoExiste("admin",   "admin123",   "ROLE_USER,ROLE_ADMIN");
        log.info("Usuarios inicializados en la base de datos.");
    }

    private void crearSiNoExiste(String username, String rawPassword, String roles) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            usuarioRepository.save(
                new Usuario(username, passwordEncoder.encode(rawPassword), roles));
        }
    }

    private void inicializarRecetas() {
        if (recetaRepository.count() > 0) return;

        agregarReceta("Arroz Graneado", "Tradicional", "Chile", "Fácil", 25, true,
            "Arroz suelto y sabroso, frito antes de cocinarse con hierbas opcionales al gusto.",
            "Una taza de arroz por dos de agua hirviendo. Se puede agregar zanahoria, pimentón, ajo, perejil u orégano.",
            "1 taza de arroz|2 tazas de agua hirviendo|Aceite|Sal|Opcional: zanahoria, pimentón, ajo, perejil",
            "Freír el arroz en aceite 2 minutos.|Agregar ingredientes opcionales.|Agregar sal.|Verter agua hirviendo.|Tapar y cocinar a fuego bajo 20 minutos.");

        agregarReceta("Pollo Asado al Horno", "Tradicional", "Chile", "Fácil", 40, true,
            "Presas de pollo aliñadas y horneadas, con múltiples combinaciones de aliños posibles.",
            "Pollo aliñado puesto en fuente para horno por 40 minutos.",
            "Presas de pollo|Sal y limón|Merkén|Mostaza|Cerveza o vino",
            "Aliñar las presas de pollo.|Agregar un chorro de cerveza o vino.|Colocar en fuente para horno.|Hornear 30-40 minutos.|Retirar papel aluminio al final para dorar.");

        agregarReceta("Pollo Guisado", "Tradicional", "Chile", "Fácil", 45, false,
            "Pollo cocinado sobre un sofrito de cebolla, pimentón y ajo con cerveza o vino.",
            "Freír cebolla con pimentón y ajo, y sobre estas verduritas cocinar las presas de pollo.",
            "Presas de pollo|1 cebolla chica|Pimentón|Ajo|Cerveza o vino|Sal y pimienta",
            "Freír cebolla con pimentón y ajo.|Agregar las presas de pollo.|Agregar cerveza o vino.|Tapar y cocinar a fuego bajo hasta que esté tierno.");

        agregarReceta("Tortillas Caseras", "Tradicional", "Chile", "Fácil", 15, true,
            "Tortillas rápidas con huevo y el ingrediente que tengas: atún, arroz, fideos o sobras.",
            "Mezclar el ingrediente base con huevos y sal, calentar la sartén y cocinar con poco aceite.",
            "2-3 huevos|Sal|Aceite|Atún o arroz o fideos o lo que sobre del día anterior",
            "Mezclar ingrediente con huevos y sal.|Calentar sartén con poco aceite.|Verter mezcla y cocinar a fuego medio.|Dar vuelta con un plato grande.|Cocinar por el otro lado.");

        agregarReceta("Plateada a la Olla", "Tradicional", "Chile", "Media", 180, true,
            "Carne sellada y cocinada lentamente en vino o cerveza por dos a tres horas.",
            "Sellar la plateada en aceite de oliva, agregar vino o cerveza y cocinar 2-3 horas.",
            "1 plateada|Aceite de oliva|1 taza de vino tinto o cerveza|Sal",
            "Calentar olla y agregar aceite de oliva.|Sellar la plateada por ambos lados.|Agregar vino o cerveza.|Cocinar 2-3 horas a fuego bajo.|Agregar sal después de 1.5 horas.");

        agregarReceta("Pino de Carne", "Tradicional", "Chile", "Fácil", 35, true,
            "Base versátil para empanadas, pastel de papas, tacos, arroz y mucho más.",
            "Freír cebolla y aliños, agregar carne molida y cocinar a fuego lento por 30 minutos.",
            "1 kilo de carne molida|1 cebolla|Ajo|Merkén|Orégano|Sal",
            "Freír cebolla con aliños.|Agregar la carne molida.|Tapar y cocinar a fuego lento 30 minutos.|Revolver para que no se pegue.");

        agregarReceta("Pastel de Papas", "Tradicional", "Chile", "Media", 60, false,
            "Pino de carne cubierto con puré de papas y gratinado con azúcar para que quede doradito.",
            "Pino de carne cubierto con puré y azúcar encima. Se hornea hasta que quede dorado.",
            "1 receta de pino de carne|5-6 papas grandes|Leche y mantequilla|2-3 huevos duros|Azúcar",
            "Preparar pino de carne.|Hacer puré con papas, leche y mantequilla.|Poner pino en fuente de horno.|Agregar huevos duros picados.|Cubrir con puré.|Espolvorear azúcar.|Hornear a 180°C hasta dorar.");

        agregarReceta("Verduras Salteadas", "Vegetariana", "Chile", "Fácil", 25, false,
            "Wok de verduras coloridas salteadas con salsa de soya.",
            "Saltear verduras en wok con aceite de oliva y salsa de soya.",
            "1 zapallito italiano|1 pimentón|Cebollines|Champiñones|Zanahoria|Brócoli|Aceite de oliva|Salsa de soya",
            "Picar todas las verduras.|Poner aceite en wok a fuego medio.|Agregar verduras más duras primero.|Ir agregando el resto.|Aliñar con salsa de soya.");

        agregarReceta("Charquicán Sin Charqui", "Tradicional", "Chile", "Media", 65, true,
            "Versión santiaguina del charquicán con carne molida, papas, zapallo y verduras.",
            "Freír carne con verduras ralladas, agregar papas y zapallo picados y cocinar a fuego bajo.",
            "300g carne molida|1 cebolla chica|Pimentón|Zanahoria|1 trozo de zapallo|5 papas|Choclo|Sal y ajo",
            "Rallar cebolla, pimentón y zanahoria.|Freír con la carne molida.|Agregar papas y zapallo picados.|Cocinar a fuego bajo sin agua.|Agregar choclo a los 20 minutos.|Cocinar 1 hora total.");

        agregarReceta("Sopa de Choritos", "Marina", "Chile", "Media", 40, true,
            "Choros frescos cocinados en vino blanco con sofrito, espesada con harina y crema.",
            "Freír cebollines con pimentón, cocinar choritos en vino blanco, desconchar y espesar.",
            "1 kilo de choritos|2 cebollines|Pimentón|2 tazas vino blanco|1 taza agua|1 cda harina|3 cdas crema",
            "Freír cebollines con pimentón.|Agregar choritos, vino y agua.|Tapar hasta que abran.|Desconchar los choritos.|Calentar sopa.|Disolver harina en vino y agregar.|Hervir 10 minutos.|Agregar crema.");

        agregarReceta("Empanadas de Pino al Horno", "Tradicional", "Chile", "Difícil", 90, true,
            "Clásicas empanadas chilenas con masa de mantequilla y cerveza, rellenas con pino jugoso.",
            "Masa con mantequilla y cerveza, rellena con pino de carne.",
            "Para el pino: 1 kilo de carne|4 cebollas|Ajo, ají, orégano|Para la masa: 1 kilo harina|Medio kilo mantequilla|Cerveza",
            "Preparar el pino y dejar enfriar.|Mezclar harina con mantequilla y cerveza.|Amasar hasta que esté suave.|Reposar 30 minutos.|Estirar, rellenar y sellar.|Pintar con huevo.|Hornear a 180°C por 25-30 minutos.");

        agregarReceta("Pescado Rico al Horno", "Marina", "Chile", "Fácil", 20, false,
            "Filetes de pescado con aceite de oliva y alcáparras, listo en 15 minutos.",
            "Cualquier pescado fileteado con aceite de oliva y alcáparras.",
            "Filetes de pescado|Aceite de oliva|Alcáparras|Sal y pimienta",
            "Poner aceite de oliva en fuente.|Colocar filetes.|Chorrear más aceite.|Agregar alcáparras.|Cocinar 10-15 minutos con tapa.");

        agregarReceta("Cazuela Casera", "Tradicional", "Chile", "Media", 60, true,
            "Cazuela de pollo o carne con zapallo, papas, zanahoria, choclo y un puñado de arroz.",
            "Cocinar las presas con verduras en orden: primero las duras, al final el choclo y el arroz.",
            "Presas de pollo o carne|Cebolla|Zanahoria|Zapallo|Papas|Porotos verdes|Choclo|Arroz|Sal",
            "Cocer las presas con cebolla y zanahoria.|Cuando esté blanda agregar zapallo y papas.|Agregar porotos y arroz.|Al final agregar choclo.|Salar y servir caliente.");

        agregarReceta("Leche Asada", "Postre", "Chile", "Media", 70, true,
            "Postre clásico chileno de leche, huevos y caramelo, horneado a baño maría.",
            "Mezclar leche, huevos y azúcar. Hacer caramelo y verter en molde. Hornear a baño maría.",
            "1 litro de leche|6 huevos|3/4 taza de azúcar|1/2 taza azúcar para caramelo|Vainilla",
            "Batir huevos con azúcar, vainilla y leche.|Preparar caramelo rubio.|Volcar caramelo en molde.|Agregar el batido.|Hornear a baño maría 1 hora.");

        agregarReceta("Arroz con Leche", "Postre", "Chile", "Fácil", 30, true,
            "Cremoso arroz con leche con yema y crema opcional, cocinado a fuego lento.",
            "El azúcar se agrega solo cuando el arroz está cocido.",
            "1 taza de arroz|1 litro de leche|1/2 taza azúcar|1 yema de huevo|Cucharadas de crema",
            "Lavar el arroz.|Calentar leche y agregar arroz.|Cocinar a fuego lento 20 minutos.|Agregar azúcar.|Mezclar yema con leche fría y agregar.|Agregar crema y apagar.");

        log.info("Recetas inicializadas en la base de datos.");
    }

    private void agregarReceta(String nombre, String tipoCocina, String paisOrigen,
                                String dificultad, int tiempo, boolean popular,
                                String descCorta, String descLarga,
                                String ingredientes, String instrucciones) {
        RecetaEntity r = new RecetaEntity();
        r.setNombre(nombre);
        r.setTipoCocina(tipoCocina);
        r.setPaisOrigen(paisOrigen);
        r.setDificultad(dificultad);
        r.setTiempoCoccion(tiempo);
        r.setPopular(popular);
        r.setDescripcionCorta(descCorta);
        r.setDescripcionLarga(descLarga);
        r.setIngredientes(ingredientes);
        r.setInstrucciones(instrucciones);
        r.setImagen(nombre.toLowerCase().replace(" ", "_") + ".jpg");
        recetaRepository.save(r);
    }
}
