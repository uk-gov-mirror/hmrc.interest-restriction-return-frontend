#!/bin/bash

echo ""
echo "Applying migration AccountingPeriodStart"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/accountingPeriodStart.routes
echo "### AccountingPeriodStart Controller" >> ../conf/accountingPeriodStart.routes
echo "### ----------------------------------------" >> ../conf/accountingPeriodStart.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "AccountingPeriodStart" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.accountingPeriodStart.AccountingPeriodStartController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/accountingPeriodStart.routes
echo "POST       /$kebabClassName                          controllers.accountingPeriodStart.AccountingPeriodStartController.onSubmit(mode: Mode = NormalMode)" >> ../conf/accountingPeriodStart.routes
echo "GET        /$kebabClassName/change                   controllers.accountingPeriodStart.AccountingPeriodStartController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/accountingPeriodStart.routes
echo "POST       /$kebabClassName/change                   controllers.accountingPeriodStart.AccountingPeriodStartController.onSubmit(mode: Mode = CheckMode)" >> ../conf/accountingPeriodStart.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# AccountingPeriodStartPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "accountingPeriodStart.title = AccountingPeriodStart" >> ../conf/messages.en
echo "accountingPeriodStart.heading = AccountingPeriodStart" >> ../conf/messages.en
echo "accountingPeriodStart.checkYourAnswersLabel = AccountingPeriodStart" >> ../conf/messages.en
echo "accountingPeriodStart.error.required.all = Please enter the date for accountingPeriodStart" >> ../conf/messages.en
echo "accountingPeriodStart.error.required.two= The accountingPeriodStart" must include {0} and {1} >> ../conf/messages.en
echo "accountingPeriodStart.error.required = The accountingPeriodStart must include {0}" >> ../conf/messages.en
echo "accountingPeriodStart.error.invalid = Enter a real date" >> ../conf/messages.en

echo ""
echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# AccountingPeriodStartPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "accountingPeriodStart.title = AccountingPeriodStart" >> ../conf/messages.cy
echo "accountingPeriodStart.heading = AccountingPeriodStart" >> ../conf/messages.cy
echo "accountingPeriodStart.checkYourAnswersLabel = AccountingPeriodStart" >> ../conf/messages.cy
echo "accountingPeriodStart.error.required.all = Enter the accountingPeriodStart" >> ../conf/messages.cy
echo "accountingPeriodStart.error.required.two= The accountingPeriodStart" must include {0} and {1} >> ../conf/messages.cy
echo "accountingPeriodStart.error.required = The accountingPeriodStart must include {0}" >> ../conf/messages.cy
echo "accountingPeriodStart.error.invalid = Enter a real AccountingPeriodStart" >> ../conf/messages.cy

echo ""
echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAccountingPeriodStartUserAnswersEntry: Arbitrary[(AccountingPeriodStartPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AccountingPeriodStartPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo ""
echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAccountingPeriodStartPage: Arbitrary[AccountingPeriodStartPage.type] =";\
    print "    Arbitrary(AccountingPeriodStartPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo ""
echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AccountingPeriodStartPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo ""
echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "  def accountingPeriodStart: Option[SummaryListRow] = answer(AccountingPeriodStartPage, accountingPeriodStartRoutes.AccountingPeriodStartController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    AccountingPeriodStartPage.toString -> AccountingPeriodStartPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    AccountingPeriodStartPage.toString -> AccountingPeriodStartPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val accountingPeriodStart = \"AccountingPeriodStart\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|accountingPeriodStart\/${kebabClassName}|g" ../generated-it/controllers/accountingPeriodStart/AccountingPeriodStartControllerISpec.scala

echo "Migration AccountingPeriodStart completed"
