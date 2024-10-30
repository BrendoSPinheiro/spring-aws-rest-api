#!/bin/bash

# Initialize localstack
docker-entrypoint.sh &

# Waiting for localstack to start
function check_dynamodb {
  aws dynamodb list-tables --endpoint-url=https://localhost.localstack.cloud:4566 --region us-east-1 > /dev/null 2>&1
}

while ! check_dynamodb; do
  echo "Waiting for DynamoDB to start"
  sleep 5
done

# Create DynamoDB table
echo "Creating DynamoDB table 'users'..."
aws dynamodb create-table --endpoint-url=https://localhost.localstack.cloud:4566 \
  --region us-east-1 \
  --table-name users \
  --attribute-definitions \
      AttributeName=id,AttributeType=S \
      AttributeName=email,AttributeType=S \
  --key-schema \
      AttributeName=id,KeyType=HASH \
  --provisioned-throughput \
      ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --global-secondary-indexes \
      "[
          {
              \"IndexName\": \"email-index\",
              \"KeySchema\": [
                  {\"AttributeName\": \"email\", \"KeyType\": \"HASH\"}
              ],
              \"Projection\": {
                  \"ProjectionType\": \"ALL\"
              },
              \"ProvisionedThroughput\": {
                  \"ReadCapacityUnits\": 5,
                  \"WriteCapacityUnits\": 5
              }
          }
      ]"

if [ $? -eq 0 ]; then
  echo "DynamoDB table 'users' was created successfully"
else
  echo "Failed to create DynamoDB table 'users'"
fi

tail -f /dev/null