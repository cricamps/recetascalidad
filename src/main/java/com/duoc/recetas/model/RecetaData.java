package com.duoc.recetas.model;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Component
public class RecetaData {

    private final List<Receta> recetas;

    public RecetaData() {
        recetas = new ArrayList<>();

        recetas.add(new Receta(
            1,
            "Arroz Graneado",
            "Tradicional",
            "Chile",
            "Fácil",
            25,
            "Arroz suelto y sabroso, frito antes de cocinarse con hierbas opcionales al gusto.",
            "Una taza de arroz por dos de agua hirviendo (la segunda taza no tan llena). " +
            "Se puede agregar zanahoria, pimentón, ajo, perejil, orégano, cebollón, ciboulette o chalota, " +
            "todo según lo que dé el refrigerador o el verdulero. La sal no es opcional porque el arroz sin sal es muy malo.",
            Arrays.asList(
                "1 taza de arroz",
                "2 tazas de agua hirviendo (la segunda no tan llena)",
                "Aceite para freír",
                "Sal (obligatoria)",
                "Opcional: zanahoria, pimentón, ajo, perejil, orégano, cebollón, ciboulette, chalota"
            ),
            Arrays.asList(
                "Poner aceite en la olla y freír el arroz por un par de minutos.",
                "Agregar los ingredientes opcionales que tengas a mano: zanahoria, pimentón, ajo, perejil, etc.",
                "Agregar sal al gusto.",
                "Verter el agua hirviendo sobre el arroz frito.",
                "Tapar la olla y poner a fuego corona (el más bajo) o sobre tostador de pan.",
                "Cocinar por 20 minutos sin destapar."
            ),
            "arroz.jpg",
            true
        ));

        recetas.add(new Receta(
            2,
            "Pollo Asado al Horno",
            "Tradicional",
            "Chile",
            "Fácil",
            40,
            "Presas de pollo aliñadas y horneadas, con múltiples combinaciones de aliños posibles.",
            "Pollo aliñado y puesto en fuente para el horno por media hora aproximadamente. " +
            "Se puede tapar con papel de aluminio para que no se seque y luego destapar para dorar. " +
            "Los aliños varían según lo que haya en el bar: cerveza, vino, merkén, mostaza o solo sal y limón.",
            Arrays.asList(
                "Presas de pollo al gusto",
                "Aliño opción 1: solo sal y limón",
                "Aliño opción 2: merkén y sal",
                "Aliño opción 3: merkén, sal y mostaza",
                "Aliño opción 4: mostaza, orégano y salsa de soya",
                "Cerveza o vino (según lo que haya en el bar)"
            ),
            Arrays.asList(
                "Aliñar las presas de pollo con la combinación de aliños elegida.",
                "Agregar un chorro de cerveza o vino sobre las presas.",
                "Colocar en fuente para horno.",
                "Hornear por aproximadamente 30-40 minutos. Revisar cuando esté cocido (depende del horno).",
                "Si se seca mucho, tapar con papel de aluminio por un rato.",
                "Retirar el papel aluminio al final para que se dore."
            ),
            "pollo_asado.jpg",
            true
        ));

        recetas.add(new Receta(
            3,
            "Pollo Guisado",
            "Tradicional",
            "Chile",
            "Fácil",
            45,
            "Pollo cocinado sobre un sofrito de cebolla, pimentón y ajo con cerveza o vino.",
            "Freír cebolla chica o cebollines con pimentón y ajo, y sobre estas verduritas ya fritas " +
            "cocinar las presas de pollo con cerveza o vino. A falta de estos, también sirve agua. " +
            "Tapar y cocinar a fuego bajo.",
            Arrays.asList(
                "Presas de pollo",
                "1 cebolla chica o cebollines",
                "Pimentón",
                "Ajo",
                "Cerveza o vino (a falta de estos, agua de la llave también sirve)",
                "Sal, pimienta, merkén u otros aliños al gusto"
            ),
            Arrays.asList(
                "Freír la cebolla (o cebollines) con pimentón y ajo en aceite.",
                "Sobre las verduritas ya fritas, agregar las presas de pollo.",
                "Agregar cerveza o vino. Si no hay, agua de la llave también sirve, no hace mal.",
                "Tapar la olla y cocinar a fuego bajo hasta que el pollo esté tierno.",
                "También queda rico friendo el pollo solo con un pedacito de ajo a fuego bajo."
            ),
            "pollo_guisado.jpg",
            false
        ));

        recetas.add(new Receta(
            4,
            "Tortillas Caseras",
            "Tradicional",
            "Chile",
            "Fácil",
            15,
            "Tortillas rápidas con huevo y el ingrediente que tengas: atún, arroz, fideos o sobras del día anterior.",
            "Mezclar el ingrediente base con huevos y un poquito de sal, calentar la sartén y cocinar con poquito aceite. " +
            "Una vez firme, dar vuelta con la ayuda de un plato grande. Son una buena solución, rápidas y versátiles. " +
            "Siempre es bueno tener huevos y tarritos de atún en casa.",
            Arrays.asList(
                "Huevos (2-3)",
                "Sal",
                "Aceite para la sartén",
                "Ingrediente base (a elección): atún, atún con rúcula, perejil, zanahoria, arroz, fideos, o lo que sobre del día anterior"
            ),
            Arrays.asList(
                "Mezclar el ingrediente elegido con los huevos y un poquito de sal.",
                "Calentar la sartén con un poquito de aceite.",
                "Verter la mezcla y cocinar a fuego medio hasta que esté firme por abajo.",
                "Dar vuelta con la ayuda de un plato grande.",
                "Cocinar por el otro lado hasta dorar.",
                "Servir caliente. Sirven para cualquier momento del día."
            ),
            "tortillas.jpg",
            true
        ));

        recetas.add(new Receta(
            5,
            "Plateada a la Olla",
            "Tradicional",
            "Chile",
            "Media",
            180,
            "Carne sellada y cocinada lentamente en vino o cerveza por dos a tres horas hasta quedar tierna.",
            "Calentar la olla, agregar aceite de oliva, poner la plateada y freír bien por ambos lados para sellar. " +
            "Agregar vino o cerveza, bajar el fuego y cocinar por dos o tres horas. La sal se agrega después de hora y media. " +
            "También queda rico con posta paleta y punta de picana.",
            Arrays.asList(
                "1 plateada (o posta paleta, o punta de picana)",
                "Aceite de oliva",
                "1 taza de vino tinto o cerveza",
                "Sal (se agrega después de 1.5 horas)"
            ),
            Arrays.asList(
                "Calentar bien la olla y agregar aceite de oliva.",
                "Poner la plateada y freír bien por ambos lados para sellarla.",
                "Agregar una taza de vino o cerveza.",
                "Bajar el fuego al mínimo y tapar.",
                "Cocinar por 2 a 3 horas, dando vuelta cada media hora.",
                "Agregar la sal después de la primera hora y media de cocción.",
                "Está lista cuando la carne esté muy tierna y se deshaga fácilmente."
            ),
            "plateada.jpg",
            true
        ));

        recetas.add(new Receta(
            6,
            "Pino de Carne",
            "Tradicional",
            "Chile",
            "Fácil",
            35,
            "Base versátil para empanadas, pastel de papas, tacos, arroz y mucho más.",
            "Muy útil: sirve para comer con arroz, puré, papas cocidas, para preparar salsa de tomate, " +
            "para tacos, pastel de papas, pastel de choclo, etc. Para lo que quieras.",
            Arrays.asList(
                "1 kilo de carne molida o picada fina",
                "1 cebolla picada muy fina",
                "Ajo al gusto",
                "Aliños opcionales: merkén, orégano, pimentón, zanahoria, perejil",
                "Sal al gusto"
            ),
            Arrays.asList(
                "Freír la cebolla con los aliños elegidos en aceite.",
                "Agregar la carne molida o picada.",
                "Tapar y cocinar a fuego lento por media hora aproximadamente.",
                "Revolver de vez en cuando para que no se pegue.",
                "Probar la sal y ajustar.",
                "Usar como base para empanadas, pastel de papas, pastel de choclo, tacos o acompañar con arroz."
            ),
            "pino.jpg",
            true
        ));

        recetas.add(new Receta(
            7,
            "Pastel de Papas",
            "Tradicional",
            "Chile",
            "Media",
            60,
            "Pino de carne cubierto con puré de papas y gratinado con azúcar para que quede doradito.",
            "Para preparar el pastel de papas se hace un pino de carne, se prepara un puré de papas rico. " +
            "En una fuente para horno se coloca una buena cantidad de pino con huevos duros picados y " +
            "sobre esto se pone una capa de puré. Sobre el puré se le pone un poco de azúcar para que quede doradito.",
            Arrays.asList(
                "1 receta de pino de carne",
                "5-6 papas grandes",
                "Leche y mantequilla para el puré",
                "2-3 huevos duros",
                "Sal al gusto",
                "Azúcar para espolvorear encima"
            ),
            Arrays.asList(
                "Preparar un pino de carne (ver receta de Pino de Carne).",
                "Cocer las papas y hacer un puré con leche, mantequilla y sal.",
                "En una fuente para horno colocar una buena capa de pino.",
                "Agregar los huevos duros picados encima del pino.",
                "Cubrir con una capa de puré, que no sea muy gruesa.",
                "Espolvorear azúcar sobre el puré para que quede doradito.",
                "Hornear a 180°C hasta que la superficie esté dorada."
            ),
            "pastel_papas.jpg",
            false
        ));

        recetas.add(new Receta(
            8,
            "Verduras Salteadas",
            "Vegetariana",
            "Chile",
            "Fácil",
            25,
            "Wok de verduras coloridas salteadas con salsa de soya. Liviana y perfecta para la noche.",
            "En la noche viene muy bien una comida livianita como esta. Se adecua a lo que dé el refrigerador y el verdulero. " +
            "No es necesario tener todas las verduras que aquí se nombran.",
            Arrays.asList(
                "1 zapallito italiano",
                "1 berenjena",
                "1 pimentón verde y 1 rojo",
                "2 cebollines",
                "Champiñones",
                "1 zanahoria",
                "Brócoli",
                "Choclo congelado (opcional)",
                "Brotes de alfalfa o dientes de dragón (opcional)",
                "Perejil",
                "Aceite de oliva",
                "Salsa de soya"
            ),
            Arrays.asList(
                "Picar todas las verduras.",
                "En un wok poner un poco de aceite de oliva a fuego medio.",
                "Comenzar con el cebollín.",
                "Ir agregando las verduras más duras primero: zanahoria, brócoli.",
                "Luego agregar pimentones, zapallitos, berenjena, choclo.",
                "Por último champiñones, perejil y brotes.",
                "Ir revolviendo constantemente.",
                "Aliñar con salsa de soya al gusto.",
                "Servir solo o acompañado de arroz blanco."
            ),
            "verduras.jpg",
            false
        ));

        recetas.add(new Receta(
            9,
            "Charquicán Sin Charqui",
            "Tradicional",
            "Chile",
            "Media",
            65,
            "Versión santiaguina del charquicán, con carne molida, papas, zapallo y verduras.",
            "Como lo comemos en Santiago. Esta receta es particular: a toda la gente se la escucha preparar de otra manera. " +
            "Se prepara a fuego bajo y con el tostador, no se agrega agua. " +
            "Se le puede agregar choclo congelado pasados 20 minutos.",
            Arrays.asList(
                "300 g de carne molida",
                "1 cebolla chica",
                "Medio pimentón",
                "Perejil",
                "1 zanahoria",
                "1 trozo de zapallo",
                "5 papas (aproximadamente)",
                "Choclo congelado (opcional, se agrega a los 20 min)",
                "Sal, ajíes y ajo al gusto"
            ),
            Arrays.asList(
                "Rallar la cebolla, el pimentón y la zanahoria.",
                "Picar el perejil.",
                "Freír esto junto con la carne molida.",
                "Picar las papas y el zapallo en trozos muy pequeños.",
                "Agregar al sofrito y revolver seguido para que no se pegue.",
                "Cocinar a fuego bajo con tostador, sin agregar agua.",
                "A los 20 minutos agregar choclo congelado si se desea.",
                "Cocinar por aproximadamente 1 hora en total.",
                "Está listo cuando las papas están bien cocidas.",
                "Agregar sal, ajíes y ajo al gusto."
            ),
            "charquican.jpg",
            true
        ));

        recetas.add(new Receta(
            10,
            "Sopa de Choritos",
            "Marina",
            "Chile",
            "Media",
            40,
            "Choros frescos cocinados en vino blanco con sofrito de cebollines, espesada con harina y crema.",
            "Una sopa de mariscos contundente y sabrosa, con choros frescos cocinados en vino blanco. " +
            "Se desconcha una vez cocidos y se espesa con harina disuelta en vino, rematando con crema.",
            Arrays.asList(
                "1 kilo de choritos (choros) lavados",
                "2 cebollines",
                "Pimentón",
                "2 tazas de vino blanco (más 1 taza adicional para espesar)",
                "1 taza de agua",
                "1 cucharada de harina",
                "3 cucharadas de crema",
                "Sal y pimienta"
            ),
            Arrays.asList(
                "Freír los cebollines picados muy finos con el pimentón rallado.",
                "Agregar los choritos lavados, 2 tazas de vino blanco y 1 taza de agua.",
                "Tapar y cocinar hasta que los choritos estén abiertos (cocidos).",
                "Dejar enfriar y desconchar los choritos (con guantes de goma porque están calientes).",
                "Volver a calentar la sopa con los choritos desconchados.",
                "En un bol disolver 1 cucharada de harina en 1 taza de vino blanco.",
                "Agregar esta mezcla a la sopa y dejar hervir 10 minutos.",
                "Agregar 3 cucharadas de crema antes de servir."
            ),
            "choritos.jpg",
            true
        ));

        recetas.add(new Receta(
            11,
            "Empanadas de Pino al Horno",
            "Tradicional",
            "Chile",
            "Difícil",
            90,
            "Clásicas empanadas chilenas con masa de mantequilla y cerveza, rellenas con pino jugoso.",
            "El pino debe quedar cremoso pero no muy jugoso para que no moje la masa. " +
            "La masa lleva mantequilla y agua con cerveza, lo que le da una textura suave y fácil de amasar. " +
            "Dejar reposar la masa media hora antes de estirar.",
            Arrays.asList(
                "Para el pino: 1 kilo de carne picada chiquita o molida",
                "4 cebollas medianas",
                "Ajo, ají, orégano, sal",
                "Para la masa: 1 kilo de harina sin polvos",
                "Medio kilo de mantequilla",
                "2 cucharaditas de polvos de hornear",
                "1 cucharadita de sal",
                "Cerveza mezclada con agua tibia (la cantidad necesaria)"
            ),
            Arrays.asList(
                "Preparar el pino: freír ajo picado con la carne y aliños hasta que esté blanda.",
                "Agregar la cebolla picada chica y cocinar bien por media hora.",
                "Si queda muy jugoso, agregar 1 cucharada de harina revolviendo para que no se apelotone.",
                "Preparar la masa: mezclar harina, sal y polvos de hornear.",
                "Hacer un hoyo, agregar la mantequilla y el agua con cerveza de a poco.",
                "Amasar hasta formar una masa blandita y suave.",
                "Dejar reposar 30 minutos.",
                "Estirar, cortar círculos, rellenar con pino, doblar y sellar.",
                "Pintar con huevo batido y hornear a 180°C por 25-30 minutos."
            ),
            "empanadas.jpg",
            true
        ));

        recetas.add(new Receta(
            12,
            "Pescado Rico al Horno",
            "Marina",
            "Chile",
            "Fácil",
            20,
            "Filetes de pescado con aceite de oliva y alcaparras, listo en 15 minutos.",
            "Cualquier pescado: reineta, blanquillo, tilapia, etc. Se compra fileteado o congelado. " +
            "Siempre es rico el pescado acompañado de una ensalada fresca de lechugas.",
            Arrays.asList(
                "Filetes de pescado al gusto (reineta, blanquillo, tilapia u otro)",
                "Aceite de oliva",
                "Alcaparras",
                "Sal y pimienta",
                "Ensalada de lechugas para acompañar"
            ),
            Arrays.asList(
                "En una fuente de horno o sartén con tapa poner un poco de aceite de oliva.",
                "Colocar los filetes de pescado encima.",
                "Chorrear un poco más de aceite de oliva sobre el pescado.",
                "Agregar unas alcaparras encima.",
                "Cocinar a fuego medio por 10 a 15 minutos con tapa.",
                "Servir acompañado de ensalada fresca de lechugas."
            ),
            "pescado.jpg",
            false
        ));

        recetas.add(new Receta(
            13,
            "Pescado sobre Cebolla con Crema",
            "Marina",
            "Chile",
            "Fácil",
            30,
            "Filetes de pescado sobre cama de cebollas salteadas, terminado con crema.",
            "Las cebollas se saltean en pluma hasta estar bien cocidas, luego el pescado se cocina encima " +
            "y se remata con crema. Se puede acompañar con papas cocidas o arroz blanco.",
            Arrays.asList(
                "2 cebollas en pluma",
                "Filetes de pescado al gusto",
                "Sal, pimienta y limón para el pescado",
                "200 g de crema",
                "Papas cocidas o arroz blanco para acompañar"
            ),
            Arrays.asList(
                "Saltear las cebollas en pluma en una olla hasta que estén bien cocidas.",
                "Aliñar aparte los filetes con sal, pimienta y limón.",
                "Cuando las cebollas estén listas, poner las presas de pescado sobre la cebolla.",
                "Dejar cocinar aproximadamente 15 minutos.",
                "Antes de servir, agregar 200 g de crema.",
                "Dejar cocinar 3 minutos más.",
                "Servir con papas cocidas o arroz blanco y ensalada."
            ),
            "pescado_cebolla.jpg",
            false
        ));

        recetas.add(new Receta(
            14,
            "Cazuela Casera",
            "Tradicional",
            "Chile",
            "Media",
            60,
            "Cazuela de pollo o carne con zapallo, papas, zanahoria, choclo y un puñado de arroz.",
            "De pollo o carne, con todas las verduras clásicas. Una presa por persona. " +
            "Las verduras se agregan en orden según dureza: primero las más duras, al final el choclo y el arroz.",
            Arrays.asList(
                "Presas de pollo o carne (1 por persona)",
                "Cebolla o puerros",
                "Zanahoria",
                "Zapallo en trozos",
                "Papas",
                "Porotos verdes",
                "Choclo",
                "Un puñado de arroz",
                "Sal al gusto"
            ),
            Arrays.asList(
                "Poner a cocer las presas con la cebolla y la zanahoria.",
                "Cuando la carne esté blanda, agregar el zapallo y las papas.",
                "Agregar los porotos verdes.",
                "Agregar un puñado de arroz.",
                "Al final agregar el choclo.",
                "Salar al gusto y servir bien caliente."
            ),
            "cazuela.jpg",
            true
        ));

        recetas.add(new Receta(
            15,
            "Leche Asada",
            "Postre",
            "Chile",
            "Media",
            70,
            "Postre clásico chileno de leche, huevos y caramelo, horneado a baño maría.",
            "Muy fácil. El caramelo requiere atención porque se quema muy rápido y queda amargo. " +
            "Tiene que estar rubio. Si tienes una fuente más grande, hacerlo a baño maría en el horno.",
            Arrays.asList(
                "1 litro de leche",
                "6 huevos",
                "3/4 taza de azúcar (para la mezcla)",
                "1/2 taza de azúcar (para el caramelo)",
                "2 cucharadas de agua (para el caramelo)",
                "Vainilla al gusto"
            ),
            Arrays.asList(
                "Batir los huevos, agregar el azúcar, la vainilla y la leche. Mezclar bien.",
                "En una sartén con teflón preparar el caramelo: poner media taza de azúcar con 2 cucharadas de agua.",
                "Esperar a que el caramelo esté rubio dorado (cuidado: se quema muy rápido y queda amargo).",
                "Volcar rápido el caramelo en la fuente para horno.",
                "Agregar inmediatamente el batido de leche y huevos.",
                "Opcional: poner la fuente dentro de otra más grande con agua para baño maría.",
                "Hornear por aproximadamente 1 hora.",
                "Está listo cuando al meter una cucharita tiene consistencia (está cuajado)."
            ),
            "leche_asada.jpg",
            true
        ));

        recetas.add(new Receta(
            16,
            "Arroz con Leche",
            "Postre",
            "Chile",
            "Fácil",
            30,
            "Cremoso arroz con leche con yema y crema opcional, cocinado a fuego lento.",
            "El azúcar se agrega solo cuando el arroz ya está cocido, si se pone antes el arroz queda duro. " +
            "Con una yema batida y un poco de crema queda mucho más rico.",
            Arrays.asList(
                "1 taza de arroz (lavada y escurrida)",
                "1 litro de leche (reservar 1/4 taza fría)",
                "1/2 taza de azúcar",
                "1 yema de huevo batida",
                "Cucharadas de crema (opcional, queda más rico)"
            ),
            Arrays.asList(
                "Lavar el arroz en colador y dejarlo reposar escurrido.",
                "Calentar el litro de leche (menos el cuarto de taza reservado).",
                "Agregar el arroz a la leche caliente.",
                "Cocinar a fuego lento por unos 20 minutos, revolviendo seguido para que no se pegue.",
                "Cuando el arroz esté bien cocido, agregar media taza de azúcar.",
                "Mezclar la yema batida con el cuarto de taza de leche fría reservada.",
                "Agregar esta mezcla al arroz junto con las cucharadas de crema.",
                "Revolver y apagar la olla."
            ),
            "arroz_leche.jpg",
            true
        ));
    }

