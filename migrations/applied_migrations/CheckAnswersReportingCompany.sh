#!/bin/bash

echo ""
echo "Applying migration CheckAnswersReportingCompany"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /checkAnswersReportingCompany                        controllers.aboutReturn.CheckAnswersAboutReturnController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /checkAnswersReportingCompany                        controllers.aboutReturn.CheckAnswersAboutReturnController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeCheckAnswersReportingCompany                  controllers.aboutReturn.CheckAnswersAboutReturnController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeCheckAnswersReportingCompany                  controllers.aboutReturn.CheckAnswersAboutReturnController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# CheckAnswersReportingCompanyPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "checkAnswersReportingCompany.title = checkAnswersReportingCompany" >> ../conf/messages.en
echo "checkAnswersReportingCompany.heading = checkAnswersReportingCompany" >> ../conf/messages.en
echo "checkAnswersReportingCompany.checkYourAnswersLabel = checkAnswersReportingCompany" >> ../conf/messages.en
echo "checkAnswersReportingCompany.label = CheckAnswersReportingCompany" >> ../conf/messages.en
echo "checkAnswersReportingCompany.error.required = Enter checkAnswersReportingCompany" >> ../conf/messages.en
echo "checkAnswersReportingCompany.error.length = CheckAnswersReportingCompany must be Check your answers for reporting company section characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# CheckAnswersReportingCompanyPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.title = checkAnswersReportingCompany" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.heading = checkAnswersReportingCompany" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.checkYourAnswersLabel = checkAnswersReportingCompany" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.label = CheckAnswersReportingCompany" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.error.required = Enter checkAnswersReportingCompany" >> ../conf/messages.cy
echo "checkAnswersReportingCompany.error.length = CheckAnswersReportingCompany must be Check your answers for reporting company section characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCheckAnswersReportingCompanyUserAnswersEntry: Arbitrary[(CheckAnswersReportingCompanyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CheckAnswersReportingCompanyPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCheckAnswersReportingCompanyPage: Arbitrary[CheckAnswersReportingCompanyPage.type] =";\
    print "    Arbitrary(CheckAnswersReportingCompanyPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CheckAnswersReportingCompanyPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def checkAnswersReportingCompany: Option[SummaryListRow] = answer(CheckAnswersReportingCompanyPage, routes.CheckAnswersAboutReturnController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration CheckAnswersReportingCompany completed"
