#! /bin/sh
### BEGIN INIT INFO
# Provides:          FastCGI servers for Django
# Required-Start:    networking
# Required-Stop:     networking
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start FastCGI servers with Django.
# Description:       Django, in order to operate with FastCGI, must be started
#                    in a very specific way with manage.py. This must be done
#                    for each DJango web server that has to run.
### END INIT INFO
#
#

#### SERVER SPECIFIC CONFIGURATION
PROJECT_ROOT=/opt/AriadneMaze
RUNFILES_PATH=$PROJECT_ROOT
HOST=127.0.0.1
PORT=8080
RUN_AS=ariadnemaze
FCGI_METHOD=threaded
APP_NAME=AriadneMaze
#### DO NOT CHANGE ANYTHING AFTER THIS LINE!

set -e

PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin
DESC="AriadneMaze Django"
NAME=$0
SCRIPTNAME=/etc/init.d/$NAME

#
#       Function that starts the daemon/service.
#
d_start()
{
    if [ -f $RUNFILES_PATH/${APP_NAME}.pid ]; then
        echo -n " already running"
    else
        start-stop-daemon --start --quiet \
                   --pidfile $RUNFILES_PATH/${APP_NAME}.pid \
                   --chuid $RUN_AS --exec /usr/bin/env -- python \
                   $PROJECT_ROOT/manage.py runfcgi \
                   method=$FCGI_METHOD \
                   host=$HOST port=$PORT pidfile=$RUNFILES_PATH/${APP_NAME}.pid
        chmod 400 $RUNFILES_PATH/${APP_NAME}.pid
    fi
}

#
#       Function that stops the daemon/service.
#
d_stop() {
    start-stop-daemon --stop --quiet --pidfile $RUNFILES_PATH/${APP_NAME}.pid \
                      || echo -n " not running"
    if [ -f $RUNFILES_PATH/${APP_NAME}.pid ]; then
       rm -f $RUNFILES_PATH/${APP_NAME}.pid
    fi
}

ACTION="$1"
case "$ACTION" in
    start)
        echo -n "Starting $DESC: $NAME"
        d_start
        echo "."
        ;;

    stop)
        echo -n "Stopping $DESC: $NAME"
        d_stop
        echo "."
        ;;

    restart|force-reload)
        echo -n "Restarting $DESC: $NAME"
        d_stop
        sleep 1
        d_start
        echo "."
        ;;

    *)
        echo "Usage: $NAME {start|stop|restart|force-reload}" >&2
        exit 3
        ;;
esac

exit 0
