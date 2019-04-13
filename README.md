# Handschriftenportal Index Update Service
<sub><sub>Maintainer: Konrad Eichstädt</sub></sub>  
<br/>
<br/>
**Inhalt:**  
[1. Einführung und Ziele ](#1-einf%C3%BChrung-und-ziele)<br/>
[1.1 Aufgabenstellung](#11-aufgabenstellung)<br/>
[1.2 Qualitätsziele ](#12-qualit%C3%A4tsziele)<br/>
[1.3 Projektbeteiligte ](#13-projektbeteiligte-stakeholder)        
[2. Randbedingungen ](#2-randbedingungen)<br/>
[3. Kontextabgrenzung](#3-kontextabgrenzung)<br/>
[4. Lösungsstrategie](#4-l%C3%B6sungsstrategie)<br/>
[5. Baustein](#5-bausteinsicht)<br/>
[6. Laufzeitsicht](#6-laufzeitsicht)<br/>
[7. Verteilungssicht](#7-verteilungssicht)<br/>
[8. Betrieb und Wiederherstellung](#8-betrieb-und-wiederherstellung)<br/>
[8.1 Ansprechpartner , Service Level](#81-ansprechpartner-service-level)<br/>
[8.2 Installation / Konfiguration](#82-installation-konfiguration)<br/>
[9. Entwurfsentscheidungen](#9-entwurfsentscheidungen)<br/>
[10. Qualitätsziele](#10-qualit%C3%A4tsziele)<br/>
[11. Risiken und technische Schulden](#11-risiken-und-technische-schulden)<br/>
[12. Glossar](#12-glossar)<br/>
[13. Release Notes](#13-release-notes)<br/>

# 1. Einführung und Ziele #
  
## 1.1 Aufgabenstellung ##
  
## 1.2 Qualitätsziele ##
Als nichtfunktionale Anforderung ist eine hohe Wartbarkeit zu erreichen. Ein Administrator möchte die Installation automatisiert durchführen.   
## 1.3 Projektbeteiligte (Stakeholder) ##
 * Administratoren
 * Entwickler 
# 2. Randbedingungen
Organisatorische und technische Randbedingungen. 
# 3. Kontextabgrenzung
Sicht aus der Vogelperspektive. Zeigt das System als Blackbox und den Zusammenhang zu Nachbarsystemen. 
# 4. Lösungsstrategie
Um ein Debian Packet erstellen zu können wird Maven verwendet. Zur Erzeugung des Debian Packetes wird das Maven Plugin jDeb verwendet und konfiguriert. 
# 5. Bausteinsicht
Statische Zerlegung des Systems in Bausteine.  
# 6. Laufzeitsicht
Zeigt das Zusammenspiel der Architekturbausteine zur Laufzeit. 
# 7. Verteilungssicht
Auf welchen Systemen laufen die Systemkomponenten. 
# 8. Betrieb und Wiederherstellung #
## 8.1 Ansprechpartner , Service Level
Verwantwortlich für die Pflege ist IDM 2.2.  
## 8.2 Installation / Konfiguration ##
To start a Solr cluster and create the "hsp" collection:
```bash
$ docker-compose up
$ docker exec solr1 /opt/solr/bin/solr create_collection -c hsp
```
## 8.3 Wiederherstellung ##
# 9. Entwurfsentscheidungen
Wichtige Architekturentscheidungen und Gründe. 
# 10. Qualitätsziele
Szenarien konkretisierte Qualitätsanforderungen. 
# 11. Risiken und technische Schulden
# 12. Glossar
Fachliches Glossar. 
# 13. Release Notes
| Komponente        | letzte Änderung           | Version  |
| ------------- |:-------------:| -----:|
