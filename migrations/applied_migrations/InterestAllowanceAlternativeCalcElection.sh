#!/bin/bash

echo ""
echo "Applying migration InterestAllowanceAlternativeCalcElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### InterestAllowanceAlternativeCalcElection Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "InterestAllowanceAlternativeCalcElection" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.InterestAllowanceAlternativeCalcElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.InterestAllowanceAlternativeCalcElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.InterestAllowanceAlternativeCalcElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.InterestAllowanceAlternativeCalcElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# InterestAllowanceAlternativeCalcElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "interestAllowanceAlternativeCalcElection.title = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.en
echo "interestAllowanceAlternativeCalcElection.heading = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.en
echo "interestAllowanceAlternativeCalcElection.checkYourAnswersLabel = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.en
echo "interestAllowanceAlternativeCalcElection.error.required = Select yes if Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# InterestAllowanceAlternativeCalcElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "interestAllowanceAlternativeCalcElection.title = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.cy
echo "interestAllowanceAlternativeCalcElection.heading = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.cy
echo "interestAllowanceAlternativeCalcElection.checkYourAnswersLabel = Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.cy
echo "interestAllowanceAlternativeCalcElection.error.required = Select yes if Do you want to make an interest allowance (alternative calculation) election for this return?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceAlternativeCalcElectionUserAnswersEntry: Arbitrary[(InterestAllowanceAlternativeCalcElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InterestAllowanceAlternativeCalcElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceAlternativeCalcElectionPage: Arbitrary[InterestAllowanceAlternativeCalcElectionPage.type] =";\
    print "    Arbitrary(InterestAllowanceAlternativeCalcElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InterestAllowanceAlternativeCalcElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interestAllowanceAlternativeCalcElection: Option[SummaryListRow] = answer(InterestAllowanceAlternativeCalcElectionPage, routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    InterestAllowanceAlternativeCalcElectionPage.toString -> InterestAllowanceAlternativeCalcElectionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    InterestAllowanceAlternativeCalcElectionPage.toString -> InterestAllowanceAlternativeCalcElectionPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val interestAllowanceAlternativeCalcElection = \"Do you want to make an interest allowance (alternative calculation) election for this return?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/InterestAllowanceAlternativeCalcElectionControllerISpec.scala

echo "Migration InterestAllowanceAlternativeCalcElection completed"
