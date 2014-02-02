#!/bin/bash

# adduser --disabled-password --gecos "" ariadnemaze

PROJECT_PATH=`dirname ${PWD}`

apt-get install -y git python-pip python-dev nginx python-flup
pip install Django==1.6.1
easy_install decorator
easy_install South

ln -s ${PROJECT_PATH} /opt/AriadneMaze

ln -s /opt/AriadneMaze/deploy/ariadnemaze-django.conf /etc/nginx/sites-available/ariadnemaze-django.conf
ln -s /etc/nginx/sites-available/ariadnemaze-django.conf /etc/nginx/sites-enabled/ariadnemaze-django.conf
rm /etc/nginx/sites-enabled/default

ln -s /opt/AriadneMaze/deploy/ariadnemaze-django.sh /etc/init.d/ariadnemaze-django.sh
update-rc.d ariadnemaze-django.sh defaults 90

/etc/init.d/nginx restart
/etc/init.d/ariadnemaze-django.sh restart
