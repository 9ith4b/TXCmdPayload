# Extract JSP payload for Scorpion webshell

Scorpion webshell's JSP payload uses asm to manipulate java bytecode, and then encrypts it and sends it to the webshell side. Scorpion webshell is a GUI interface, it is not convenient to execute commands in batch, here the CMD is extracted independently to generate payload.class, which can be integrated into other programs to execute commands in batch.

## build

```shell
cd txws
mvn clean package
```

## run

```shell
cd target\classes
cp config .
cp lib .

java -cp lib/asm-9.0.jar; com.txws.CMD <command>

java -cp lib/asm-9.0.jar; com.txws.CMD dir  # Generate a payload that executes the dir command
```
