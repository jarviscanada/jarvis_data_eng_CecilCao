# This script collects server usage data and then inserts the data into the psql database.
# The script will be executed every minute using Linuxs crontab program.

# Assign CLI arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
  echo "Usage: ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password"
  exit 1
fi

# Data References
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Retrieve host hardware usage
memory_free=$(echo $vmstat_mb | tail -1 | awk '{print $27}')                            # Total memory free in Mb
cpu_idle=$(echo $vmstat_mb | tail -1 | awk '{print $38}')                               # % of time CPU is idle
cpu_kernel=$(echo $vmstat_mb | tail -1 | awk '{print $37}')                             # % of time CPU is running kernel processes
disk_io=$(echo $vmstat_mb | tail -1 | awk -v bi="32" -v bo="33" '{print $bi+$bo}')      # Total I/O operations on disks
disk_available=$(df -BM | egrep "^/dev/sda2" | awk '{gsub(/[a-zA-Z]/, ""); print $4}')  # Available Disk space on root.

# Retrieve auxillary database info
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')"
timestamp=$(vmstat -t | tail -1 | awk '{print $18 " " $19}')

# Add a new entry into the host_usage table.
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p $psql_port -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?