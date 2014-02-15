#!/bin/bash

# adduser --disabled-password --gecos "" hikariserver

PROJECT_PATH=`dirname ${PWD}`

apt-get install -y git python-pip python-dev nginx python-flup
pip install Django==1.6.1
easy_install decorator
easy_install South

ln -s ${PROJECT_PATH} /opt/HikariServer

ln -s /opt/HikariServer/deploy/hikariserver-django.conf /etc/nginx/sites-available/hikariserver-django.conf
ln -s /etc/nginx/sites-available/hikariserver-django.conf /etc/nginx/sites-enabled/hikariserver-django.conf
rm /etc/nginx/sites-enabled/default

ln -s /opt/HikariServer/deploy/hikariserver-django.sh /etc/init.d/hikariserver-django.sh
update-rc.d hikariserver-django.sh defaults 90

/etc/init.d/nginx restart
/etc/init.d/hikariserver-django.sh restart
