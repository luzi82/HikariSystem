#!/bin/bash

# adduser --disabled-password --gecos "" hikarisystem

PROJECT_PATH=`dirname ${PWD}`

apt-get install -y git python-pip python-dev nginx python-flup
pip install Django==1.6.1
easy_install decorator
easy_install South

ln -s ${PROJECT_PATH} /opt/HikariSystem

ln -s /opt/HikariSystem/deploy/hikarisystem-django.conf /etc/nginx/sites-available/hikarisystem-django.conf
ln -s /etc/nginx/sites-available/hikarisystem-django.conf /etc/nginx/sites-enabled/hikarisystem-django.conf
rm /etc/nginx/sites-enabled/default

ln -s /opt/HikariSystem/deploy/hikarisystem-django.sh /etc/init.d/hikarisystem-django.sh
update-rc.d hikarisystem-django.sh defaults 90

/etc/init.d/nginx restart
/etc/init.d/hikarisystem-django.sh restart
