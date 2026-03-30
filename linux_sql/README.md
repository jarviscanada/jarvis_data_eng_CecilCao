# Linux Cluster Monitoring Agent
This project is under development. Since this project follows the GitFlow, the final work will be merged to the main branch after Team Code Team.

# Script usage
`./scripts/psql_docker.sh start|stop|create [db_username] [db_password]`

# Examples
## Create a psql docker container with the given username and password.
## Print error message if username or password is not given
## Print error message if the container is already created
`./scripts/psql_docker.sh create db_username db_password`

## Start the stopped psql docker container
## Print error message if the container is not created
`./scripts/psql_docker.sh start`

## Stop the running psql docker container
## Print error message if the container is not created
`./scripts/psql_docker.sh stop`

# Contributions
This project is developed by Cecil Cao, with the help of resources from Jarvis.
