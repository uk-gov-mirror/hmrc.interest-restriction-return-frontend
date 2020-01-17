#!/bin/bash

echo ""
echo "Applying migration UnderConstruction"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /underConstruction                       controllers.UnderConstructionController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# UnderConstructionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "underConstruction.title = underConstruction" >> ../conf/messages.en
echo "underConstruction.heading = underConstruction" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# UnderConstructionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "underConstruction.title = underConstruction" >> ../conf/messages.cy
echo "underConstruction.heading = underConstruction" >> ../conf/messages.cy

echo "Adding template to Nunjucks templates"
echo "object UnderConstructionTemplate extends WithName(\"underConstruction.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    UnderConstructionPage.toString -> UnderConstructionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration UnderConstruction completed"
