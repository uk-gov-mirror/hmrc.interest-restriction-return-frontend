#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyCTUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyCTUTR                        controllers.ReportingCompanyCTUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanyCTUTR                        controllers.ReportingCompanyCTUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanyCTUTR                  controllers.ReportingCompanyCTUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanyCTUTR                  controllers.ReportingCompanyCTUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyCTUTRPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyCTUTR.title = reportingCompanyCTUTR" >> ../conf/messages.en
echo "reportingCompanyCTUTR.heading = reportingCompanyCTUTR" >> ../conf/messages.en
echo "reportingCompanyCTUTR.checkYourAnswersLabel = reportingCompanyCTUTR" >> ../conf/messages.en
echo "reportingCompanyCTUTR.label = ReportingCompanyCTUTR" >> ../conf/messages.en
echo "reportingCompanyCTUTR.error.required = Enter reportingCompanyCTUTR" >> ../conf/messages.en
echo "reportingCompanyCTUTR.error.length = ReportingCompanyCTUTR must be 10 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyCTUTRPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.title = reportingCompanyCTUTR" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.heading = reportingCompanyCTUTR" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.checkYourAnswersLabel = reportingCompanyCTUTR" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.label = ReportingCompanyCTUTR" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.error.required = Enter reportingCompanyCTUTR" >> ../conf/messages.cy
echo "reportingCompanyCTUTR.error.length = ReportingCompanyCTUTR must be 10 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyCTUTRUserAnswersEntry: Arbitrary[(ReportingCompanyCTUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanyCTUTRPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyCTUTRPage: Arbitrary[ReportingCompanyCTUTRPage.type] =";\
    print "    Arbitrary(ReportingCompanyCTUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanyCTUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanyCTUTR: Option[SummaryListRow] = answer(ReportingCompanyCTUTRPage, routes.ReportingCompanyCTUTRController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object ReportingCompanyCTUTRTemplate extends WithName(\"reportingCompanyCTUTR.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReportingCompanyCTUTR completed"
