--- This script assumes host_agent is manually created. The script creates two separate tables: host_info and host_usage
--- into the database host_agent.
--- You can run this script with the command: psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

-- Create the database
-- IF NOT EXISTS(SELECT name FROM sys.databases WHERE name = 'host_agent')
-- BEGIN
--         CREATE DATABASE host_agent;
-- END

--- Connect to the database
\c host_agent;

-- Create a host_info table
CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
    id SERIAL NOT NULL,
    hostname VARCHAR NOT NULL,
    cpu_number INT2 NOT NULL,
    cpu_architecture VARCHAR NOT NULL,
    cpu_model VARCHAR NOT NULL,
    cpu_mhz FLOAT8 NOT NULL,
    l2_cache INT4 NOT NULL,
    "timestamp" TIMESTAMP NULL,
    total_mem INT4 NULL,
    CONSTRAINT host_info_pk PRIMARY KEY (id),
    CONSTRAINT host_info_un UNIQUE (hostname)
);

-- Create a host_info table
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage (
    "timestamp" TIMESTAMP NOT NULL,
    host_id SERIAL NOT NULL,
    memory_free INT4 NOT NULL,
    cpu_idle INT2 NOT NULL,
    cpu_kernel INT2 NOT NULL,
    disk_io INT4 NOT NULL,
    disk_available INT4 NOT NULL,
    CONSTRAINT host_usage_host_info_fk FOREIGN KEY (host_id) REFERENCES host_info(id)
);
