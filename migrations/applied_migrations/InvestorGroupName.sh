#!/bin/bash

echo ""
echo "Applying migration InvestorGroupName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### InvestorGroupName Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "InvestorGroupName" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.InvestorGroupNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.InvestorGroupNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.InvestorGroupNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.InvestorGroupNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# InvestorGroupNamePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "investorGroupName.title = investorGroupName" >> ../conf/messages.en
echo "investorGroupName.heading = investorGroupName" >> ../conf/messages.en
echo "investorGroupName.checkYourAnswersLabel = investorGroupName" >> ../conf/messages.en
echo "investorGroupName.label= Enter the name of the investor group" >> ../conf/messages.en
echo "investorGroupName.error.required = Enter investorGroupName" >> ../conf/messages.en
echo "investorGroupName.error.length = InvestorGroupName must be 160 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# InvestorGroupNamePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "investorGroupName.title = investorGroupName" >> ../conf/messages.cy
echo "investorGroupName.heading = investorGroupName" >> ../conf/messages.cy
echo "investorGroupName.checkYourAnswersLabel = investorGroupName" >> ../conf/messages.cy
echo "investorGroupName.label = Enter the name of the investor group" >> ../conf/messages.cy
echo "investorGroupName.error.required = Enter investorGroupName" >> ../conf/messages.cy
echo "investorGroupName.error.length = InvestorGroupName must be 160 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInvestorGroupNameUserAnswersEntry: Arbitrary[(InvestorGroupNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InvestorGroupNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInvestorGroupNamePage: Arbitrary[InvestorGroupNamePage.type] =";\
    print "    Arbitrary(InvestorGroupNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InvestorGroupNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def investorGroupName: Option[SummaryListRow] = answer(InvestorGroupNamePage, electionsRoutes.InvestorGroupNameController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    InvestorGroupNamePage.toString -> InvestorGroupNamePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    InvestorGroupNamePage.toString -> InvestorGroupNamePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val investorGroupName = \"Enter the name of the investor group\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/InvestorGroupNameControllerISpec.scala

echo "Migration InvestorGroupName completed"
