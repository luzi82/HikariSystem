#!/bin/bash

rm -rf data 
rm -f /run/shm/hikaridev.sqlite3

python manage_hikaridev.py syncdb --migrate --noinput

python manage_hikaridev.py shell < _dev_init.sh.py.in >> /dev/null

python manage_hikaridev.py csv_in
python manage_hikaridev.py csv_out
