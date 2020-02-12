#!/bin/bash

echo ""
echo "Applying migration ElectedInterestAllowanceConsolidatedPshipBefore"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### ElectedInterestAllowanceConsolidatedPshipBefore Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "ElectedInterestAllowanceConsolidatedPshipBefore" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.ElectedInterestAllowanceConsolidatedPshipBeforeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.ElectedInterestAllowanceConsolidatedPshipBeforeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# ElectedInterestAllowanceConsolidatedPshipBeforePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "electedInterestAllowanceConsolidatedPshipBefore.title = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceConsolidatedPshipBefore.heading = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceConsolidatedPshipBefore.checkYourAnswersLabel = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceConsolidatedPshipBefore.error.required = Select yes if Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# ElectedInterestAllowanceConsolidatedPshipBeforePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "electedInterestAllowanceConsolidatedPshipBefore.title = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceConsolidatedPshipBefore.heading = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceConsolidatedPshipBefore.checkYourAnswersLabel = Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceConsolidatedPshipBefore.error.required = Select yes if Has the group made an interest allowance election for consolidated partnerships in any previous returns?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedInterestAllowanceConsolidatedPshipBeforeUserAnswersEntry: Arbitrary[(ElectedInterestAllowanceConsolidatedPshipBeforePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ElectedInterestAllowanceConsolidatedPshipBeforePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedInterestAllowanceConsolidatedPshipBeforePage: Arbitrary[ElectedInterestAllowanceConsolidatedPshipBeforePage.type] =";\
    print "    Arbitrary(ElectedInterestAllowanceConsolidatedPshipBeforePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ElectedInterestAllowanceConsolidatedPshipBeforePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def electedInterestAllowanceConsolidatedPshipBefore: Option[SummaryListRow] = answer(ElectedInterestAllowanceConsolidatedPshipBeforePage, routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ElectedInterestAllowanceConsolidatedPshipBeforePage.toString -> ElectedInterestAllowanceConsolidatedPshipBeforePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    ElectedInterestAllowanceConsolidatedPshipBeforePage.toString -> ElectedInterestAllowanceConsolidatedPshipBeforePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val electedInterestAllowanceConsolidatedPshipBefore = \"Has the group made an interest allowance election for consolidated partnerships in any previous returns?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/ElectedInterestAllowanceConsolidatedPshipBeforeControllerISpec.scala

echo "Migration ElectedInterestAllowanceConsolidatedPshipBefore completed"
