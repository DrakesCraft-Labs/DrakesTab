# DrakesTab

Plugin de tablist + sidebar, extraido del modulo `drakestab` del antiguo `DrakesCore`.

## Objetivo
Mostrar informacion dinamica de red en Tab y Scoreboard con bajo flicker.

## Que hace hoy
- Header/Footer animado por frames en `tab.yml`.
- Sidebar con estrategia anti-flicker usando scoreboard teams.
- Variables internas: `%money%`, `%ping%`, `%tps%`.
- Truncado configurable de lineas largas para reducir glitches de sidebar.
- Soporta PlaceholderAPI en header, footer y lineas.
- Integra Vault (si existe) para balance economico.
- Comando admin `/drakestab reload|status`.

## Integracion con otros plugins
- `DrakesRanks` via placeholders PAPI (ej: `%drakesranks_rank%`).
- `Vault` + provider de economia para `%money%`.

## Configuracion
- `src/main/resources/tab.yml`
- Control de intervalos de update para tab y sidebar.

## Dependencias
- Paper 1.20.6
- Java 21
- PlaceholderAPI (opcional)
- Vault + provider economia (opcional)

## Pendiente real
- Perfilado por mundo o grupo de permisos.
- Wrap/truncado inteligente para lineas largas.
- Modo packet-level para redes muy grandes.
