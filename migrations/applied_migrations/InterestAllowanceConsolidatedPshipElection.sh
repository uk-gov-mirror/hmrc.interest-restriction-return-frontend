#!/bin/bash

echo ""
echo "Applying migration InterestAllowanceConsolidatedPshipElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### InterestAllowanceConsolidatedPshipElection Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "InterestAllowanceConsolidatedPshipElection" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.InterestAllowanceConsolidatedPshipElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.InterestAllowanceConsolidatedPshipElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# InterestAllowanceConsolidatedPshipElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "interestAllowanceConsolidatedPshipElection.title = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.en
echo "interestAllowanceConsolidatedPshipElection.heading = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.en
echo "interestAllowanceConsolidatedPshipElection.checkYourAnswersLabel = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.en
echo "interestAllowanceConsolidatedPshipElection.error.required = Select yes if Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# InterestAllowanceConsolidatedPshipElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "interestAllowanceConsolidatedPshipElection.title = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.cy
echo "interestAllowanceConsolidatedPshipElection.heading = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.cy
echo "interestAllowanceConsolidatedPshipElection.checkYourAnswersLabel = Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.cy
echo "interestAllowanceConsolidatedPshipElection.error.required = Select yes if Do you want to make an interest allowance election for consolidated partnerships?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceConsolidatedPshipElectionUserAnswersEntry: Arbitrary[(InterestAllowanceConsolidatedPshipElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InterestAllowanceConsolidatedPshipElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceConsolidatedPshipElectionPage: Arbitrary[InterestAllowanceConsolidatedPshipElectionPage.type] =";\
    print "    Arbitrary(InterestAllowanceConsolidatedPshipElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InterestAllowanceConsolidatedPshipElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interestAllowanceConsolidatedPshipElection: Option[SummaryListRow] = answer(InterestAllowanceConsolidatedPshipElectionPage, routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    InterestAllowanceConsolidatedPshipElectionPage.toString -> InterestAllowanceConsolidatedPshipElectionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    InterestAllowanceConsolidatedPshipElectionPage.toString -> InterestAllowanceConsolidatedPshipElectionPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val interestAllowanceConsolidatedPshipElection = \"Do you want to make an interest allowance election for consolidated partnerships?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/InterestAllowanceConsolidatedPshipElectionControllerISpec.scala

echo "Migration InterestAllowanceConsolidatedPshipElection completed"
