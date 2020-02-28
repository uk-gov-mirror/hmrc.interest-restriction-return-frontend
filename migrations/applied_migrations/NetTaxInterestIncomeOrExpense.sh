#!/bin/bash

echo ""
echo "Applying migration NetTaxInterestIncomeOrExpense"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### NetTaxInterestIncomeOrExpense Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "NetTaxInterestIncomeOrExpense" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.NetTaxInterestIncomeOrExpenseController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.NetTaxInterestIncomeOrExpenseController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.NetTaxInterestIncomeOrExpenseController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.NetTaxInterestIncomeOrExpenseController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# NetTaxInterestIncomeOrExpensePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.title = NetTaxInterestIncomeOrExpense" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.heading = NetTaxInterestIncomeOrExpense" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.expenseRadio = Net tax-interest expense" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.incomeRadio = Net tax-interest income" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.checkYourAnswersLabel = NetTaxInterestIncomeOrExpense" >> ../conf/messages.en
echo "netTaxInterestIncomeOrExpense.error.required = Select netTaxInterestIncomeOrExpense" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# NetTaxInterestIncomeOrExpensePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.title = NetTaxInterestIncomeOrExpense" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.heading = NetTaxInterestIncomeOrExpense" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.expenseRadio = Net tax-interest expense" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.incomeRadio = Net tax-interest income" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.checkYourAnswersLabel = NetTaxInterestIncomeOrExpense" >> ../conf/messages.cy
echo "netTaxInterestIncomeOrExpense.error.required = Select netTaxInterestIncomeOrExpense" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNetTaxInterestIncomeOrExpenseUserAnswersEntry: Arbitrary[(NetTaxInterestIncomeOrExpensePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NetTaxInterestIncomeOrExpensePage.type]";\
    print "        value <- arbitrary[NetTaxInterestIncomeOrExpense].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNetTaxInterestIncomeOrExpensePage: Arbitrary[NetTaxInterestIncomeOrExpensePage.type] =";\
    print "    Arbitrary(NetTaxInterestIncomeOrExpensePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNetTaxInterestIncomeOrExpense: Arbitrary[NetTaxInterestIncomeOrExpense] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(NetTaxInterestIncomeOrExpense.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NetTaxInterestIncomeOrExpensePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "  def netTaxInterestIncomeOrExpense: Option[SummaryListRow] = answer(NetTaxInterestIncomeOrExpensePage, ukCompaniesRoutes.NetTaxInterestIncomeOrExpenseController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    NetTaxInterestIncomeOrExpensePage.toString -> NetTaxInterestIncomeOrExpensePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    NetTaxInterestIncomeOrExpensePage.toString -> NetTaxInterestIncomeOrExpensePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val netTaxInterestIncomeOrExpense = \"NetTaxInterestIncomeOrExpense\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/NetTaxInterestIncomeOrExpenseControllerISpec.scala

echo "Migration NetTaxInterestIncomeOrExpense completed"
