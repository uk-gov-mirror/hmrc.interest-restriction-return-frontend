#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyAppointed"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyAppointed                        controllers.aboutReturn.ReportingCompanyAppointedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanyAppointed                        controllers.aboutReturn.ReportingCompanyAppointedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanyAppointed                  controllers.aboutReturn.ReportingCompanyAppointedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanyAppointed                  controllers.aboutReturn.ReportingCompanyAppointedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyAppointedPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyAppointed.title = reportingCompanyAppointed" >> ../conf/messages.en
echo "reportingCompanyAppointed.heading = reportingCompanyAppointed" >> ../conf/messages.en
echo "reportingCompanyAppointed.checkYourAnswersLabel = reportingCompanyAppointed" >> ../conf/messages.en
echo "reportingCompanyAppointed.error.required = Select yes if reportingCompanyAppointed" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyAppointedPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyAppointed.title = reportingCompanyAppointed" >> ../conf/messages.cy
echo "reportingCompanyAppointed.heading = reportingCompanyAppointed" >> ../conf/messages.cy
echo "reportingCompanyAppointed.checkYourAnswersLabel = reportingCompanyAppointed" >> ../conf/messages.cy
echo "reportingCompanyAppointed.error.required = Select yes if reportingCompanyAppointed" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyAppointedUserAnswersEntry: Arbitrary[(ReportingCompanyAppointedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanyAppointedPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyAppointedPage: Arbitrary[ReportingCompanyAppointedPage.type] =";\
    print "    Arbitrary(ReportingCompanyAppointedPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanyAppointedPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanyAppointed: Option[SummaryListRow] = answer(ReportingCompanyAppointedPage, routes.ReportingCompanyAppointedController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ReportingCompanyAppointed completed"
