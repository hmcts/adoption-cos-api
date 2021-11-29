#!/usr/bin/env bash

scriptPath=$(dirname $(realpath $0))

# Roles used during the CCD import
${scriptPath}/add-ccd-role.sh "caseworker-adoption-courtadmin_beta"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-superuser"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-courtadmin-la"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-courtadmin"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-solicitor"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-pcqextractor"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-systemupdate"
${scriptPath}/add-ccd-role.sh "caseworker-adoption-bulkscan"
${scriptPath}/add-ccd-role.sh "caseworker-caa"
${scriptPath}/add-ccd-role.sh "citizen"
