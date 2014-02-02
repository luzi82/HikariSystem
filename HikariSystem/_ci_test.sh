#!/bin/bash

cp src/AriadneMaze/secret.py.example src/AriadneMaze/secret.py || exit 1
./dev_test.sh || exit 1
