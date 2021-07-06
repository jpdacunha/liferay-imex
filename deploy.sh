#!/bin/bash

# In order to exit if any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

imex_home=$(pwd)
imex_modules_home="./modules/imex"


function deployALL() {

	deployJAVA "$@"
	deployREACT "$@"
	
}

function deployJAVA() {

	executeJAVA deploy

}

function buildJAVA() {

	executeJAVA build

}

function buildREST() {

	echo "# - Building REST Service ..."
	echo " "

	cd $imex_home/$imex_modules_home/imex-trigger/imex-rest-trigger/imex-rest-trigger-impl
	$imex_home/gradlew buildREST

	echo " "
	echo "#"
	echo "# Done."

}

function deployREACT() {

	echo "# - Deploying REACT APP ..."
	echo " "

	cd $imex_home/$imex_modules_home/imex-gui/imex-gui-widget
	npm run deploy:liferay

	echo " "
	echo "#"
	echo "# Done."

}

function executeJAVA() {

	if [ -z $1 ]
	then
		echo " Missing required command (ex: deploy)"
		exit 1
	fi

	cd $imex_home/$imex_modules_home/imex-core
	$imex_home/gradlew $1

	cd $imex_home/$imex_modules_home/imex-exporter
	$imex_home/gradlew $1

	cd $imex_home/$imex_modules_home/imex-exporter-importer-common
	$imex_home/gradlew $1

	cd $imex_home/$imex_modules_home/imex-importer
	$imex_home/gradlew $1

	cd $imex_home/$imex_modules_home/imex-processor
	$imex_home/gradlew $1

	cd $imex_home/$imex_modules_home/imex-trigger
	$imex_home/gradlew $1
	
}

function manual() {

	echo "##"
	echo "# Usage: deploy.sh"
	echo "##"
	echo " "	
	echo " -- DEV FUNCTIONS -- "
	echo "  deployALL             : Deploy All modules to Liferay"
	echo "  buildAll              : Build All modules"
	echo "  deployJAVA            : Deploy only JAVA modules"
	echo "  buildJAVA             : Build JAVA modules"
	echo "  deployREACT           : Deploy react app"
	echo "  buildREST             : Build REST services"	
	echo " "
	echo " "

}

if [ $# -eq 0 ]; then
    manual
    exit 0
fi

case "$1" in
"deployALL")
	deployALL "$@"
	;;
"buildAll")
	buildAll "$@"
	;;
"deployJAVA")
	deployJAVA "$@"
	;;
"deployREACT")
	deployREACT "$@"
	;; 
"buildREST")
	buildREST "$@"
	;;
"buildJAVA")
	buildJAVA "$@"
	;;
*)
    manual
    ;;
esac