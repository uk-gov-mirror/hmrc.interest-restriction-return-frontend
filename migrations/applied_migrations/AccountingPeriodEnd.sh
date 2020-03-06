#!/bin/bash

echo ""
echo "Applying migration AccountingPeriodEnd"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/accountingPeriodEnd.routes
echo "### AccountingPeriodEnd Controller" >> ../conf/accountingPeriodEnd.routes
echo "### ----------------------------------------" >> ../conf/accountingPeriodEnd.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "AccountingPeriodEnd" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.aboutReturn.AccountingPeriodEndController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/accountingPeriodEnd.routes
echo "POST       /$kebabClassName                          controllers.aboutReturn.AccountingPeriodEndController.onSubmit(mode: Mode = NormalMode)" >> ../conf/accountingPeriodEnd.routes
echo "GET        /$kebabClassName/change                   controllers.aboutReturn.AccountingPeriodEndController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/accountingPeriodEnd.routes
echo "POST       /$kebabClassName/change                   controllers.aboutReturn.AccountingPeriodEndController.onSubmit(mode: Mode = CheckMode)" >> ../conf/accountingPeriodEnd.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# AccountingPeriodEndPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "accountingPeriodEnd.title = AccountingPeriodEnd" >> ../conf/messages.en
echo "accountingPeriodEnd.heading = AccountingPeriodEnd" >> ../conf/messages.en
echo "accountingPeriodEnd.checkYourAnswersLabel = AccountingPeriodEnd" >> ../conf/messages.en
echo "accountingPeriodEnd.error.required.all = Please enter the date for accountingPeriodEnd" >> ../conf/messages.en
echo "accountingPeriodEnd.error.required.two= The accountingPeriodEnd" must include {0} and {1} >> ../conf/messages.en
echo "accountingPeriodEnd.error.required = The accountingPeriodEnd must include {0}" >> ../conf/messages.en
echo "accountingPeriodEnd.error.invalid = Enter a real date" >> ../conf/messages.en

echo ""
echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# AccountingPeriodEndPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "accountingPeriodEnd.title = AccountingPeriodEnd" >> ../conf/messages.cy
echo "accountingPeriodEnd.heading = AccountingPeriodEnd" >> ../conf/messages.cy
echo "accountingPeriodEnd.checkYourAnswersLabel = AccountingPeriodEnd" >> ../conf/messages.cy
echo "accountingPeriodEnd.error.required.all = Enter the accountingPeriodEnd" >> ../conf/messages.cy
echo "accountingPeriodEnd.error.required.two= The accountingPeriodEnd" must include {0} and {1} >> ../conf/messages.cy
echo "accountingPeriodEnd.error.required = The accountingPeriodEnd must include {0}" >> ../conf/messages.cy
echo "accountingPeriodEnd.error.invalid = Enter a real AccountingPeriodEnd" >> ../conf/messages.cy

echo ""
echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAccountingPeriodEndUserAnswersEntry: Arbitrary[(AccountingPeriodEndPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AccountingPeriodEndPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo ""
echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAccountingPeriodEndPage: Arbitrary[AccountingPeriodEndPage.type] =";\
    print "    Arbitrary(AccountingPeriodEndPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo ""
echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AccountingPeriodEndPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo ""
echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "  def accountingPeriodEnd: Option[SummaryListRow] = answer(AccountingPeriodEndPage, accountingPeriodEndRoutes.AccountingPeriodEndController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    AccountingPeriodEndPage.toString -> AccountingPeriodEndPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    AccountingPeriodEndPage.toString -> AccountingPeriodEndPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val accountingPeriodEnd = \"AccountingPeriodEnd\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|accountingPeriodEnd\/${kebabClassName}|g" ../generated-it/controllers/accountingPeriodEnd/AccountingPeriodEndControllerISpec.scala

echo "Migration AccountingPeriodEnd completed"
