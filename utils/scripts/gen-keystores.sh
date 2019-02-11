#!/usr/bin/env bash

function create_key_pair_and_cert {
	KEY_NAME=$1
	KEY_STORE_NAME=$2
	CERT_NAME=$3
	keytool -genkeypair \
		-alias ${KEY_NAME} \
		-dname "CN=*,OU=Dev,O=ConSol Software GmbH,L=Duesseldorf,S=Nordrhein-Westfalen,C=DE" \
		-validity 180 \
		-keyalg RSA \
		-keysize 4096 \
		-keypass ${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
		-storepass ${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
		-storetype PKCS12 \
		-keystore ${KEY_STORE_NAME}.pkcs12
	keytool -exportcert \
		-alias ${KEY_NAME} \
		-file ${CERT_NAME}.crt \
		-storetype PKCS12 \
		-storepass ${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
		-keystore ${KEY_STORE_NAME}.pkcs12
}

function import_cert_into_keystore {
	CERT_FILE=$1
	KEY_STORE_FILE=$2
	KEY_NAME_INSIDE_KEY_STORE=$3
	keytool -importcert \
		-file ${CERT_FILE} \
		-keystore ${KEY_STORE_FILE} \
		-alias ${KEY_NAME_INSIDE_KEY_STORE} \
		-storetype PKCS12 \
		-storepass ${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
		-noprompt \
		-trustcacerts
}

rm -rf ${KEYSTORES_DIR}
mkdir -p ${KEYSTORES_DIR}
pushd ${KEYSTORES_DIR}

echo "generating for each service a private key and a cert"
for SERVICE_NAME in "${SERVICES[@]}"
do
    create_key_pair_and_cert ${SERVICE_NAME}-private-key ${SERVICE_NAME}-keystore ${SERVICE_NAME}-public-key
done

echo "bundling all certs into a trust store"
for CERT_FILE in $(ls *.crt)
do
    KEY_NAME=$(echo ${CERT_FILE} | cut -d'.' -f 1)
    import_cert_into_keystore ${CERT_FILE} truststore.pkcs12 ${KEY_NAME}
done
# add COMODO RSA CA root cert
import_cert_into_keystore ../certs/comodo-rsa-ca.crt truststore.pkcs12 comodorsaca

touch log.txt
echo "listing the contents of key stores (see log.txt)"
for KEY_STORE_FILE in $(ls *.pkcs12)
do
    echo "### ${KEY_STORE_FILE} ###" >>log.txt
    keytool -list \
        -v \
        -keystore ${KEY_STORE_FILE} \
        -storetype PKCS12 \
		-storepass ${PASSWORD_FOR_KEYS_AND_KEY_STORES} \
        >>log.txt
    echo "" >>log.txt
done

echo "done"
popd
