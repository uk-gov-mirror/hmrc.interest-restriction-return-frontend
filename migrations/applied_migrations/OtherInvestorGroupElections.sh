#!/bin/bash

echo ""
echo "Applying migration OtherInvestorGroupElections"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### OtherInvestorGroupElections Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "OtherInvestorGroupElections" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.OtherInvestorGroupElectionsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.OtherInvestorGroupElectionsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.OtherInvestorGroupElectionsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.OtherInvestorGroupElectionsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# OtherInvestorGroupElectionsPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "otherInvestorGroupElections.title = Which other elections apply to this investor group?" >> ../conf/messages.en
echo "otherInvestorGroupElections.heading = Which other elections apply to this investor group?" >> ../conf/messages.en
echo "otherInvestorGroupElections.groupRatioBlended = Group ratio (blended)" >> ../conf/messages.en
echo "otherInvestorGroupElections.groupEBITDA = Option 2" >> ../conf/messages.en
echo "otherInvestorGroupElections.checkYourAnswersLabel = Which other elections apply to this investor group?" >> ../conf/messages.en
echo "otherInvestorGroupElections.error.required = Select otherInvestorGroupElections" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# OtherInvestorGroupElectionsPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "otherInvestorGroupElections.title = Which other elections apply to this investor group?" >> ../conf/messages.cy
echo "otherInvestorGroupElections.heading = Which other elections apply to this investor group?" >> ../conf/messages.cy
echo "otherInvestorGroupElections.groupRatioBlended = Group ratio (blended)" >> ../conf/messages.cy
echo "otherInvestorGroupElections.groupEBITDA = Option 2" >> ../conf/messages.cy
echo "otherInvestorGroupElections.checkYourAnswersLabel = Which other elections apply to this investor group?" >> ../conf/messages.cy
echo "otherInvestorGroupElections.error.required = Select otherInvestorGroupElections" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOtherInvestorGroupElectionsUserAnswersEntry: Arbitrary[(OtherInvestorGroupElectionsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OtherInvestorGroupElectionsPage.type]";\
    print "        value <- arbitrary[OtherInvestorGroupElections].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOtherInvestorGroupElectionsPage: Arbitrary[OtherInvestorGroupElectionsPage.type] =";\
    print "    Arbitrary(OtherInvestorGroupElectionsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOtherInvestorGroupElections: Arbitrary[OtherInvestorGroupElections] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(OtherInvestorGroupElections.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OtherInvestorGroupElectionsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherInvestorGroupElections: Option[SummaryListRow] = answer(OtherInvestorGroupElectionsPage, electionsRoutes.OtherInvestorGroupElectionsController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    OtherInvestorGroupElectionsPage.toString -> OtherInvestorGroupElectionsPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    OtherInvestorGroupElectionsPage.toString -> OtherInvestorGroupElectionsPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val otherInvestorGroupElections = \"Which other elections apply to this investor group?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/OtherInvestorGroupElectionsControllerISpec.scala

echo "Migration OtherInvestorGroupElections completed"
