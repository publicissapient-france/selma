#!/bin/bash

env

mvn clean verify --settings target/travis/settings.xml
