# Canary
An early warning of poison in the mines.

Canary is a mod that detects dangerous behaviors in other mods and provides several diagnostic tools.

### Detected Issues

#### BlockState Property Injection
Some mods try to add new BlockState properties to vanilla blocks.
Unfortunately, this is likely to cause block desyncs.

Solutions:
- Use [Statement](https://github.com/Virtuoel/Statement), a library designed to do this safely
- Use a custom block instead, converting between it and the vanilla block as needed

#### Synced Entity Data Injection
Some mods try to add new synced data entries (`EntityDataAccessor`s) to vanilla entity classes.
Doing this is likely to cause inscrutable networking errors, since field order is not stable between clients and the server.

Solutions:
- Use a [Data Attachment](https://docs.fabricmc.net/develop/data-attachments)
- Store values in normal fields, and sync them with custom packets

### Diagnostics

In addition, Canary has the following diagnostic features:
- when enabled in the config (`"print_blockstate_report": true`), a report of BlockStates will be printed to the log
- the command `/canaryclient diagnoseDesync` will generate a report of blockstates on the client and server

### Fun Fact
This mod is named after the real life practice of using canaries in coal mines to detect
poisonous gasses. Like the real life counterpart, if the canary dies (the game crashes),
you've got a problem.
