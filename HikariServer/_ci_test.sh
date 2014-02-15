#!/bin/bash

cp src/HikariServer/secret.py.example src/HikariServer/secret.py || exit 1
./dev_test.sh || exit 1
