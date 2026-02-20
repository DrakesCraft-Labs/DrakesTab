# DrakesTab

## Qué es
Tablist animado (header/footer) y scoreboard sidebar anti-flicker.

## Arquitectura
- `TabManager` gestiona frames, sidebar y placeholders internos.
- `VaultEconomyProvider` integra economía si existe Vault.

## Hecho
- Header/footer animados con MiniMessage.
- Sidebar con anti-flicker (teams buffer).
- Placeholders internos `%money%`, `%ping%`, `%tps%`.
- Soporte de PlaceholderAPI.

## Falta
- Auto-wrap de líneas largas (opcional).
- Perfil por mundo/permisos (opcional).

## Configuración
- `tab.yml` con comentarios in-line.
- Rank se obtiene por PlaceholderAPI (ej: `%drakesranks_rank%`).

## Dependencias
- Paper 1.20.6
- Java 21
- PlaceholderAPI (opcional)
- Vault + economía (opcional para Money)
- DrakesRanks (opcional, para placeholders de rango)
