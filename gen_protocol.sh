#!/bin/bash

cd HikariClientProtocolGen
java -cp "bin:../import/lib/apache/commons-collections-3.2.1.jar:../import/lib/apache/commons-io-2.4.jar:../import/lib/apache/commons-lang-2.4.jar:../import/lib/apache/velocity-1.7.jar:../HikariProtocolDef/bin" com.luzi82.hikari.client.protocol.gen.Main

