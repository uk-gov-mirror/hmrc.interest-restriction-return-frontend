#!/bin/bash

echo ""
echo "Applying migration Confirmation"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /confirmation                       controllers.ConfirmationController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ConfirmationPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "confirmation.title = confirmation" >> ../conf/messages.en
echo "confirmation.heading = confirmation" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ConfirmationPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "confirmation.title = confirmation" >> ../conf/messages.cy
echo "confirmation.heading = confirmation" >> ../conf/messages.cy

echo "Adding template to Nunjucks templates"
echo "object ConfirmationTemplate extends WithName(\"confirmation.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ConfirmationPage.toString -> ConfirmationPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration Confirmation completed"
