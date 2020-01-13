#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyResult"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyResult                        controllers.ReportingCompanyResultController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanyResult                        controllers.ReportingCompanyResultController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanyResult                  controllers.ReportingCompanyResultController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanyResult                  controllers.ReportingCompanyResultController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyResultPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyResult.title = reportingCompanyResult" >> ../conf/messages.en
echo "reportingCompanyResult.heading = reportingCompanyResult" >> ../conf/messages.en
echo "reportingCompanyResult.checkYourAnswersLabel = reportingCompanyResult" >> ../conf/messages.en
echo "reportingCompanyResult.error.required = Select yes if reportingCompanyResult" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyResultPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyResult.title = reportingCompanyResult" >> ../conf/messages.cy
echo "reportingCompanyResult.heading = reportingCompanyResult" >> ../conf/messages.cy
echo "reportingCompanyResult.checkYourAnswersLabel = reportingCompanyResult" >> ../conf/messages.cy
echo "reportingCompanyResult.error.required = Select yes if reportingCompanyResult" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyResultUserAnswersEntry: Arbitrary[(ReportingCompanyResultPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanyResultPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyResultPage: Arbitrary[ReportingCompanyResultPage.type] =";\
    print "    Arbitrary(ReportingCompanyResultPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanyResultPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanyResult: Option[SummaryListRow] = answer(ReportingCompanyResultPage, routes.ReportingCompanyResultController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object ReportingCompanyResultTemplate extends WithName(\"reportingCompanyResult.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReportingCompanyResult completed"
