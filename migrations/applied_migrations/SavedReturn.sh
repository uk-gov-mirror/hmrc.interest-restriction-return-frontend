#!/bin/bash

echo ""
echo "Applying migration SavedReturn"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /savedReturn                       controllers.SavedReturnController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# SavedReturnPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "savedReturn.title = savedReturn" >> ../conf/messages.en
echo "savedReturn.heading = savedReturn" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# SavedReturnPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "savedReturn.title = savedReturn" >> ../conf/messages.cy
echo "savedReturn.heading = savedReturn" >> ../conf/messages.cy

echo "Adding template to Nunjucks templates"
echo "object SavedReturnTemplate extends WithName(\"savedReturn.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration SavedReturn completed"
