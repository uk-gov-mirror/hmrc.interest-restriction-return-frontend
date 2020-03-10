#!/bin/bash

echo ""
echo "Applying migration ReactivationAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### ReactivationAmount Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "ReactivationAmount" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.ReactivationAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.ReactivationAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.ReactivationAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.ReactivationAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# ReactivationAmountPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "reactivationAmount.title = ReactivationAmount" >> ../conf/messages.en
echo "reactivationAmount.heading = ReactivationAmount" >> ../conf/messages.en
echo "reactivationAmount.label= Add a reactivation amount" >> ../conf/messages.en
echo "reactivationAmount.checkYourAnswersLabel = ReactivationAmount" >> ../conf/messages.en
echo "reactivationAmount.error.nonNumeric = Enter your reactivationAmount using numbers" >> ../conf/messages.en
echo "reactivationAmount.error.required = Enter your reactivationAmount" >> ../conf/messages.en
echo "reactivationAmount.error.invalidNumeric = The reactivationAmount must be valid decimal or whole number" >> ../conf/messages.en
echo "reactivationAmount.error.outOfRange = ReactivationAmount must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# ReactivationAmountPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "reactivationAmount.title = ReactivationAmount" >> ../conf/messages.cy
echo "reactivationAmount.heading = ReactivationAmount" >> ../conf/messages.cy
echo "reactivationAmount.label=Add a reactivation amount" >> ../conf/messages.cy
echo "reactivationAmount.checkYourAnswersLabel = ReactivationAmount" >> ../conf/messages.cy
echo "reactivationAmount.error.nonNumeric = Enter your reactivationAmount using numbers" >> ../conf/messages.cy
echo "reactivationAmount.error.required = Enter your reactivationAmount" >> ../conf/messages.cy
echo "reactivationAmount.error.invalidNumeric = The reactivationAmount must be valid decimal or whole number" >> ../conf/messages.cy
echo "reactivationAmount.error.outOfRange = ReactivationAmount must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReactivationAmountUserAnswersEntry: Arbitrary[(ReactivationAmountPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReactivationAmountPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReactivationAmountPage: Arbitrary[ReactivationAmountPage.type] =";\
    print "    Arbitrary(ReactivationAmountPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReactivationAmountPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def reactivationAmount: Option[SummaryListRow] = answer(ReactivationAmountPage, ukCompaniesRoutes.ReactivationAmountController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ReactivationAmountPage.toString -> ReactivationAmountPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    ReactivationAmountPage.toString -> ReactivationAmountPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val reactivationAmount = \"Add a reactivation amount\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/ReactivationAmountControllerISpec.scala

echo "Migration ReactivationAmount completed"
