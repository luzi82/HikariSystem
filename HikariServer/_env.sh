#!/bin/bash

PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export PYTHONPATH=${PYTHONPATH}:${PROJECT_DIR}/import/MemHTTPServer/src
