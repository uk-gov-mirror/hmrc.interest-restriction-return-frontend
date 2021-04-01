#!/bin/bash

echo ""
echo "Applying migration Declaration"

echo "" >> ../conf/reviewAndComplete.routes
echo "### Declaration Controller" >> ../conf/reviewAndComplete.routes
echo "### ----------------------------------------" >> ../conf/reviewAndComplete.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "Declaration" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                         controllers.reviewAndComplete.DeclarationController.onPageLoad()" >> ../conf/reviewAndComplete.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# DeclarationPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "declaration.title = declaration" >> ../conf/messages.en
echo "declaration.heading = declaration" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# DeclarationPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "declaration.title = declaration" >> ../conf/messages.cy
echo "declaration.heading = declaration" >> ../conf/messages.cy

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeclarationPage: Arbitrary[DeclarationPage.type] =";\
    print "    Arbitrary(DeclarationPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val declaration = \"Declaration\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    DeclarationPage.toString -> DeclarationPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    DeclarationPage.toString -> DeclarationPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|reviewAndComplete\/${kebabClassName}|g" ../generated-it/controllers/reviewAndComplete/DeclarationControllerISpec.scala

echo "Migration Declaration completed"
