docker-compose stop mongo
docker-compose -f docker-compose-mongo-not-master.yml up -d mongo

sleep 20

# wait a moment about 20 seconds
docker exec -it wallet-app_mongo_1 mongo local --eval "db.dropDatabase()"

docker-compose -f docker-compose-mongo-not-master.yml stop mongo

docker-compose up -d mongo

sleep 20

# wait a moment about 20 seconds
docker exec -it wallet-app_mongo_1 mongo --eval "rs.initiate()"
