# Imex

Imex is a tool designed to import / export datas from Liferay portal. For further information please see documentation hosted in sub projects.

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

## 1.4.3 Version
 - Compatibility with DXP 7.4u22

## 1.4.5 Version
 - Implementing check all in IMEX GUI and passing selecxted importers and esporters to Rest API
 - Adding sort feature to /importers / exportes Rest API

## 1.4.6 Version
 - Simplification of version management using -consumer-policy and -provider-policy in bnd's

## 1.4.7 Version
 - Adding getSupportedProfiles endPoint

# 1.4.8 Version
 - Manage LAR export options per site.
 - Fixing bug in exporter / importer priority (not working as expected) 
