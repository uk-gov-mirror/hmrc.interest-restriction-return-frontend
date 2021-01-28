#!/bin/bash

echo ""
echo "Applying migration EnterANGIE"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/groupLevelInformation.routes
echo "### EnterANGIE Controller" >> ../conf/groupLevelInformation.routes
echo "### ----------------------------------------" >> ../conf/groupLevelInformation.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "EnterANGIE" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.groupLevelInformation.EnterANGIEController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName                          controllers.groupLevelInformation.EnterANGIEController.onSubmit(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "GET        /$kebabClassName/change                   controllers.groupLevelInformation.EnterANGIEController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName/change                   controllers.groupLevelInformation.EnterANGIEController.onSubmit(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# EnterANGIEPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "enterANGIE.title = EnterANGIE" >> ../conf/messages.en
echo "enterANGIE.heading = EnterANGIE" >> ../conf/messages.en
echo "enterANGIE.label= Enter the adjusted net group-interest expense (ANGIE)" >> ../conf/messages.en
echo "enterANGIE.checkYourAnswersLabel = EnterANGIE" >> ../conf/messages.en
echo "enterANGIE.error.nonNumeric = Enter your enterANGIE using numbers" >> ../conf/messages.en
echo "enterANGIE.error.required = Enter your enterANGIE" >> ../conf/messages.en
echo "enterANGIE.error.invalidNumeric = The enterANGIE must be valid decimal or whole number" >> ../conf/messages.en
echo "enterANGIE.error.outOfRange = EnterANGIE must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# EnterANGIEPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "enterANGIE.title = EnterANGIE" >> ../conf/messages.cy
echo "enterANGIE.heading = EnterANGIE" >> ../conf/messages.cy
echo "enterANGIE.label=Enter the adjusted net group-interest expense (ANGIE)" >> ../conf/messages.cy
echo "enterANGIE.checkYourAnswersLabel = EnterANGIE" >> ../conf/messages.cy
echo "enterANGIE.error.nonNumeric = Enter your enterANGIE using numbers" >> ../conf/messages.cy
echo "enterANGIE.error.required = Enter your enterANGIE" >> ../conf/messages.cy
echo "enterANGIE.error.invalidNumeric = The enterANGIE must be valid decimal or whole number" >> ../conf/messages.cy
echo "enterANGIE.error.outOfRange = EnterANGIE must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterANGIEUserAnswersEntry: Arbitrary[(EnterANGIEPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EnterANGIEPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterANGIEPage: Arbitrary[EnterANGIEPage.type] =";\
    print "    Arbitrary(EnterANGIEPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EnterANGIEPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def enterANGIE: Option[SummaryListRow] = answer(EnterANGIEPage, groupLevelInformationRoutes.EnterANGIEController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    EnterANGIEPage.toString -> EnterANGIEPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    EnterANGIEPage.toString -> EnterANGIEPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val enterANGIE = \"Enter the adjusted net group-interest expense (ANGIE)\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|groupLevelInformation\/${kebabClassName}|g" ../generated-it/controllers/groupLevelInformation/EnterANGIEControllerISpec.scala

echo "Migration EnterANGIE completed"
