#!/bin/bash

if [ "$#" = "0" ]; then
	python manage_hikaridev.py test --failfast || exit 1
elif [ "$#" = "1" ]; then
	python manage_hikaridev.py test ${1}
elif [ "$#" = "2" ]; then
	python manage_hikaridev.py test ${1}.SimpleTest.${2}
fi
