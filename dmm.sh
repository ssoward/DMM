ssh ssoward@70.58.38.250 cp /home/ssoward/tools/wars/DMM.war /home/ssoward/tools/wars/DMM.$1
ant -propertyfile dmm.properties
scp dist/DMM.war ssoward@70.58.38.250:/home/ssoward/tools/wars/
ant remove -propertyfile dmm.properties
ant install -propertyfile dmm.properties

