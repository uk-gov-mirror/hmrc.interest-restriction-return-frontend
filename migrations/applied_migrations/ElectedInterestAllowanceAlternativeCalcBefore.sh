#!/bin/bash

echo ""
echo "Applying migration ElectedInterestAllowanceAlternativeCalcBefore"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### ElectedInterestAllowanceAlternativeCalcBefore Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "ElectedInterestAllowanceAlternativeCalcBefore" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.ElectedInterestAllowanceAlternativeCalcBeforeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.ElectedInterestAllowanceAlternativeCalcBeforeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# ElectedInterestAllowanceAlternativeCalcBeforePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "electedInterestAllowanceAlternativeCalcBefore.title = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceAlternativeCalcBefore.heading = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceAlternativeCalcBefore.checkYourAnswersLabel = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.en
echo "electedInterestAllowanceAlternativeCalcBefore.error.required = Select yes if Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# ElectedInterestAllowanceAlternativeCalcBeforePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "electedInterestAllowanceAlternativeCalcBefore.title = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceAlternativeCalcBefore.heading = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceAlternativeCalcBefore.checkYourAnswersLabel = Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.cy
echo "electedInterestAllowanceAlternativeCalcBefore.error.required = Select yes if Has the group made an interest allowance (alternative calculation) election in any previous returns?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedInterestAllowanceAlternativeCalcBeforeUserAnswersEntry: Arbitrary[(ElectedInterestAllowanceAlternativeCalcBeforePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ElectedInterestAllowanceAlternativeCalcBeforePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedInterestAllowanceAlternativeCalcBeforePage: Arbitrary[ElectedInterestAllowanceAlternativeCalcBeforePage.type] =";\
    print "    Arbitrary(ElectedInterestAllowanceAlternativeCalcBeforePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ElectedInterestAllowanceAlternativeCalcBeforePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def electedInterestAllowanceAlternativeCalcBefore: Option[SummaryListRow] = answer(ElectedInterestAllowanceAlternativeCalcBeforePage, routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ElectedInterestAllowanceAlternativeCalcBeforePage.toString -> ElectedInterestAllowanceAlternativeCalcBeforePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    ElectedInterestAllowanceAlternativeCalcBeforePage.toString -> ElectedInterestAllowanceAlternativeCalcBeforePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val electedInterestAllowanceAlternativeCalcBefore = \"Has the group made an interest allowance (alternative calculation) election in any previous returns?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/ElectedInterestAllowanceAlternativeCalcBeforeControllerISpec.scala

echo "Migration ElectedInterestAllowanceAlternativeCalcBefore completed"
