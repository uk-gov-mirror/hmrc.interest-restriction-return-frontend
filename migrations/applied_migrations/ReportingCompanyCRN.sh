#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyCRN"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyCRN                        controllers.aboutReportingCompany.ReportingCompanyCRNController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanyCRN                        controllers.aboutReportingCompany.ReportingCompanyCRNController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanyCRN                  controllers.aboutReportingCompany.ReportingCompanyCRNController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanyCRN                  controllers.aboutReportingCompany.ReportingCompanyCRNController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyCRNPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyCRN.title = reportingCompanyCRN" >> ../conf/messages.en
echo "reportingCompanyCRN.heading = reportingCompanyCRN" >> ../conf/messages.en
echo "reportingCompanyCRN.checkYourAnswersLabel = reportingCompanyCRN" >> ../conf/messages.en
echo "reportingCompanyCRN.label = ReportingCompanyCRN" >> ../conf/messages.en
echo "reportingCompanyCRN.error.required = Enter reportingCompanyCRN" >> ../conf/messages.en
echo "reportingCompanyCRN.error.length = ReportingCompanyCRN must be 8 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyCRNPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyCRN.title = reportingCompanyCRN" >> ../conf/messages.cy
echo "reportingCompanyCRN.heading = reportingCompanyCRN" >> ../conf/messages.cy
echo "reportingCompanyCRN.checkYourAnswersLabel = reportingCompanyCRN" >> ../conf/messages.cy
echo "reportingCompanyCRN.label = ReportingCompanyCRN" >> ../conf/messages.cy
echo "reportingCompanyCRN.error.required = Enter reportingCompanyCRN" >> ../conf/messages.cy
echo "reportingCompanyCRN.error.length = ReportingCompanyCRN must be 8 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyCRNUserAnswersEntry: Arbitrary[(ReportingCompanyCRNPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanyCRNPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyCRNPage: Arbitrary[ReportingCompanyCRNPage.type] =";\
    print "    Arbitrary(ReportingCompanyCRNPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanyCRNPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanyCRN: Option[SummaryListRow] = answer(ReportingCompanyCRNPage, routes.ReportingCompanyCRNController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object ReportingCompanyCRNTemplate extends WithName(\"reportingCompanyCRN.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReportingCompanyCRN completed"
