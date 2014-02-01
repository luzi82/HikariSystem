#!/bin/bash

PROJECT_PATH=`dirname ${PWD}`

apt-get install -y git python-pip python-dev nginx python-flup
easy_install decorator

ln -s ${PROJECT_PATH} /opt/AriadneMaze

adduser --disabled-password --gecos "" ariadnemaze

ln -s /opt/AriadneMaze/deploy/ariadnemaze-django.conf /etc/nginx/sites-available/ariadnemaze-django.conf
ln -s /etc/nginx/sites-available/ariadnemaze-django.conf /etc/nginx/sites-enabled/ariadnemaze-django.conf
rm /etc/nginx/sites-enabled/default

ln -s /opt/AriadneMaze/deploy/ariadnemaze-django.sh /etc/init.d/ariadnemaze-django.sh
update-rc.d ariadnemaze-django.sh defaults 90
