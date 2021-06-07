@echo off
:: 程序开始启动
start javaw -Dfile.encoding=utf-8 -server -Xms1024m -Xmx4096m -XX:+UseG1GC -jar api.jar
:: 程序运行完成
exit