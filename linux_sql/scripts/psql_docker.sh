#!/bin/bash

# Setup arguments
cmd=$1
db_username=$2
db_password=$3

# Starting Docker
sudo systemctl status docker || sudo systemctl start docker

# Check container status
docker container inspect jrvs-psql
container_status=$?

# Handle create/stop/start commands
case $cmd in
  start|stop)
    # Check instance status; exit 1 if container has not been created
      if [ $container_status -eq 1 ]
      then
        exit 1
      fi

      # Start or stop the container
    	docker container $cmd jrvs-psql
    	exit $?
    ;;

  create)
    # Check if the container is already created
    if [ $container_status -eq 0 ]; then
      echo 'Container already exists'
      exit 1
    fi

    # Check # of CLI arguments
    if [ $# -ne 3 ]; then
      echo 'Create requires username and password'
      exit 1
    fi

    # Create the volume and container
    docker volume create pgdata
    docker run --name $db_username -e DB_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

    # Start the container
    docker container start $db_username
    exit $?
    ;;

  *)
    echo 'Illegal command'
    echo 'Commands: start|stop|create'
    exit 1
    ;;
esac