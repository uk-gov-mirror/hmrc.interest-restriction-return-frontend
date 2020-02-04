#!/bin/bash

echo ""
echo "Applying migration ReportingCompanySameAsParent"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /reportingCompanySameAsParent                        controllers.groupStructure.ReportingCompanySameAsParentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /reportingCompanySameAsParent                        controllers.groupStructure.ReportingCompanySameAsParentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReportingCompanySameAsParent                  controllers.groupStructure.ReportingCompanySameAsParentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReportingCompanySameAsParent                  controllers.groupStructure.ReportingCompanySameAsParentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReportingCompanySameAsParentPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reportingCompanySameAsParent.title = Is Name also the parent company?" >> ../conf/messages.en
echo "reportingCompanySameAsParent.heading = Is Name also the parent company?" >> ../conf/messages.en
echo "reportingCompanySameAsParent.checkYourAnswersLabel =Is Name also the parent company?" >> ../conf/messages.en
echo "reportingCompanySameAsParent.error.required = Select yes if Name is also the parent company" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReportingCompanySameAsParentPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reportingCompanySameAsParent.title = Is Name also the parent company?" >> ../conf/messages.cy
echo "reportingCompanySameAsParent.heading = Is Name also the parent company?" >> ../conf/messages.cy
echo "reportingCompanySameAsParent.checkYourAnswersLabel = Is Name also the parent company?" >> ../conf/messages.cy
echo "reportingCompanySameAsParent.error.required = Select yes if Name is also the parent company" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanySameAsParentUserAnswersEntry: Arbitrary[(ReportingCompanySameAsParentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReportingCompanySameAsParentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReportingCompanySameAsParentPage: Arbitrary[ReportingCompanySameAsParentPage.type] =";\
    print "    Arbitrary(ReportingCompanySameAsParentPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReportingCompanySameAsParentPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reportingCompanySameAsParent: Option[SummaryListRow] = answer(ReportingCompanySameAsParentPage, routes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ReportingCompanySameAsParentPage.toString -> ReportingCompanySameAsParentPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration ReportingCompanySameAsParent completed"
