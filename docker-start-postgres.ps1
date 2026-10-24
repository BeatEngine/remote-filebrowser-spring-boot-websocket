$sqlpw = "123456";
docker run --name pg-filebrowser-demo -e "POSTGRES_PASSWORD=${sqlpw}" -p 5432:5432 -d postgres
# 2. Wait a few seconds for Postgres to fully boot up, then run the SQL:
Start-Sleep -Seconds 7
docker exec -i pg-filebrowser-demo psql -U postgres --% -c "CREATE DATABASE springboot_filebrowser;"