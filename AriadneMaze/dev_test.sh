#!/bin/bash

. ./_env.sh

cd src

if [ "$#" = "0" ]; then
	#python manage.py test --failfast || exit 1
	python manage.py test --failfast am_profile || exit 1
elif [ "$#" = "1" ]; then
	python manage.py test ${1}
elif [ "$#" = "2" ]; then
	python manage.py test ${1}.SimpleTest.${2}
fi
