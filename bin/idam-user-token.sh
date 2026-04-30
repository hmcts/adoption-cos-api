#!/usr/bin/env bash

BASEDIR=$(realpath $(dirname ${0})/../../)

if [ -f $BASEDIR/.env ]
then
  export $(cat $BASEDIR/.env | sed 's/#.*//g' | xargs)
fi

set -e

username=${1}
password=${2}

IDAM_URL=${IDAM_API_URL_BASE:-http://localhost:5000}
CLIENT_ID=${CLIENT_ID:-adoption-cos-api}
clientSecret=${COS_API_OAUTH2_CLIENT_SECRET}
redirectUri=http://localhost:3000/receiver

curl --silent --location --show-error "${IDAM_URL}/o/token" \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_ID" \
--data-urlencode "client_secret=$clientSecret" \
--data-urlencode "redirect_uri=$redirectUri" \
--data-urlencode "username=$username" \
--data-urlencode "password=$password" \
--data-urlencode 'scope=openid profile roles' \
--data-urlencode 'grant_type=password' \
| jq -r .access_token
