#!/bin/bash

echo ""
echo "Applying migration InterestAllowanceNonConsolidatedInvestmentsElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### InterestAllowanceNonConsolidatedInvestmentsElection Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "InterestAllowanceNonConsolidatedInvestmentsElection" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.InterestAllowanceNonConsolidatedInvestmentsElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.InterestAllowanceNonConsolidatedInvestmentsElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# InterestAllowanceNonConsolidatedInvestmentsElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "interestAllowanceNonConsolidatedInvestmentsElection.title = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.en
echo "interestAllowanceNonConsolidatedInvestmentsElection.heading = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.en
echo "interestAllowanceNonConsolidatedInvestmentsElection.checkYourAnswersLabel = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.en
echo "interestAllowanceNonConsolidatedInvestmentsElection.error.required = Select yes if Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# InterestAllowanceNonConsolidatedInvestmentsElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "interestAllowanceNonConsolidatedInvestmentsElection.title = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.cy
echo "interestAllowanceNonConsolidatedInvestmentsElection.heading = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.cy
echo "interestAllowanceNonConsolidatedInvestmentsElection.checkYourAnswersLabel = Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.cy
echo "interestAllowanceNonConsolidatedInvestmentsElection.error.required = Select yes if Do you want to make an interest allowance election for non-consolidated investments?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceNonConsolidatedInvestmentsElectionUserAnswersEntry: Arbitrary[(InterestAllowanceNonConsolidatedInvestmentsElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InterestAllowanceNonConsolidatedInvestmentsElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceNonConsolidatedInvestmentsElectionPage: Arbitrary[InterestAllowanceNonConsolidatedInvestmentsElectionPage.type] =";\
    print "    Arbitrary(InterestAllowanceNonConsolidatedInvestmentsElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InterestAllowanceNonConsolidatedInvestmentsElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interestAllowanceNonConsolidatedInvestmentsElection: Option[SummaryListRow] = answer(InterestAllowanceNonConsolidatedInvestmentsElectionPage, routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    InterestAllowanceNonConsolidatedInvestmentsElectionPage.toString -> InterestAllowanceNonConsolidatedInvestmentsElectionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    InterestAllowanceNonConsolidatedInvestmentsElectionPage.toString -> InterestAllowanceNonConsolidatedInvestmentsElectionPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val interestAllowanceNonConsolidatedInvestmentsElection = \"Do you want to make an interest allowance election for non-consolidated investments?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/InterestAllowanceNonConsolidatedInvestmentsElectionControllerISpec.scala

echo "Migration InterestAllowanceNonConsolidatedInvestmentsElection completed"
