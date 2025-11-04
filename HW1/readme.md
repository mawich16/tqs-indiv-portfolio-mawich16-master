# To Run local

### Inside the /BE/hw1 directory

```bash
docker run --name postgresdb \
    -e POSTGRES_USER=admin \
    -e POSTGRES_PASSWORD=secret \
    -e POSTGRES_DB=meals_db \
    -p 5432:5432 \
    -d postgres:latest

mvn spring-boot:run
```

### Inside the /FE/bookings directory

```bash
npm install
npm run dev
```