    public List<Receta> getTodasLasRecetas() {
        return recetas;
    }

    public List<Receta> getRecetasPopulares() {
        List<Receta> populares = new ArrayList<>();
        for (Receta r : recetas) {
            if (r.isPopular()) populares.add(r);
        }
        return populares;
    }

    public Receta getRecetaPorId(int id) {
        for (Receta r : recetas) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public List<Receta> getRecetasRecientes() {
        // Últimas 6 ingresadas
        int total = recetas.size();
        return recetas.subList(Math.max(0, total - 6), total);
    }

    public List<Receta> buscarRecetas(String nombre, String tipoCocina,
                                      String ingrediente, String paisOrigen,
                                      String dificultad) {
        List<Receta> resultado = new ArrayList<>();
        for (Receta r : recetas) {
            boolean coincide = true;

            if (nombre != null && !nombre.isBlank()) {
                if (!r.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    coincide = false;
                }
            }
            if (tipoCocina != null && !tipoCocina.isBlank()) {
                if (!r.getTipoCocina().equalsIgnoreCase(tipoCocina)) {
                    coincide = false;
                }
            }
            if (ingrediente != null && !ingrediente.isBlank()) {
                boolean tieneIngrediente = false;
                for (String ing : r.getIngredientes()) {
                    if (ing.toLowerCase().contains(ingrediente.toLowerCase())) {
                        tieneIngrediente = true;
                        break;
                    }
                }
                if (!tieneIngrediente) coincide = false;
            }
            if (paisOrigen != null && !paisOrigen.isBlank()) {
                if (!r.getPaisOrigen().equalsIgnoreCase(paisOrigen)) {
                    coincide = false;
                }
            }
            if (dificultad != null && !dificultad.isBlank()) {
                if (!r.getDificultad().equalsIgnoreCase(dificultad)) {
                    coincide = false;
                }
            }

            if (coincide) resultado.add(r);
        }
        return resultado;
    }
}
