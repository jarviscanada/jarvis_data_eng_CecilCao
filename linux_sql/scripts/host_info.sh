# This script collects hardware specification data and then inserts the data into the psql instance.
# Assume that hardware specifications are static, so the script will be executed only once.
# Assume that the CPU provides its refresh rate in GHz

# Assign CLI arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
  echo "Usage: ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password"
  exit 1
fi

# Parse host hardware specifications
lscpu_out=`lscpu`
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk -F: '{print $2}' | xargs)
cpu_mhz=$(echo "$cpu_model" | awk -F@ '{gsub(/[a-zA-Z]/, ""); print $2*1000}' | xargs)  # Assume cpu_model refresh rate is in GHz
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
timestamp=$(date +"%Y-%m-%d %H:%M:%S") # current timestamp in `YYYY-MM-DD HH:MM:SS`
total_mem=$(vmstat -s --unit M | egrep "total memory" | awk '{print $1}') # Store memory in megabytes

# Add a new entry into host_info table
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem) VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p $psql_port -d "$db_name" -U "$psql_user" -c "$insert_stmt"
exit $?