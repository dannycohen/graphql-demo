

curl -s -X POST \
  -H "Content-Type: application/json" \
  -d '{"operationName":null,"variables":{},"query":"{\n  foods {\n    id\n    name\n  }\n}\n"}' \
  http://localhost:8080/graphql \
  | jq .



curl -s -X POST \
  -H "Content-Type: application/json" \
  -d '{"operationName":null,"variables":{},"query":"{\n  food(id: 1) {\n    id\n    name\n  }\n}\n"}' \
  http://localhost:8080/graphql \
  | jq .