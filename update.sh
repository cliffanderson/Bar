#!/bin/bash
cd ~/IdeaProjects/Bar

mvn clean compile assembly:single
cp target/*.jar .

if [ -f export.sql ];
then
    rm export.sql
fi

mysqldump -u bar bar > export.sql
