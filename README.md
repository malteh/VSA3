README
======

Starten des globalen Namensdienst:
----------------------------------

```java -cp ./globalns.jar globalns.NameServiceGlobal```
Im Verzeichis muss sich eine Datei ```global_nameservice.config``` befinden. Deren Aufbau ist : ```DEFAULT_GLOBAL_NS_PORT=22334```

Starten der vorgegebenen JARs:
------------------------------

```export GLOBAL_NS_HOST=Hostname```
```export GLOBAL_NS_PORT=22334```

Bank:

```java -cp bank.jar:bank_access.jar:mware_lib.jar bank.Bank $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA -v```

Filiale:

```java -cp filiale.jar:bank_access.jar:cash_access.jar:mware_lib.jar filiale.Filiale $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA  1 -v```

Geldautomat:

```java -cp geldautomat.jar:cash_access.jar:mware_lib.jar geldautomat.Geldautomat $GLOBAL_NS_HOST $GLOBAL_NS_PORT NDEA-1 -v```

Für die Middleware ist eine ```middleware.config``` mitgeliefert. Diese muss sich im Verzeichnis des JARs befinden.
