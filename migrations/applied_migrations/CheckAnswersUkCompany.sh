#!/bin/bash

echo ""
echo "Applying migration CheckAnswersUkCompany"

echo "" >> ../conf/ukCompanies.routes
echo "### CheckAnswersUkCompany Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CheckAnswersUkCompany" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                         controllers.ukCompanies.CheckAnswersUkCompanyController.onPageLoad()" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CheckAnswersUkCompanyPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "checkAnswersUkCompany.title = checkAnswersUkCompany" >> ../conf/messages.en
echo "checkAnswersUkCompany.heading = checkAnswersUkCompany" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CheckAnswersUkCompanyPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "checkAnswersUkCompany.title = checkAnswersUkCompany" >> ../conf/messages.cy
echo "checkAnswersUkCompany.heading = checkAnswersUkCompany" >> ../conf/messages.cy

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCheckAnswersUkCompanyPage: Arbitrary[CheckAnswersUkCompanyPage.type] =";\
    print "    Arbitrary(CheckAnswersUkCompanyPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val checkAnswersUkCompany = \"Check company details\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CheckAnswersUkCompanyPage.toString -> CheckAnswersUkCompanyPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    CheckAnswersUkCompanyPage.toString -> CheckAnswersUkCompanyPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/CheckAnswersUkCompanyControllerISpec.scala

echo "Migration CheckAnswersUkCompany completed"
