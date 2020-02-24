#!/bin/bash

echo ""
echo "Applying migration AboutAddingUKCompanies"

echo "" >> ../conf/ukCompanies.routes
echo "### AboutAddingUKCompanies Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "AboutAddingUKCompanies" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                         controllers.ukCompanies.AboutAddingUKCompaniesController.onPageLoad()" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# AboutAddingUKCompaniesPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "aboutAddingUKCompanies.title = aboutAddingUKCompanies" >> ../conf/messages.en
echo "aboutAddingUKCompanies.heading = aboutAddingUKCompanies" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# AboutAddingUKCompaniesPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "aboutAddingUKCompanies.title = aboutAddingUKCompanies" >> ../conf/messages.cy
echo "aboutAddingUKCompanies.heading = aboutAddingUKCompanies" >> ../conf/messages.cy

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val aboutAddingUKCompanies = \"In this section you will need to tell us about eligible UK companies in the group\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/AboutAddingUKCompaniesControllerISpec.scala

echo "Migration AboutAddingUKCompanies completed"
