#!/bin/bash

cp src/HikariSystem/secret.py.example src/HikariSystem/secret.py || exit 1
./dev_test.sh || exit 1
