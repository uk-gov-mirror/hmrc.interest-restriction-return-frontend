#!/bin/bash

echo ""
echo "Applying migration DerivedCompany"

echo "" >> ../conf/derivedCompany.routes
echo "### DerivedCompany Controller" >> ../conf/derivedCompany.routes
echo "### ----------------------------------------" >> ../conf/derivedCompany.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "DerivedCompany" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                         controllers.derivedCompany.DerivedCompanyController.onPageLoad()" >> ../conf/derivedCompany.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# DerivedCompanyPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "derivedCompany.title = derivedCompany" >> ../conf/messages.en
echo "derivedCompany.heading = derivedCompany" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# DerivedCompanyPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "derivedCompany.title = derivedCompany" >> ../conf/messages.cy
echo "derivedCompany.heading = derivedCompany" >> ../conf/messages.cy

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val derivedCompany = \"DerivedCompany\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|derivedCompany\/${kebabClassName}|g" ../generated-it/controllers/derivedCompany/DerivedCompanyControllerISpec.scala

echo "Migration DerivedCompany completed"
