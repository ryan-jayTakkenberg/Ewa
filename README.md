
# Green Left

Green Left is a website that manages the inventory, orders, warehouses, projects and teams of Solar Serum.


## Color Reference
**TODO:** UPDATE THIS TO THE CORRECT COLORS !!!! <!> <!>

| Color            | Hex                                                              |
|------------------|------------------------------------------------------------------|
| Primary Color    | ![primary](https://via.placeholder.com/10/c5ce2c?text=+) #C5CE2C |



## Run Locally

Clone the project

```bash
git clone https://gitlab.fdmci.hva.nl/se-ewa/2023-2024-1/solar-1.git
```

### Frontend

Go to the project frontend directory

```bash
cd %PROJECT-PATH%
cd frontend
```

Install dependencies (requires [npm](https://nodejs.org/en/download))

```bash
npm install
```

Start the frontend server

```bash
npm run serve
```

Additionally, you can build the frontend

```bash
npm run build
```

### Backend

Go to the project backend directory and install as a maven project

```bash
cd %PROJECT-PATH%
cd backend/esserver
```

Build backend server jar file (requires [maven](https://maven.apache.org/download.cgi))

```bash
mvn install
```

Run the docker script (requires [docker](https://docs.docker.com/desktop/install/windows-install/))

```bash
docker build -t green-left-image .
docker run -d --name green-left-container green-left-image
```
