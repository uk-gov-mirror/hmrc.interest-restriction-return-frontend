#!/bin/bash

echo ""
echo "Applying migration InvestorRatioMethod"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### InvestorRatioMethod Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "InvestorRatioMethod" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.InvestorRatioMethodController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.InvestorRatioMethodController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.InvestorRatioMethodController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.InvestorRatioMethodController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# InvestorRatioMethodPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "investorRatioMethod.title = Which ratio do you want to elect for this investor group?" >> ../conf/messages.en
echo "investorRatioMethod.heading = Which ratio do you want to elect for this investor group?" >> ../conf/messages.en
echo "investorRatioMethod.value = Group Ratio" >> ../conf/messages.en
echo "investorRatioMethod.value-2 = Fixed Ratio" >> ../conf/messages.en
echo "investorRatioMethod.checkYourAnswersLabel = Which ratio do you want to elect for this investor group?" >> ../conf/messages.en
echo "investorRatioMethod.error.required = Select investorRatioMethod" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# InvestorRatioMethodPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "investorRatioMethod.title = Which ratio do you want to elect for this investor group?" >> ../conf/messages.cy
echo "investorRatioMethod.heading = Which ratio do you want to elect for this investor group?" >> ../conf/messages.cy
echo "investorRatioMethod.value = Group Ratio" >> ../conf/messages.cy
echo "investorRatioMethod.value-2 = Fixed Ratio" >> ../conf/messages.cy
echo "investorRatioMethod.checkYourAnswersLabel = Which ratio do you want to elect for this investor group?" >> ../conf/messages.cy
echo "investorRatioMethod.error.required = Select investorRatioMethod" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInvestorRatioMethodUserAnswersEntry: Arbitrary[(InvestorRatioMethodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InvestorRatioMethodPage.type]";\
    print "        value <- arbitrary[InvestorRatioMethod].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInvestorRatioMethodPage: Arbitrary[InvestorRatioMethodPage.type] =";\
    print "    Arbitrary(InvestorRatioMethodPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInvestorRatioMethod: Arbitrary[InvestorRatioMethod] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(InvestorRatioMethod.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InvestorRatioMethodPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "  def investorRatioMethod: Option[SummaryListRow] = answer(InvestorRatioMethodPage, electionsRoutes.InvestorRatioMethodController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    InvestorRatioMethodPage.toString -> InvestorRatioMethodPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    InvestorRatioMethodPage.toString -> InvestorRatioMethodPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val investorRatioMethod = \"Which ratio do you want to elect for this investor group?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/InvestorRatioMethodControllerISpec.scala

echo "Migration InvestorRatioMethod completed"
