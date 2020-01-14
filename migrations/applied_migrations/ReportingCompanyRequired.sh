#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyRequired"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyRequired                       controllers.ReportingCompanyRequiredController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyRequiredPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyRequired.title = reportingCompanyRequired" >> ../conf/messages.en
echo "reportingCompanyRequired.heading = reportingCompanyRequired" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyRequiredPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyRequired.title = reportingCompanyRequired" >> ../conf/messages.cy
echo "reportingCompanyRequired.heading = reportingCompanyRequired" >> ../conf/messages.cy

echo "Adding template to Nunjucks templates"
echo "object ReportingCompanyRequiredTemplate extends WithName(\"reportingCompanyRequired.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReportingCompanyRequired completed"