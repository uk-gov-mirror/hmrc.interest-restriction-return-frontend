#!/bin/bash

echo ""
echo "Applying migration DisallowedAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/groupLevelInformation.routes
echo "### DisallowedAmount Controller" >> ../conf/groupLevelInformation.routes
echo "### ----------------------------------------" >> ../conf/groupLevelInformation.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "DisallowedAmount" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.groupLevelInformation.DisallowedAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName                          controllers.groupLevelInformation.DisallowedAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/groupLevelInformation.routes
echo "GET        /$kebabClassName/change                   controllers.groupLevelInformation.DisallowedAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes
echo "POST       /$kebabClassName/change                   controllers.groupLevelInformation.DisallowedAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/groupLevelInformation.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# DisallowedAmountPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "disallowedAmount.title = DisallowedAmount" >> ../conf/messages.en
echo "disallowedAmount.heading = DisallowedAmount" >> ../conf/messages.en
echo "disallowedAmount.label= Enter the total disallowed amount for the group" >> ../conf/messages.en
echo "disallowedAmount.checkYourAnswersLabel = DisallowedAmount" >> ../conf/messages.en
echo "disallowedAmount.error.nonNumeric = Enter your disallowedAmount using numbers" >> ../conf/messages.en
echo "disallowedAmount.error.required = Enter your disallowedAmount" >> ../conf/messages.en
echo "disallowedAmount.error.invalidNumeric = The disallowedAmount must be valid decimal or whole number" >> ../conf/messages.en
echo "disallowedAmount.error.outOfRange = DisallowedAmount must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# DisallowedAmountPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "disallowedAmount.title = DisallowedAmount" >> ../conf/messages.cy
echo "disallowedAmount.heading = DisallowedAmount" >> ../conf/messages.cy
echo "disallowedAmount.label=Enter the total disallowed amount for the group" >> ../conf/messages.cy
echo "disallowedAmount.checkYourAnswersLabel = DisallowedAmount" >> ../conf/messages.cy
echo "disallowedAmount.error.nonNumeric = Enter your disallowedAmount using numbers" >> ../conf/messages.cy
echo "disallowedAmount.error.required = Enter your disallowedAmount" >> ../conf/messages.cy
echo "disallowedAmount.error.invalidNumeric = The disallowedAmount must be valid decimal or whole number" >> ../conf/messages.cy
echo "disallowedAmount.error.outOfRange = DisallowedAmount must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDisallowedAmountUserAnswersEntry: Arbitrary[(DisallowedAmountPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DisallowedAmountPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDisallowedAmountPage: Arbitrary[DisallowedAmountPage.type] =";\
    print "    Arbitrary(DisallowedAmountPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DisallowedAmountPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def disallowedAmount: Option[SummaryListRow] = answer(DisallowedAmountPage, groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    DisallowedAmountPage.toString -> DisallowedAmountPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    DisallowedAmountPage.toString -> DisallowedAmountPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val disallowedAmount = \"Enter the total disallowed amount for the group\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|groupLevelInformation\/${kebabClassName}|g" ../generated-it/controllers/groupLevelInformation/DisallowedAmountControllerISpec.scala

echo "Migration DisallowedAmount completed"
