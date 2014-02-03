#!/bin/bash

cd HikariClientProtocolGen
java -cp "bin:lib/commons-collections-3.2.1.jar:lib/commons-io-2.4.jar:lib/commons-lang-2.4.jar:lib/commons-lang3-3.2.1.jar:lib/velocity-1.7.jar:../HikariProtocolDef/bin" com.luzi82.hikari.client.protocol.gen.Main

