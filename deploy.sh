#!/bin/bash

# In order to exit if any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo  an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

# COLORS MAPPING :
# Num  Colour    #define         R G B

# 0    black     COLOR_BLACK     0,0,0
# 1    red       COLOR_RED       1,0,0
# 2    green     COLOR_GREEN     0,1,0
# 3    yellow    COLOR_YELLOW    1,1,0
# 4    blue      COLOR_BLUE      0,0,1
# 5    magenta   COLOR_MAGENTA   1,0,1
# 6    cyan      COLOR_CYAN      0,1,1
# 7    white     COLOR_WHITE     1,1,1

BLUE=`tput setaf 4`
RED=`tput setaf 1`
NC=`tput sgr0` # No Color

liferay_home="/home/dev/liferay/liferay-compose-73/mount/files/"
# Separating deploy dir from LIFERAY_HOME because in docker context deploy dir can be outside LIFERAY_HOME
liferay_deploy_dir="/home/dev/liferay/liferay-compose-73/mount/deploy"
imex_home=$(pwd)
imex_modules_home="./modules/imex"


function restartALL() {

	if [[ ! -e $liferay_home ]]
	then
		echo -e  "${RED} Invalid path to LIFERAY_HOME : $liferay_home ${NC}"
		exit 1		
	fi

	echo -e  " "	
	echo -e  "${BLUE}# - Restarting modules from LIFERAY...${NC}"
	echo -e 
	cp -f $liferay_home/osgi/modules/imex*.jar $liferay_deploy_dir
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"

}

function deployALL() {

	deployJAVA "$@"
	deployREACT "$@"
	
}

function deployJAVA() {

	echo -e  " "	
	echo -e  "${BLUE}# - Cleaning JAVA modules ...${NC}"
	echo -e  " "
	executeJAVA clean
	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"

	echo -e  " "
	echo -e  "${BLUE}# - Deploying JAVA modules ...${NC}"
	echo -e  " "
	executeJAVA deploy
	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"
}

function buildJAVA() {

	echo -e  " "
	echo -e  "${BLUE}# - Cleaning JAVA modules ...${NC}"
	echo -e  " "
	executeJAVA clean
	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"

	echo -e  " "
	echo -e  "${BLUE}# - Building JAVA modules ...${NC}"
	echo -e  " "
	executeJAVA build
	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"
}

function buildREST() {

	echo -e  " "
	echo -e  "${BLUE}# - Building REST Service ...${NC}"
	echo -e  " "

	cd $imex_home/$imex_modules_home/imex-trigger/imex-rest-trigger/imex-rest-trigger-impl
	$imex_home/gradlew buildREST

	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"

}

function deployREACT() {

	echo -e  "${BLUE}# - Deploying REACT APP ...${NC}"
	echo -e  " "

	cd $imex_home/$imex_modules_home/imex-gui/imex-gui-widget
	npm run deploy:liferay

	echo -e  " "
	echo -e  "${BLUE}#${NC}"
	echo -e  "${BLUE}# Done.${NC}"

}

function executeJAVA() {

	if [ -z $1 ]
	then
		echo -e  "${RED} Missing required command (ex: deploy)${NC}"
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

	echo -e   "${BLUE}##"
	echo -e  "# Usage: deploy.sh"
	echo -e  "##"
	echo -e  " "	
	echo -e  " -- DEV FUNCTIONS -- "
	echo -e  "  deployALL             : Deploy All modules to Liferay"
	echo -e  "  buildAll              : Build All modules"
	echo -e  "  deployJAVA            : Deploy only JAVA modules"
	echo -e  "  buildJAVA             : Build JAVA modules"
	echo -e  "  deployREACT           : Deploy react app"
	echo -e  "  buildREST             : Build REST services"	
	echo -e  "  restartALL            : Restart all modules deployed in OSGI container"
	echo -e  " "
	echo -e  " ${NC}"

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
"restartALL")
	restartALL "$@"
	;;
*)
    manual
    ;;
esac