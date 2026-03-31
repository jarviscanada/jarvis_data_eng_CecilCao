# Linux Cluster Monitoring Agent
(about 100-150 words)
Discuss the design of the project. What does this project/product do? Who are the users? What are the technologies you have used? (e.g. bash, docker, git, etc..)
The Linux Cluster Monitoring Agent is a simple cronjob that manages ...

# Quick Start
Be sure to navigate into the `linux_sql` folder before running these commands.
- Start a psql instance using psql_docker.sh
```
    ./scripts/psql_docker.sh start|stop|create [db_username] [db_password]
```
- Create the database:
```
--- Connect to the psql instance
psql -h localhost -U postgres -W

-- list all database
postgres=# \l

-- create a database
postgres=# CREATE DATABASE host_agent;
```
- Create tables using ddl.sql
```
    export PGPASSWORD=password
    psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```
- Insert hardware specs data into the DB using host_info.sh
```
    ./sql/ddl.sql start|stop|create [db_username] [db_password]
```
- Insert hardware usage data into the DB using host_usage.sh
```
    ./scripts/host_usage.sh localhost 5432 host_agent postgres password
```
- Crontab setup
```
    crontab -e
```
Update the path and place into the crontab to run `host_usage.sh` every 5 minutes:
```
* * * * * bash /path/to/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

# Implemenation
Discuss how you implement the project.

## Architecture
Draw a cluster diagram with three Linux hosts, a DB, and agents (use draw.io website). Image must be saved to the `assets` directory.

## Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh
- host_info.sh
- host_usage.sh
- crontab
- queries.sql (describe what business problem you are trying to resolve)

## Database Modeling
Describe the schema of each table using markdown table syntax (do not put any sql code)
- `host_info`
- `host_usage`

# Test
How did you test your bash scripts DDL? What was the result?

# Deployment
How did you deploy your app? (e.g. Github, crontab, docker)

# Improvements
Write at least three things you want to improve
e.g.
- handle hardware updates
- blah
- blah

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
