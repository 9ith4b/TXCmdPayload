# 提取天蝎webshell的JSP的payload

天蝎webshell的JSP payload使用了asm来操作java字节码，然后加密发送到webshell端。天蝎webshell是GUI界面的，不便于批量执行命令，这里将CMD独立提取出来，生成payload.class，可以将其集成到其他程序中以批量执行命令。

## 编译

```
cd txws
mvn clean package
```

## 运行

```
cd target\classes
cp config .
cp lib .

java -cp lib/asm-9.0.jar; com.txws.CMD <command>

java -cp lib/asm-9.0.jar; com.txws.CMD dir  # 生成执行dir命令的payload
```
