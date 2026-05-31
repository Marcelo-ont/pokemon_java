import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class PokemonApp extends Application {
    private final Random random = new Random();
    private final CatalogoPokemon catalogo = new CatalogoPokemon();
    private final ObservableList<Jugador> jugadores = FXCollections.observableArrayList();
    private final ObservableList<String> mensajes = FXCollections.observableArrayList();

    private ListView<Jugador> listaJugadores;
    private TextField campoNombre;
    private Button botonPreparar;
    private Button botonCombate;
    private Button botonCambiar;
    private Button botonPocion;
    private Button botonSuperPocion;
    private Button botonCapturar;
    private Button[] botonesAtaque;
    private ComboBox<Pokemon> selectorPokemon;
    private VBox cajaEquipo;
    private ListView<String> listaMensajes;
    private Label etiquetaEstado;
    private Label etiquetaJugador;
    private Label etiquetaStats;
    private Label etiquetaInventario;
    private Label etiquetaPokemonJugador;
    private Label etiquetaTipoJugador;
    private Label etiquetaVidaJugador;
    private ProgressBar barraVidaJugador;
    private Label etiquetaEnemigo;
    private Label etiquetaTipoEnemigo;
    private Label etiquetaVidaEnemigo;
    private ProgressBar barraVidaEnemigo;

    private Jugador jugadorActual;
    private Pokemon pokemonActual;
    private Pokemon enemigo;
    private Partida partidaActual;
    private Pokemon[] usados;
    private int cantidadUsados;
    private int numeroPartida = 1;
    private boolean combateActivo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(crearMenu(stage));
        root.setLeft(crearPanelJugadores());
        root.setCenter(crearPanelCombate());
        root.setBottom(crearBarraEstado());
        root.setStyle("-fx-background-color: #f5f7fb; -fx-font-family: 'Avenir Next', 'Segoe UI', sans-serif;");

        mensajes.add("Agrega un jugador para comenzar.");

        Scene scene = new Scene(root, 980, 650);
        stage.setTitle("Proyecto Pokemon");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();

        refrescarPantalla();
    }

    private MenuBar crearMenu(Stage stage) {
        MenuItem agregar = new MenuItem("Agregar jugador");
        agregar.setOnAction(event -> {
            campoNombre.requestFocus();
            etiquetaEstado.setText("Escribe el nombre y presiona Agregar.");
        });

        MenuItem preparar = new MenuItem("Preparar equipo");
        preparar.setOnAction(event -> prepararEquipo());

        MenuItem combate = new MenuItem("Nuevo combate");
        combate.setOnAction(event -> iniciarCombate());

        MenuItem limpiarRegistro = new MenuItem("Limpiar registro");
        limpiarRegistro.setOnAction(event -> mensajes.clear());

        MenuItem salir = new MenuItem("Salir");
        salir.setOnAction(event -> stage.close());

        Menu juego = new Menu("Juego");
        juego.getItems().addAll(agregar, preparar, combate, new SeparatorMenuItem(), limpiarRegistro, salir);

        MenuItem comoJugar = new MenuItem("Como jugar");
        comoJugar.setOnAction(event -> mostrarAyuda("Como jugar",
                "1. Escribe el nombre de un jugador y presiona Agregar.\n"
                        + "2. Selecciona al jugador en la lista.\n"
                        + "3. Presiona Nuevo combate para generar un Pokemon salvaje.\n"
                        + "4. Usa ataques hasta debilitarlo o baja su vida para capturarlo.\n"
                        + "5. Al ganar puedes recibir experiencia, pociones o pokeballs."));

        MenuItem botones = new MenuItem("Que hace cada boton");
        botones.setOnAction(event -> mostrarAyuda("Que hace cada cosa",
                "Agregar: crea un jugador con el nombre escrito.\n"
                        + "Preparar equipo: reinicia el equipo del jugador con 3 Pokemon iniciales y objetos.\n"
                        + "Nuevo combate: empieza una pelea contra un Pokemon salvaje.\n"
                        + "Ataques: hacen danio al rival; el tipo puede aumentar o reducir el danio.\n"
                        + "Pocion: cura 20 puntos de vida.\n"
                        + "Super pocion: cura toda la vida.\n"
                        + "Capturar: intenta agregar el Pokemon salvaje al equipo si tiene poca vida.\n"
                        + "Cambiar: cambia tu Pokemon activo durante el combate.\n"
                        + "Registro: muestra lo que va pasando en la partida."));

        MenuItem tipos = new MenuItem("Tipos");
        tipos.setOnAction(event -> mostrarAyuda("Tipos de Pokemon",
                "Fuego hace mas danio a planta y menos a agua.\n"
                        + "Agua hace mas danio a fuego y menos a planta.\n"
                        + "Planta hace mas danio a agua y menos a fuego.\n"
                        + "Si ambos son del mismo tipo, el danio baja."));

        Menu ayuda = new Menu("Ayuda");
        ayuda.getItems().addAll(comoJugar, botones, tipos);

        return new MenuBar(juego, ayuda);
    }

    private VBox crearPanelJugadores() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(18));
        panel.setPrefWidth(285);
        panel.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dde3ee; -fx-border-width: 0 1 0 0;");

        Label titulo = new Label("Jugadores");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1f2937;");

        campoNombre = new TextField();
        campoNombre.setPromptText("Nombre");
        campoNombre.setOnAction(event -> agregarJugador());
        campoNombre.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #cbd5e1; -fx-padding: 8 10;");
        campoNombre.setTooltip(new Tooltip("Escribe aqui el nombre del jugador nuevo."));

        Button botonAgregar = new Button("Agregar");
        botonAgregar.setMaxWidth(Double.MAX_VALUE);
        botonAgregar.setOnAction(event -> agregarJugador());
        botonAgregar.setStyle(estiloBoton("#2563eb", "#ffffff"));
        botonAgregar.setTooltip(new Tooltip("Crea un jugador y le da un equipo inicial."));

        listaJugadores = new ListView<>(jugadores);
        listaJugadores.setTooltip(new Tooltip("Selecciona aqui el jugador que va a jugar."));
        listaJugadores.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Jugador jugador, boolean empty) {
                super.updateItem(jugador, empty);
                if (empty || jugador == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(jugador.getNombre() + "\n" + jugador.resumenEstadisticas());
                    setStyle("-fx-padding: 10; -fx-background-radius: 8;");
                }
            }
        });
        listaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (combateActivo && nuevo != jugadorActual) {
                listaJugadores.getSelectionModel().select(jugadorActual);
                etiquetaEstado.setText("Termina el combate actual antes de cambiar de jugador.");
                return;
            }
            jugadorActual = nuevo;
            refrescarPantalla();
        });
        VBox.setVgrow(listaJugadores, Priority.ALWAYS);

        botonPreparar = new Button("Preparar equipo");
        botonPreparar.setMaxWidth(Double.MAX_VALUE);
        botonPreparar.setOnAction(event -> prepararEquipo());
        botonPreparar.setStyle(estiloBoton("#059669", "#ffffff"));
        botonPreparar.setTooltip(new Tooltip("Reinicia el equipo, pociones y pokeballs del jugador seleccionado."));

        botonCombate = new Button("Nuevo combate");
        botonCombate.setMaxWidth(Double.MAX_VALUE);
        botonCombate.setOnAction(event -> iniciarCombate());
        botonCombate.setStyle(estiloBoton("#dc2626", "#ffffff"));
        botonCombate.setTooltip(new Tooltip("Empieza una pelea contra un Pokemon salvaje."));

        etiquetaStats = new Label();
        etiquetaStats.setWrapText(true);
        etiquetaStats.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px;");

        panel.getChildren().addAll(titulo, campoNombre, botonAgregar, listaJugadores, botonPreparar, botonCombate, etiquetaStats);
        return panel;
    }

    private VBox crearPanelCombate() {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(20));

        HBox encabezado = new HBox(12);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        etiquetaJugador = new Label("Selecciona un jugador");
        etiquetaJugador.setStyle("-fx-font-size: 24px; -fx-font-weight: 800; -fx-text-fill: #111827;");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        etiquetaInventario = new Label();
        etiquetaInventario.setStyle("-fx-background-color: #eef2ff; -fx-text-fill: #3730a3; -fx-padding: 7 10; -fx-background-radius: 999;");
        encabezado.getChildren().addAll(etiquetaJugador, espacio, etiquetaInventario);

        HBox tablero = new HBox(14);
        tablero.getChildren().addAll(crearPanelPokemonJugador(), crearPanelPokemonEnemigo());
        VBox.setVgrow(tablero, Priority.ALWAYS);

        GridPane acciones = new GridPane();
        acciones.setHgap(10);
        acciones.setVgap(10);
        acciones.setPadding(new Insets(2, 0, 0, 0));

        botonesAtaque = new Button[3];
        for (int i = 0; i < botonesAtaque.length; i++) {
            final int indice = i;
            botonesAtaque[i] = new Button("Ataque " + (i + 1));
            botonesAtaque[i].setMaxWidth(Double.MAX_VALUE);
            botonesAtaque[i].setOnAction(event -> atacar(indice));
            botonesAtaque[i].setStyle(estiloBoton("#334155", "#ffffff"));
            botonesAtaque[i].setTooltip(new Tooltip("Ataca al Pokemon salvaje con este movimiento."));
            acciones.add(botonesAtaque[i], i, 0);
        }

        botonPocion = new Button("Pocion");
        botonPocion.setMaxWidth(Double.MAX_VALUE);
        botonPocion.setOnAction(event -> usarPocion(false));
        botonPocion.setStyle(estiloBoton("#f59e0b", "#111827"));
        botonPocion.setTooltip(new Tooltip("Cura 20 puntos de vida al Pokemon activo."));

        botonSuperPocion = new Button("Super pocion");
        botonSuperPocion.setMaxWidth(Double.MAX_VALUE);
        botonSuperPocion.setOnAction(event -> usarPocion(true));
        botonSuperPocion.setStyle(estiloBoton("#10b981", "#ffffff"));
        botonSuperPocion.setTooltip(new Tooltip("Cura toda la vida del Pokemon activo."));

        botonCapturar = new Button("Capturar");
        botonCapturar.setMaxWidth(Double.MAX_VALUE);
        botonCapturar.setOnAction(event -> capturar());
        botonCapturar.setStyle(estiloBoton("#7c3aed", "#ffffff"));
        botonCapturar.setTooltip(new Tooltip("Usa una pokeball; funciona cuando el rival tiene 25% de vida o menos."));

        acciones.add(botonPocion, 0, 1);
        acciones.add(botonSuperPocion, 1, 1);
        acciones.add(botonCapturar, 2, 1);
        for (int i = 0; i < 3; i++) {
            acciones.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(160, 220, Double.MAX_VALUE, Priority.ALWAYS, null, true));
        }

        Label tituloRegistro = new Label("Registro");
        tituloRegistro.setStyle("-fx-font-weight: 800; -fx-text-fill: #1f2937;");
        listaMensajes = new ListView<>(mensajes);
        listaMensajes.setPrefHeight(150);
        listaMensajes.setStyle("-fx-background-radius: 8; -fx-border-color: #dde3ee; -fx-border-radius: 8;");
        listaMensajes.setTooltip(new Tooltip("Aqui aparece el historial de acciones del combate."));

        panel.getChildren().addAll(encabezado, tablero, acciones, tituloRegistro, listaMensajes);
        return panel;
    }

    private VBox crearPanelPokemonJugador() {
        VBox panel = crearPanelBasePokemon("#e0f2fe");
        Label titulo = new Label("Tu pokemon");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #075985;");

        etiquetaPokemonJugador = new Label("-");
        etiquetaPokemonJugador.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #0f172a;");
        etiquetaTipoJugador = crearEtiquetaTipo();
        etiquetaVidaJugador = crearEtiquetaVida();
        barraVidaJugador = crearBarraVida();

        selectorPokemon = new ComboBox<>();
        selectorPokemon.setMaxWidth(Double.MAX_VALUE);
        selectorPokemon.setTooltip(new Tooltip("Elige otro Pokemon vivo de tu equipo."));
        selectorPokemon.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Pokemon pokemon, boolean empty) {
                super.updateItem(pokemon, empty);
                setText(empty || pokemon == null ? null : pokemon.resumen());
            }
        });
        selectorPokemon.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pokemon pokemon, boolean empty) {
                super.updateItem(pokemon, empty);
                setText(empty || pokemon == null ? "Equipo" : pokemon.getNombre());
            }
        });

        botonCambiar = new Button("Cambiar");
        botonCambiar.setMaxWidth(Double.MAX_VALUE);
        botonCambiar.setOnAction(event -> cambiarPokemon());
        botonCambiar.setStyle(estiloBoton("#0284c7", "#ffffff"));
        botonCambiar.setTooltip(new Tooltip("Cambia el Pokemon activo por el seleccionado."));

        cajaEquipo = new VBox(7);
        VBox.setVgrow(cajaEquipo, Priority.ALWAYS);

        panel.getChildren().addAll(titulo, etiquetaPokemonJugador, etiquetaTipoJugador, etiquetaVidaJugador, barraVidaJugador,
                selectorPokemon, botonCambiar, cajaEquipo);
        return panel;
    }

    private VBox crearPanelPokemonEnemigo() {
        VBox panel = crearPanelBasePokemon("#fff7ed");
        Label titulo = new Label("Pokemon salvaje");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #9a3412;");

        etiquetaEnemigo = new Label("-");
        etiquetaEnemigo.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #0f172a;");
        etiquetaTipoEnemigo = crearEtiquetaTipo();
        etiquetaVidaEnemigo = crearEtiquetaVida();
        barraVidaEnemigo = crearBarraVida();

        Label pista = new Label("Baja su vida a 25% o menos para capturarlo.");
        pista.setWrapText(true);
        pista.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

        panel.getChildren().addAll(titulo, etiquetaEnemigo, etiquetaTipoEnemigo, etiquetaVidaEnemigo, barraVidaEnemigo, pista);
        return panel;
    }

    private VBox crearPanelBasePokemon(String colorFondo) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(18));
        panel.setMinWidth(280);
        panel.setStyle("-fx-background-color: " + colorFondo + "; -fx-background-radius: 8; -fx-border-color: #dbe2ea; -fx-border-radius: 8;");
        HBox.setHgrow(panel, Priority.ALWAYS);
        return panel;
    }

    private HBox crearBarraEstado() {
        HBox barra = new HBox();
        barra.setPadding(new Insets(9, 18, 9, 18));
        barra.setStyle("-fx-background-color: #111827;");
        etiquetaEstado = new Label("Listo.");
        etiquetaEstado.setStyle("-fx-text-fill: #f8fafc;");
        barra.getChildren().add(etiquetaEstado);
        return barra;
    }

    private Label crearEtiquetaTipo() {
        Label label = new Label("-");
        label.setStyle("-fx-padding: 4 9; -fx-background-radius: 999; -fx-font-size: 12px; -fx-font-weight: 800;");
        return label;
    }

    private Label crearEtiquetaVida() {
        Label label = new Label("HP 0/0");
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        return label;
    }

    private ProgressBar crearBarraVida() {
        ProgressBar barra = new ProgressBar(0);
        barra.setMaxWidth(Double.MAX_VALUE);
        barra.setMinHeight(14);
        return barra;
    }

    private void mostrarAyuda(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ayuda");
        alerta.setHeaderText(titulo);
        alerta.setContentText(contenido);
        alerta.getDialogPane().setPrefWidth(560);
        alerta.showAndWait();
    }

    private void agregarJugador() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            etiquetaEstado.setText("Escribe un nombre para el jugador.");
            return;
        }
        if (buscarJugador(nombre) != null) {
            etiquetaEstado.setText("Ese jugador ya existe.");
            return;
        }

        Jugador jugador = new Jugador(nombre);
        jugador.prepararEquipo(catalogo, random);
        jugadores.add(jugador);
        listaJugadores.getSelectionModel().select(jugador);
        campoNombre.clear();
        mensajes.add("Jugador registrado: " + jugador.getNombre() + ".");
        etiquetaEstado.setText("Jugador agregado.");
        refrescarPantalla();
    }

    private Jugador buscarJugador(String nombre) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                return jugador;
            }
        }
        return null;
    }

    private void prepararEquipo() {
        Jugador jugador = listaJugadores.getSelectionModel().getSelectedItem();
        if (jugador == null) {
            etiquetaEstado.setText("Selecciona un jugador.");
            return;
        }
        if (combateActivo) {
            etiquetaEstado.setText("No puedes preparar equipo durante un combate.");
            return;
        }

        jugador.prepararEquipo(catalogo, random);
        jugadorActual = jugador;
        mensajes.clear();
        mensajes.add("Equipo preparado para " + jugador.getNombre() + ".");
        etiquetaEstado.setText("Equipo listo.");
        refrescarPantalla();
    }

    private void iniciarCombate() {
        Jugador jugador = listaJugadores.getSelectionModel().getSelectedItem();
        if (jugador == null) {
            etiquetaEstado.setText("Selecciona un jugador.");
            return;
        }
        if (combateActivo) {
            etiquetaEstado.setText("Ya hay un combate activo.");
            return;
        }
        if (jugador.getCantidadPokemones() == 0 || !jugador.tienePokemonVivos()) {
            jugador.prepararEquipo(catalogo, random);
        }

        jugadorActual = jugador;
        pokemonActual = primerPokemonVivo(jugador);
        enemigo = catalogo.crearSalvaje(random);
        partidaActual = new Partida(numeroPartida, new Jugador[]{jugadorActual});
        usados = new Pokemon[6];
        cantidadUsados = 0;
        combateActivo = true;

        mensajes.clear();
        mensajes.add("Partida " + numeroPartida + ": " + pokemonActual.getNombre() + " contra " + enemigo.getNombre() + ".");
        numeroPartida++;
        etiquetaEstado.setText("Combate iniciado.");
        refrescarPantalla();
    }

    private void atacar(int indiceAtaque) {
        if (!combateListo()) {
            return;
        }

        registrarUsado(pokemonActual);
        Ataque ataque = pokemonActual.getAtaques()[indiceAtaque];
        int danio = pokemonActual.atacar(enemigo, indiceAtaque);
        double multiplicador = TablaTipos.multiplicador(ataque.getTipo(), enemigo.getTipo());
        mensajes.add(pokemonActual.getNombre() + " uso " + ataque.getNombre() + " e hizo " + danio + " de danio.");
        if (multiplicador > 1.0) {
            mensajes.add("Es super efectivo.");
        } else if (multiplicador < 1.0) {
            mensajes.add("No es muy efectivo.");
        }

        if (enemigo.estaDebilitado()) {
            finalizarVictoria(false);
            return;
        }

        turnoEnemigo();
        refrescarPantalla();
    }

    private void usarPocion(boolean superPocion) {
        if (!combateListo()) {
            return;
        }

        Usable item = superPocion ? new SuperPocion() : new Pocion();
        mensajes.add(item.usar(jugadorActual, pokemonActual));
        etiquetaEstado.setText("Item usado.");
        refrescarPantalla();
    }

    private void capturar() {
        if (!combateListo()) {
            return;
        }
        if (jugadorActual.getPokeballs() <= 0) {
            mensajes.add("No tienes pokeballs.");
            etiquetaEstado.setText("Sin pokeballs.");
            refrescarPantalla();
            return;
        }

        jugadorActual.setPokeballs(jugadorActual.getPokeballs() - 1);
        if (enemigo.getVidaActual() > enemigo.getVidaMaxima() / 4) {
            mensajes.add("El pokemon sigue demasiado fuerte.");
            etiquetaEstado.setText("Captura fallida.");
            refrescarPantalla();
            return;
        }

        if (!jugadorActual.agregarPokemon(catalogo.crearPorNombre(enemigo.getNombre()))) {
            mensajes.add("Tu equipo esta lleno.");
            etiquetaEstado.setText("Equipo lleno.");
            refrescarPantalla();
            return;
        }

        mensajes.add("Capturaste a " + enemigo.getNombre() + ".");
        partidaActual.registrarCaptura(jugadorActual);
        partidaActual.registrarVictoria(jugadorActual, 5);
        repartirRecompensa();
        cerrarCombate("Captura completa.");
    }

    private void cambiarPokemon() {
        if (!combateListo()) {
            return;
        }

        Pokemon elegido = selectorPokemon.getSelectionModel().getSelectedItem();
        if (elegido == null || elegido == pokemonActual || elegido.estaDebilitado()) {
            etiquetaEstado.setText("Elige otro pokemon vivo.");
            return;
        }

        pokemonActual = elegido;
        mensajes.add("Ahora pelea " + pokemonActual.getNombre() + ".");
        etiquetaEstado.setText("Pokemon cambiado.");
        refrescarPantalla();
    }

    private void turnoEnemigo() {
        int indice = random.nextInt(enemigo.getAtaques().length);
        Ataque ataque = enemigo.getAtaques()[indice];
        int danio = enemigo.atacar(pokemonActual, indice);
        mensajes.add(enemigo.getNombre() + " uso " + ataque.getNombre() + " e hizo " + danio + " de danio.");

        if (!pokemonActual.estaDebilitado()) {
            return;
        }

        mensajes.add(pokemonActual.getNombre() + " se debilito.");
        if (!jugadorActual.tienePokemonVivos()) {
            finalizarDerrota();
            return;
        }

        pokemonActual = primerPokemonVivo(jugadorActual);
        mensajes.add("Sale " + pokemonActual.getNombre() + ".");
    }

    private void finalizarVictoria(boolean porCaptura) {
        if (!porCaptura) {
            mensajes.add("Ganaste el combate.");
            partidaActual.registrarVictoria(jugadorActual, 10);
            darExperiencia();
            repartirRecompensa();
        }
        cerrarCombate("Victoria registrada.");
    }

    private void finalizarDerrota() {
        mensajes.add("Perdiste el combate.");
        partidaActual.registrarDerrota(jugadorActual);
        cerrarCombate("Derrota registrada.");
    }

    private void cerrarCombate(String estado) {
        if (partidaActual != null) {
            partidaActual.cerrar();
        }
        partidaActual = null;
        enemigo = null;
        combateActivo = false;
        etiquetaEstado.setText(estado);
        refrescarPantalla();
    }

    private void darExperiencia() {
        Pokemon[] unicos = new Pokemon[cantidadUsados];
        int totalUnicos = 0;
        for (int i = 0; i < cantidadUsados; i++) {
            if (!contiene(unicos, totalUnicos, usados[i])) {
                unicos[totalUnicos] = usados[i];
                totalUnicos++;
            }
        }

        for (int i = 0; i < totalUnicos; i++) {
            Pokemon pokemon = unicos[i];
            int experiencia = 5 + random.nextInt(6);
            mensajes.add(pokemon.getNombre() + " gano " + experiencia + " de experiencia.");
            int subidas = pokemon.ganarExperiencia(experiencia);
            for (int j = 0; j < subidas; j++) {
                mensajes.add(pokemon.getNombre() + " subio a nivel " + pokemon.getNivel() + ".");
            }
            String evolucion = catalogo.evolucionarSiPuede(pokemon);
            if (!evolucion.isEmpty()) {
                mensajes.add(evolucion);
            }
        }
    }

    private void repartirRecompensa() {
        if (random.nextBoolean()) {
            jugadorActual.setPociones(jugadorActual.getPociones() + 1);
            mensajes.add("Recibiste 1 pocion.");
        }
        if (random.nextInt(4) == 0) {
            jugadorActual.setSuperPociones(jugadorActual.getSuperPociones() + 1);
            mensajes.add("Recibiste 1 super pocion.");
        }
        if (random.nextBoolean()) {
            jugadorActual.setPokeballs(jugadorActual.getPokeballs() + 1);
            mensajes.add("Recibiste 1 pokeball.");
        }
    }

    private boolean combateListo() {
        if (!combateActivo || jugadorActual == null || pokemonActual == null || enemigo == null) {
            etiquetaEstado.setText("Inicia un combate primero.");
            return false;
        }
        return true;
    }

    private Pokemon primerPokemonVivo(Jugador jugador) {
        Pokemon[] equipo = jugador.getPokemones();
        for (int i = 0; i < jugador.getCantidadPokemones(); i++) {
            if (!equipo[i].estaDebilitado()) {
                return equipo[i];
            }
        }
        return null;
    }

    private void registrarUsado(Pokemon pokemon) {
        if (pokemon == null || contiene(usados, cantidadUsados, pokemon)) {
            return;
        }
        usados[cantidadUsados] = pokemon;
        cantidadUsados++;
    }

    private boolean contiene(Pokemon[] arreglo, int total, Pokemon pokemon) {
        for (int i = 0; i < total; i++) {
            if (arreglo[i] == pokemon) {
                return true;
            }
        }
        return false;
    }

    private void refrescarPantalla() {
        Jugador seleccionado = listaJugadores == null ? null : listaJugadores.getSelectionModel().getSelectedItem();
        if (!combateActivo) {
            jugadorActual = seleccionado;
            pokemonActual = jugadorActual == null ? null : primerPokemonVivo(jugadorActual);
            enemigo = null;
        }

        refrescarJugador();
        refrescarPokemonJugador();
        refrescarEnemigo();
        refrescarEquipo();
        refrescarAcciones();
        listaJugadores.refresh();
        if (listaMensajes != null && !mensajes.isEmpty()) {
            listaMensajes.scrollTo(mensajes.size() - 1);
        }
    }

    private void refrescarJugador() {
        if (jugadorActual == null) {
            etiquetaJugador.setText("Sin jugador");
            etiquetaInventario.setText("Inventario vacio");
            etiquetaStats.setText("");
            return;
        }

        etiquetaJugador.setText(jugadorActual.getNombre());
        etiquetaInventario.setText("Pokeballs " + jugadorActual.getPokeballs()
                + " | Pociones " + jugadorActual.getPociones()
                + " | Super " + jugadorActual.getSuperPociones());
        etiquetaStats.setText(jugadorActual.resumenEstadisticas());
    }

    private void refrescarPokemonJugador() {
        if (pokemonActual == null) {
            etiquetaPokemonJugador.setText("-");
            etiquetaTipoJugador.setText("-");
            etiquetaTipoJugador.setStyle(estiloTipo("normal"));
            etiquetaVidaJugador.setText("HP 0/0");
            barraVidaJugador.setProgress(0);
            return;
        }

        etiquetaPokemonJugador.setText(pokemonActual.getNombre());
        etiquetaTipoJugador.setText(pokemonActual.getTipo().toUpperCase());
        etiquetaTipoJugador.setStyle(estiloTipo(pokemonActual.getTipo()));
        etiquetaVidaJugador.setText("HP " + pokemonActual.getVidaActual() + "/" + pokemonActual.getVidaMaxima());
        barraVidaJugador.setProgress(progresoVida(pokemonActual));
    }

    private void refrescarEnemigo() {
        if (enemigo == null) {
            etiquetaEnemigo.setText("-");
            etiquetaTipoEnemigo.setText("-");
            etiquetaTipoEnemigo.setStyle(estiloTipo("normal"));
            etiquetaVidaEnemigo.setText("HP 0/0");
            barraVidaEnemigo.setProgress(0);
            return;
        }

        etiquetaEnemigo.setText(enemigo.getNombre());
        etiquetaTipoEnemigo.setText(enemigo.getTipo().toUpperCase());
        etiquetaTipoEnemigo.setStyle(estiloTipo(enemigo.getTipo()));
        etiquetaVidaEnemigo.setText("HP " + enemigo.getVidaActual() + "/" + enemigo.getVidaMaxima());
        barraVidaEnemigo.setProgress(progresoVida(enemigo));
    }

    private void refrescarEquipo() {
        cajaEquipo.getChildren().clear();
        selectorPokemon.getItems().clear();
        if (jugadorActual == null || jugadorActual.getCantidadPokemones() == 0) {
            cajaEquipo.getChildren().add(crearTextoSecundario("Sin equipo."));
            return;
        }

        Pokemon[] equipo = jugadorActual.getPokemones();
        for (int i = 0; i < jugadorActual.getCantidadPokemones(); i++) {
            Pokemon pokemon = equipo[i];
            cajaEquipo.getChildren().add(crearFilaEquipo(pokemon));
            if (!pokemon.estaDebilitado()) {
                selectorPokemon.getItems().add(pokemon);
            }
        }
        selectorPokemon.getSelectionModel().select(pokemonActual);
    }

    private HBox crearFilaEquipo(Pokemon pokemon) {
        HBox fila = new HBox(8);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(7));
        fila.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

        Label nombre = new Label(pokemon.getNombre());
        nombre.setStyle("-fx-font-weight: 800; -fx-text-fill: #172033;");
        Label vida = new Label(pokemon.getVidaActual() + "/" + pokemon.getVidaMaxima());
        vida.setStyle("-fx-text-fill: #64748b;");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        Label tipo = new Label(pokemon.getTipo());
        tipo.setStyle(estiloTipo(pokemon.getTipo()));

        fila.getChildren().addAll(nombre, vida, espacio, tipo);
        return fila;
    }

    private Label crearTextoSecundario(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-text-fill: #64748b;");
        return label;
    }

    private void refrescarAcciones() {
        boolean activo = combateActivo && pokemonActual != null && enemigo != null;
        botonPreparar.setDisable(jugadorActual == null || combateActivo);
        botonCombate.setDisable(jugadorActual == null || combateActivo);
        botonCambiar.setDisable(!activo);
        botonPocion.setDisable(!activo);
        botonSuperPocion.setDisable(!activo);
        botonCapturar.setDisable(!activo);
        selectorPokemon.setDisable(!activo);

        for (int i = 0; i < botonesAtaque.length; i++) {
            Button boton = botonesAtaque[i];
            boton.setDisable(!activo);
            if (activo && i < pokemonActual.getAtaques().length) {
                Ataque ataque = pokemonActual.getAtaques()[i];
                boton.setText(ataque.getNombre() + " (" + ataque.getDanio() + ")");
            } else {
                boton.setText("Ataque " + (i + 1));
            }
        }
    }

    private double progresoVida(Pokemon pokemon) {
        if (pokemon.getVidaMaxima() == 0) {
            return 0;
        }
        return (double) pokemon.getVidaActual() / pokemon.getVidaMaxima();
    }

    private String estiloTipo(String tipo) {
        String fondo = "#e2e8f0";
        String texto = "#334155";
        if ("fuego".equals(tipo)) {
            fondo = "#fee2e2";
            texto = "#991b1b";
        } else if ("agua".equals(tipo)) {
            fondo = "#dbeafe";
            texto = "#1d4ed8";
        } else if ("planta".equals(tipo)) {
            fondo = "#dcfce7";
            texto = "#166534";
        }
        return "-fx-background-color: " + fondo + "; -fx-text-fill: " + texto
                + "; -fx-padding: 4 9; -fx-background-radius: 999; -fx-font-size: 12px; -fx-font-weight: 800;";
    }

    private String estiloBoton(String fondo, String texto) {
        return "-fx-background-color: " + fondo
                + "; -fx-text-fill: " + texto
                + "; -fx-font-weight: 800; -fx-background-radius: 8; -fx-padding: 9 12;";
    }
}
