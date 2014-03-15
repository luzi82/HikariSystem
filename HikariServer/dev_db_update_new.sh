#!/bin/bash

# $1 : app name

if [ "$#" = "0" ]; then
	echo $0 [app_name]
	exit
fi

./manage_hikaridev.py schemamigration ${1} --auto
