#!/usr/bin/env bash

URL_USER=$(echo "$(minikube service user --namespace=${K8S_NAMESPACE} --url)" | sed -e 's/http/https/')
URL_PROPHECY=$(echo "$(minikube service prophecy --namespace=${K8S_NAMESPACE} --url)" | sed -e 's/http/https/')
URL_STATS=$(echo "$(minikube service stats --namespace=${K8S_NAMESPACE} --url)" | sed -e 's/http/https/')
ADMIN_TOKEN=$(cat admin-token.txt)

# create bob
curl -X POST \
    --insecure \
    -v \
    -H "Authorization: Bearer ${ADMIN_TOKEN}" \
    -H "Content-Type: application/json" \
    -d '{ "name": "bob", "email": "bob@test.test" }' \
    "${URL_USER}/user"

# show info about bob
echo "bob's info"
curl --insecure -H "Authorization: Bearer ${ADMIN_TOKEN}" "${URL_USER}/user/bob" | jq

# issue a token for bob
BOB_TOKEN=$(curl -X POST \
    --insecure \
    -H "Authorization: Bearer ${ADMIN_TOKEN}" \
    -H "Content-Type: application/json" \
    -d '{ "userName": "bob", "roles": ["prophecy-creator", "user-reader"] }' \
    "${URL_USER}/token")
echo "raw token"
echo ${BOB_TOKEN}
echo "header"
echo ${BOB_TOKEN} | cut -d. -f 1 | base64 -d | jq
echo "payload"
echo ${BOB_TOKEN} | cut -d. -f 2 | base64 -d | jq
echo "signature"
echo ${BOB_TOKEN} | cut -d. -f 3

# bob creates a prophecy
FUTURE_DATE=`date -d "+15 days" '+%Y-%m-%dT%H:%M:%SZ'`
STOCK_VALUE=$((${RANDOM} / 100))
REQUEST_BODY=$(cat <<EOF
{
    "stockName": "GOOG",
    "stockExpectedValue": ${STOCK_VALUE},
    "prophecyType":"BULL",
    "expectedAt": "${FUTURE_DATE}"
}
EOF
)
curl -X POST \
    --insecure \
    -v \
    -H "Authorization: Bearer ${BOB_TOKEN}" \
    -H "Content-Type: application/json" \
    -d "${REQUEST_BODY}" \
    "${URL_PROPHECY}/prophecy"

# and then requests statistics about him
echo "bob's stats:"
curl --insecure -H "Authorization: Bearer ${BOB_TOKEN}" "${URL_STATS}/stats/show" | jq
