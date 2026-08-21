<p align="center">
  <img src="https://raw.githubusercontent.com/DrakesCraft-Labs/DrakesTab/master/banner.svg" width="100%" alt="DRAKES TAB animated banner" />
</p>

# DrakesTab

> ### 🏰 ¡Únete a la Comunidad Oficial de DrakesCraft!
> 
> * 🎮 **IP del Servidor**: `play.drakescraft.cl` *(Java 1.21.11 & Bedrock)*
> * 💬 **Discord Oficial**: [discord.gg/drakescraft](https://discord.gg/rR7FbfCt9Y)
> * 🌐 **Web & Guía**: [web.drakescraft.cl](https://web.drakescraft.cl) — 🛒 **Tienda**: [web.drakescraft.cl/store](https://web.drakescraft.cl/store.html)
> 
> *¡Juega con este addon y más de 80 expansiones optimizadas en vivo en nuestra network de supervivencia técnica!*

---

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


## ⚖️ Upstream Attribution & License / Licencia y Créditos

- **Original Project / Upstream**: Slimefun4 Community Addon.
- **Port & Maintenance**: DrakesCraft Labs team (Compatibility for Paper / Purpur 1.21.11).
- **License**: GPL-3.0 / MIT.
- **Source Code**: [GitHub Repository](https://github.com/DrakesCraft-Labs/DrakesTab)
- **Support & Issues**: [GitHub Issues](https://github.com/DrakesCraft-Labs/DrakesTab/issues) | [Discord](https://discord.gg/rR7FbfCt9Y)

*This project is an open-source derivative work maintained by DrakesCraft Labs under the terms of its original license. All original assets and concepts belong to their respective creators.*
