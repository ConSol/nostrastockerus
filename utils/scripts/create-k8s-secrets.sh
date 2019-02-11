#!/usr/bin/env bash

pushd ${KEYSTORES_DIR}

for SERVICE_NAME in "${SERVICES[@]}"
do
    kubectl create secret generic ${SERVICE_NAME}-keystore \
        --from-file=${SERVICE_NAME}-keystore.pkcs12 \
        --from-literal=KEY_STORE_PASSWORD=${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
        --namespace=${K8S_NAMESPACE} \
        --dry-run \
        -o yaml \
            | kubectl apply -f -
    kubectl create secret generic ${SERVICE_NAME}-db \
        --from-literal=DB_NAME=${SERVICE_NAME} \
        --from-literal=DB_USER=${SERVICE_NAME} \
        --from-literal=DB_PASSWORD=ConSolSoFtWaReGmbH \
        --namespace=${K8S_NAMESPACE} \
        --dry-run \
        -o yaml \
            | kubectl apply -f -
done

kubectl create secret generic truststore \
    --from-file=truststore.pkcs12 \
    --from-literal=TRUST_STORE_PASSWORD=${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
    --namespace=${K8S_NAMESPACE} \
    --dry-run \
    -o yaml \
        | kubectl apply -f -

popd
