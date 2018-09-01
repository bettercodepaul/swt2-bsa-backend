#!/bin/sh

# $1 := SONAR CLOUD PROJECT KEY

quality_gate_result=$(curl -s 'https://sonarcloud.io/api/qualitygates/project_status?projectKey=$1' | python -mjson.tool)

if grep -q '"status": "ERROR"' ${quality_gate_result}; then
    echo "Quality gate failure"
    exit 1
else
    echo "Quality gate passed"
    exit 0
fi