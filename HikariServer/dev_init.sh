#!/bin/bash

rm -rf data 
rm -f db.sqlite3

python manage.py syncdb --migrate --noinput

python manage.py shell < _dev_init.sh.py.in >> /dev/null

python manage.py csv_in
python manage.py csv_out
