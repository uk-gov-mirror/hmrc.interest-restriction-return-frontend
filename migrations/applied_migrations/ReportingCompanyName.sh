#!/bin/bash

echo ""
echo "Applying migration ReportingCompanyName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanyName                        controllers.aboutReportingCompany.ReportingCompanyNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanyName                        controllers.aboutReportingCompany.ReportingCompanyNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanyName                  controllers.aboutReportingCompany.ReportingCompanyNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanyName                  controllers.aboutReportingCompany.ReportingCompanyNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanyNamePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanyName.title = reportingCompanyName" >> ../conf/messages.en
echo "reportingCompanyName.heading = reportingCompanyName" >> ../conf/messages.en
echo "reportingCompanyName.checkYourAnswersLabel = reportingCompanyName" >> ../conf/messages.en
echo "reportingCompanyName.label = ReportingCompanyName" >> ../conf/messages.en
echo "reportingCompanyName.error.required = Enter reportingCompanyName" >> ../conf/messages.en
echo "reportingCompanyName.error.length = ReportingCompanyName must be 160 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanyNamePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanyName.title = reportingCompanyName" >> ../conf/messages.cy
echo "reportingCompanyName.heading = reportingCompanyName" >> ../conf/messages.cy
echo "reportingCompanyName.checkYourAnswersLabel = reportingCompanyName" >> ../conf/messages.cy
echo "reportingCompanyName.label = ReportingCompanyName" >> ../conf/messages.cy
echo "reportingCompanyName.error.required = Enter reportingCompanyName" >> ../conf/messages.cy
echo "reportingCompanyName.error.length = ReportingCompanyName must be 160 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyNameUserAnswersEntry: Arbitrary[(ReportingCompanyNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanyNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanyNamePage: Arbitrary[ReportingCompanyNamePage.type] =";\
    print "    Arbitrary(ReportingCompanyNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanyNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanyName: Option[SummaryListRow] = answer(ReportingCompanyNamePage, routes.ReportingCompanyNameController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object ReportingCompanyNameTemplate extends WithName(\"reportingCompanyName.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReportingCompanyName completed"
