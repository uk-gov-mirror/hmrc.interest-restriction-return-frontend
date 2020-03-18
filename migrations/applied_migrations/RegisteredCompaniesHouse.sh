#!/bin/bash

echo ""
echo "Applying migration RegisteredCompaniesHouse"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /registeredCompaniesHouse                        controllers.ultimateParentCompany.RegisteredCompaniesHouseController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /registeredCompaniesHouse                        controllers.ultimateParentCompany.RegisteredCompaniesHouseController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRegisteredCompaniesHouse                  controllers.ultimateParentCompany.RegisteredCompaniesHouseController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRegisteredCompaniesHouse                  controllers.ultimateParentCompany.RegisteredCompaniesHouseController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# RegisteredCompaniesHousePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "registeredCompaniesHouse.title = RegisteredCompaniesHouse" >> ../conf/messages.en
echo "registeredCompaniesHouse.heading = RegisteredCompaniesHouse" >> ../conf/messages.en
echo "registeredCompaniesHouse.checkYourAnswersLabel = RegisteredCompaniesHouse" >> ../conf/messages.en
echo "registeredCompaniesHouse.error.required = Select yes if RegisteredCompaniesHouse" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# RegisteredCompaniesHousePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "registeredCompaniesHouse.title = RegisteredCompaniesHouse" >> ../conf/messages.cy
echo "registeredCompaniesHouse.heading = RegisteredCompaniesHouse" >> ../conf/messages.cy
echo "registeredCompaniesHouse.checkYourAnswersLabel = RegisteredCompaniesHouse" >> ../conf/messages.cy
echo "registeredCompaniesHouse.error.required = Select yes if RegisteredCompaniesHouse" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegisteredCompaniesHouseUserAnswersEntry: Arbitrary[(RegisteredCompaniesHousePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RegisteredCompaniesHousePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegisteredCompaniesHousePage: Arbitrary[RegisteredCompaniesHousePage.type] =";\
    print "    Arbitrary(RegisteredCompaniesHousePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RegisteredCompaniesHousePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def registeredCompaniesHouse: Option[SummaryListRow] = answer(RegisteredCompaniesHousePage, routes.RegisteredCompaniesHouseController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    RegisteredCompaniesHousePage.toString -> RegisteredCompaniesHousePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration RegisteredCompaniesHouse completed"
