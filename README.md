# Canary
An early warning of poison in the mines.

Canary is a mod that detects dangerous behavior of other mods.

Current problems that Canary detects:
- Registration of new EntityDataSerializers
- Unsafe registration of entity tracked data
- Adding new BlockState properties to vanilla blocks

In addition, Canary has the following diagnostic features:
- when enabled in the config (`"print_blockstate_report": true`), a report of BlockStates will be printed to the log
- the command `/canaryclient diagnoseDesync` will generate a report of blockstates on the client and server

### Fun Fact
This mod is named after the real life practice of using canaries in coal mines to detect
poisonous gasses. Like the real life counterpart, if the canary dies (the game crashes),
you've got a problem.
