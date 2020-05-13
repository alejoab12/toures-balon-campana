# toures-balon-campana
Servicio que se encarga de la gestion de la campaña


Instalacion de servicio MINIO
$ docker pull minio/minio

# Run in detached mode with key/secret passed
$ docker run -p 9090:9000 --name minio -e MINIO_ACCESS_KEY=minioadmin -e MINIO_SECRET_KEY=minioadmin -d minio/minio server /data
