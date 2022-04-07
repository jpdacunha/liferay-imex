IMEX is a full OSGI importer / exporter tool designed to work with Liferay DXP. The following documentation introduces how it's work exactly.

A this moment IMEX is build using Liferay Workspace and gradle

# IMEX commands
imex:help : display all available commands

# How to create a new Exporter

In order to create a new exporter in imex please follow this steps

## Step 01 : Eclipse project setup

 - Create exporter project with blade

 cd <LIFERAY_WORKSPACE_HOME>/modules/imex/imex-exporter
 blade create -t service -p com.liferay.imex.site.exporter -s Exporter -c SiteExporter imex-site-exporter

 - In eclipse from the top level folder use gradle > refresh gradle project

## Step 02 : Resolve dependencies and compilation problems

 - Open project build.graddle and add the missing dependencies (Imex core dependencies) :
    - imex-core-api
    - imex-core-util

 Don't forget to save the file
 
 - Perform a new gradle refresh gradle > refresh gradle project
 
 - Resolve missing imports in Exporter class and add unimplemented methods (all compilation problems must be fixed)
 
## Step 03 : Check names

- Open bnd.bnd files and check bundle-symbolic name. Change it if needed.

- To be compliant copy / paste Bundle-Name and replace "-" with "." 
Example : imex-site-exporter => imex.site.exporter

## Step 04 : Add exporter properties

- Copy paste priorities from another exporter 
Example : 	property = {
			"imex.component.execution.priority=10",
			"imex.component.description=Site exporter",
			"service.ranking:Integer=10"
		},
		
## Step 05 : Deploy and check

- telnet localhost 11311
- imex:le

Imex show your new exporter in the list

# How to create an Exporter / Importer common project

Exporter / Importer projects are designed to share classes or methods across an importer and an exporter. 

## Step 01 : Eclipse project setup

 - Create exporter project with blade

cd <LIFERAY_WORKSPACE_HOME>/modules/imex/imex-exporter-importer-common
blade create -t api -p com.liferay.imex.site.model imex-site-common

 - In eclipse from the top level folder use gradle > refresh gradle project
 
 - Delete unused auto generated interface 
 
 - Rename package name to remove .api at the end
 
 - Delete all src/main/resources files
 
 
## Step 02 : Update BND

- Open bnd.bnd files and check bundle-symbolic name. Change it if needed.

- To be compliant copy / paste Bundle-Name and replace "-" with "." 
Example : imex-site-exporter => imex.site.exporter

- Remove lines starting with import-package

## Step 03 : Add dependencies 

- Open the corresponding exporter build.gradle
- Update the dependency to point the new project
- Repeat the same for the importer
 

# How to create a new Importer
In order to create a new importer in imex please follow this steps


# Release notes
## 1.3.0 Version
 - Raw ewport process feature added : export datas in a human readable format
 - Support for profiles : this features is available for Importers and Exporters 
 - Lock management to manage concurrent execution
 - Fully support for debug parameter in shell trigger
 
## 1.4.0 Version
 - Site importer : including support for UPGRADE_GROUP_ONLY import method. This method update the group object and it's dependencies without importing any LAR.
 - Merge feature for configuration files. This feature allow the user to update it's own configuration file without having to merge it by hand.
 
## 1.4.1 Version
 - Support for merging configuration files and comments preservation.
 - Adding Service Tracker for triggers. FilesSystme Service Tracker implémentation
 - Supports deploy (called by ST after registering the service) and undeploy <called by ST after uneregestring service) methods for importer, Exporters, Triggers.
 
## 1.4.2 Version  
 - Ability to update a wcddm template or structure by key in case of StructureDuplicateStructureKeyException or TemplateDuplicateTemplateKeyException exception